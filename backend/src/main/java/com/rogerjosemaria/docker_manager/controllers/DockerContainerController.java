package com.rogerjosemaria.docker_manager.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.dockerjava.api.model.Container;
import com.rogerjosemaria.docker_manager.services.DockerContainerService;

import lombok.AllArgsConstructor;

@RestController 
@RequestMapping("/api/v1/container")
@AllArgsConstructor 
public class DockerContainerController {

    private final DockerContainerService dockerContainerService;

    @GetMapping("")
    public List<Container> listContainers(Boolean value){
        Boolean all = value!=null ? value : true;
        return dockerContainerService.listContainers(all);
    }

    @PostMapping("")
    public void createContainer(String imageName){
        dockerContainerService.createContainer(imageName);
    }
    
    @PostMapping("/{id}/start")
    public void startContainer(String id){
        dockerContainerService.startContainer(id);
    }
    
    @PostMapping("/{id}/stop")
    public void stopContainer(String id){
        dockerContainerService.stopContainer(id);
    }
    
    @PostMapping("/{id}")
    public void deleteContainer(String id){
        dockerContainerService.deleteContainer(id);
    }
    
}
