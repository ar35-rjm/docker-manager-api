package com.rogerjosemaria.docker_manager.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.dockerjava.api.model.Container;
import com.rogerjosemaria.docker_manager.services.DockerService;

import lombok.AllArgsConstructor;

@RestController 
@RequestMapping("/api/container")
@AllArgsConstructor 
public class DockerContainerController {

    private final DockerService dockerService;

    @GetMapping("")
    public List<Container> listContainers(Boolean value){
        Boolean all = value!=null ? value : true;
        return dockerService.listContainers(all);
    }

    @PostMapping("")
    public void createContainer(String imageName){
        dockerService.createContainer(imageName);
    }
    
    @PostMapping("/{id}/start")
    public void startContainer(String id){
        dockerService.startContainer(id);
    }
    
    @PostMapping("/{id}/stop")
    public void stopContainer(String id){
        dockerService.stopContainer(id);
    }
    
    @PostMapping("/{id}")
    public void deleteContainer(String id){
        dockerService.deleteContainer(id);
    }
    
}
