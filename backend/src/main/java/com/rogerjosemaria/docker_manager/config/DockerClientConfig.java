package com.rogerjosemaria.docker_manager.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
//import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;

@Configuration
public class DockerClientConfig {

    // Fixed missing '}' and provided default fallback
    @Value("${docker.socket.path:unix:///var/run/docker.sock}")
    private String dockerSocketPath;

    @Bean
    public DockerClient dockerClient() {
        DefaultDockerClientConfig.Builder configBuilder = DefaultDockerClientConfig
                .createDefaultConfigBuilder();

        if (this.dockerSocketPath != null && !this.dockerSocketPath.isBlank()) {
            configBuilder.withDockerHost(this.dockerSocketPath)
                         .withDockerTlsVerify(false);
        }

        DefaultDockerClientConfig config = configBuilder.build();

        // Build ApacheDockerHttpClient with explicit timeouts
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

        // Pass built 'config' (not 'configBuilder')
        return DockerClientImpl.getInstance(config, httpClient);
    }
}