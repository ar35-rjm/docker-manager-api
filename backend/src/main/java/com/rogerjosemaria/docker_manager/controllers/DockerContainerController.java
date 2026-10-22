package com.rogerjosemaria.docker_manager.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.dockerjava.api.model.Container;
import com.rogerjosemaria.docker_manager.services.DockerService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/container")
@AllArgsConstructor
public class DockerContainerController {

    private final DockerService dockerService;

    @GetMapping("")
    public List<Container> listContainers(@RequestParam(value = "all", required = false, defaultValue = "true") boolean all){
        return dockerService.listContainers(all);
    }

    @PostMapping("")
    public void createContainer(@RequestParam("imageName") String imageName){
        dockerService.createContainer(imageName);
    }

    @PostMapping("/{id}/start")
    public void startContainer(@PathVariable("id") String id){
        dockerService.startContainer(id);
    }

    @PostMapping("/{id}/stop")
    public void stopContainer(@PathVariable("id") String id){
        dockerService.stopContainer(id);
    }

    @PostMapping("/{id}")
    public void deleteContainer(@PathVariable("id") String id){
        dockerService.deleteContainer(id);
    }
}
