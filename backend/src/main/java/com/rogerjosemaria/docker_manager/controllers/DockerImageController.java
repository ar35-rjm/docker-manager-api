package com.rogerjosemaria.docker_manager.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.SearchItem;
import com.rogerjosemaria.docker_manager.models.PullStatus;
import com.rogerjosemaria.docker_manager.services.DockerImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class DockerImageController {

    private final DockerImageService dockerImageService;

    @GetMapping("")
    public List<Image> listImages(){
        return dockerImageService.listImages();
    }

    @GetMapping("/filter")
    public List<Image> filterImages(@RequestParam("imageName") String imageName){
        return dockerImageService.filterImages(imageName);
    }

    @GetMapping("/search")
    public List<SearchItem> searchImages(@RequestParam("term") String term){
        return dockerImageService.searchImages(term);
    }

    @PostMapping("/pull")
    public ResponseEntity<PullStatus> pullImage(
            @RequestParam("repository") String repository,
            @RequestParam(value = "tag", required = false, defaultValue = "latest") String tag){
        PullStatus status = dockerImageService.startPull(repository, tag);
        return ResponseEntity.accepted().body(status);
    }

    @GetMapping("/pull-status")
    public PullStatus pullStatus(
            @RequestParam("repository") String repository,
            @RequestParam(value = "tag", required = false, defaultValue = "latest") String tag){
        PullStatus status = dockerImageService.getPullStatus(repository, tag);
        if (status == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No pull found for " + repository + ":" + tag);
        }
        return status;
    }

    @DeleteMapping("/{id}")
    public void removeImage(
            @PathVariable("id") String imageId,
            @RequestParam(value = "force", required = false, defaultValue = "false") boolean force){
        dockerImageService.removeImage(imageId, force);
    }

    @GetMapping("/{id}/containers")
    public List<Container> listContainersUsingImage(@PathVariable("id") String imageId){
        return dockerImageService.listContainersUsingImage(imageId);
    }
}
