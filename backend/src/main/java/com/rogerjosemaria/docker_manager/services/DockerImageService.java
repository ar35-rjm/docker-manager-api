package com.rogerjosemaria.docker_manager.services;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.SearchItem;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DockerImageService {

    private final DockerClient dockerClient;

    public List<Image> listImages() {
        return dockerClient.listImagesCmd().exec();
    }

    public List<Image> filterImages(String imageName) {
        return dockerClient.listImagesCmd().withImageNameFilter(imageName).exec();
    }

    public void pullNewImage(String imageId) {
        try {
            dockerClient.pullImageCmd(imageId)
                .exec(new PullImageResultCallback())
                .awaitCompletion(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while pulling Docker image: " + imageId, e);
        }
    }

    public void removeImage(String imageId) {
        dockerClient.removeImageCmd(imageId).exec();
    }

    public List<Container> listContainersUsingImage(String imageId) {
        return dockerClient.listContainersCmd()
            .withShowAll(true)
            .withAncestorFilter(List.of(imageId))
            .exec();
    }

    public List<SearchItem> searchImages(String imageName) {
        return dockerClient.searchImagesCmd(imageName).exec();
    }
}