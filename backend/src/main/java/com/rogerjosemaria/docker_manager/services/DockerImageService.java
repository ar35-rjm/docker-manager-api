package com.rogerjosemaria.docker_manager.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class DockerImageService {

    private final DockerClient dockerClient;

    public List<Image> listImages(){
        return dockerClient.listImagesCmd().exec();
    }

    public List<Image> filterImages(String imageName){
        return dockerClient.listImagesCmd().withImageNameFilter(imageName).exec();
    }

    public void pullNewImage(String imageName){
        //search about pulling and than put it here
    }
}
