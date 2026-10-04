package com.rogerjosemaria.docker_manager.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class DockerContainerService {

    private final DockerClient dockerClient;

    public List<Container> listContainers(Boolean listAll){
        return dockerClient.listContainersCmd()
                .withShowAll(listAll)
                .exec();
    }

    public void startContainer(String id){
        dockerClient.startContainerCmd(id).exec();
    }

    public void stopContainer(String id){
        dockerClient.stopContainerCmd(id).exec();
    }

    public void deleteContainer(String id){
        dockerClient.removeContainerCmd(id).exec();
    }

    public void createContainer(String imageName){
        dockerClient.createContainerCmd(imageName).exec();
    }

}
