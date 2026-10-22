package com.rogerjosemaria.docker_manager.models;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PullStatus {

    public enum State {
        PULLING, COMPLETED, FAILED
    }

    private final String repository;
    private final String tag;
    private final Instant startedAt;

    private volatile State state;
    private volatile int layersDone;
    private volatile int layersTotal;
    private volatile String lastMessage;
    private volatile String error;
    private volatile Instant finishedAt;

    public PullStatus(String repository, String tag) {
        this.repository = repository;
        this.tag = tag;
        this.startedAt = Instant.now();
        this.state = State.PULLING;
    }

    public String reference() {
        return repository + ":" + tag;
    }
}
