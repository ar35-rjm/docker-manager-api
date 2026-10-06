package com.rogerjosemaria.docker_manager.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.dockerjava.api.model.Image;
import com.rogerjosemaria.docker_manager.services.DockerImageService;

import lombok.AllArgsConstructor;

@RestController 
@RequestMapping("/api/v1/images")
@AllArgsConstructor 
public class DockerImageController {

    private final DockerImageService dockerImageService;

    @GetMapping("")
    public List<Image> listImages(){
        return dockerImageService.listImages();
    }

    @GetMapping("/filter")
    public List<Image> listImages(@RequestParam(required = false, defaultValue = "image-") String imageName){
        return dockerImageService.filterImages(imageName);
    }

    // @GetMapping("/search")
    // public List<Image> searchImages(@RequestParam(required = false, defaultValue = "image-") String imageName){
    //     return dockerImageService.searchImages(imageName);
    // }

    // @GetMapping("/pull")
    // public void pullNewImage(@RequestParam(required = false, defaultValue = "image-") String imageName){
    //     dockerImageService.pullNewImage(imageName);
    // }

    // @GetMapping("/remove")
    // public void removeImage(@RequestParam(required = false, defaultValue = "image-") String imageId){
    //     dockerImageService.removeImage(imageId);
    // }

    // @GetMapping("/containers")
    // public List<Container> listContainersUsingImage(@RequestParam(required = false, defaultValue = "image-") String imageId){
    //     return dockerImageService.listContainersUsingImage(imageId);
    // }

}
