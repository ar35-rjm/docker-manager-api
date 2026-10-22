package com.rogerjosemaria.docker_manager.services;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.SearchItem;
import com.rogerjosemaria.docker_manager.models.PullStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DockerImageService {

    private final DockerClient dockerClient;
    private final ExecutorService dockerPullExecutor;

    // pullStatuses is initialized here, so @RequiredArgsConstructor keeps it
    // out of the constructor and lets Spring inject only the two fields above.
    private final Map<String, PullStatus> pullStatuses = new ConcurrentHashMap<>();

    public List<Image> listImages() {
        return dockerClient.listImagesCmd().exec();
    }

    public List<Image> filterImages(String imageName) {
        return dockerClient.listImagesCmd().withImageNameFilter(imageName).exec();
    }

    public List<SearchItem> searchImages(String term) {
        return dockerClient.searchImagesCmd(term).exec();
    }

    public PullStatus startPull(String repository, String tag) {
        String repo = repository == null ? "" : repository.trim();
        String resolvedTag = tag == null || tag.isBlank() ? "latest" : tag.trim();
        if (repo.isBlank()) {
            throw new IllegalArgumentException("repository is required");
        }

        String reference = repo + ":" + resolvedTag;

        PullStatus existing = pullStatuses.get(reference);
        if (existing != null && existing.getState() == PullStatus.State.PULLING) {
            return existing;
        }

        PullStatus status = new PullStatus(repo, resolvedTag);
        pullStatuses.put(reference, status);
        dockerPullExecutor.submit(() -> doPull(status, repo, resolvedTag));
        return status;
    }

    public PullStatus getPullStatus(String repository, String tag) {
        String repo = repository == null ? "" : repository.trim();
        String resolvedTag = tag == null || tag.isBlank() ? "latest" : tag.trim();
        return pullStatuses.get(repo + ":" + resolvedTag);
    }

    private void doPull(PullStatus status, String repository, String tag) {
        PullImageResultCallback callback = new PullImageResultCallback() {
            @Override
            public void onNext(PullResponseItem item) {
                super.onNext(item);
                recordProgress(status, item);
            }
        };

        try (callback) {
            dockerClient.pullImageCmd(repository)
                    .withTag(tag)
                    .exec(callback);
            // The daemon signals completion or failure through the callback;
            // awaitCompletion() returns once the stream is fully processed.
            callback.awaitCompletion();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail(status, "Pull interrupted: " + e.getMessage());
            return;
        } catch (DockerException e) {
            fail(status, e.getMessage());
            return;
        } catch (Exception e) {
            // Transport/IO failures outside the DockerException hierarchy;
            // without this the status would stay PULLING forever.
            fail(status, e.getMessage());
            return;
        }

        // Some registry errors arrive as stream items with an errorDetail
        // instead of an onError callback, so double-check before reporting
        // success.
        if (status.getError() != null) {
            status.setState(PullStatus.State.FAILED);
        } else {
            status.setState(PullStatus.State.COMPLETED);
        }
        status.setFinishedAt(Instant.now());
    }

    private void fail(PullStatus status, String message) {
        if (status.getError() == null && message != null) {
            status.setError(message);
        }
        status.setState(PullStatus.State.FAILED);
        status.setFinishedAt(Instant.now());
    }

    private void recordProgress(PullStatus status, PullResponseItem item) {
        if (item.getErrorDetail() != null) {
            status.setError(item.getErrorDetail().getMessage());
            return;
        }

        String layerStatus = item.getStatus();
        if (layerStatus == null) {
            return;
        }
        switch (layerStatus) {
            case "Pulling fs layer" -> status.setLayersTotal(status.getLayersTotal() + 1);
            case "Pull complete", "Already exists" -> status.setLayersDone(status.getLayersDone() + 1);
            default -> status.setLastMessage(layerStatus);
        }
    }

    public void removeImage(String imageId, boolean force) {
        dockerClient.removeImageCmd(imageId).withForce(force).exec();
    }

    public List<Container> listContainersUsingImage(String imageId) {
        return dockerClient.listContainersCmd()
            .withShowAll(true)
            .withAncestorFilter(List.of(imageId))
            .exec();
    }
}
