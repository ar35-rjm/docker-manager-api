package com.rogerjosemaria.docker_manager.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.dockerjava.api.model.Image;
import com.rogerjosemaria.docker_manager.services.DockerService;

import lombok.AllArgsConstructor;

@RestController 
@RequestMapping("/api/images")
@AllArgsConstructor 
public class DockerImageController {

    private final DockerService dockerService;

    @GetMapping("")
    public List<Image> listImages(){
        return dockerService.listImages();
    }

    @GetMapping("/filter")
    public List<Image> listImages(@RequestParam(required = false, defaultValue = "image-") String imageName){
        return dockerService.filterImages(imageName);
    }

}
