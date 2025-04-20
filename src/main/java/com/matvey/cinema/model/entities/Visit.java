package com.matvey.cinema.model.entities;

import java.time.LocalDateTime;

public class Visit {
    private String id;
    private String url;
    private LocalDateTime timestamp;

    public Visit(String id, String url, LocalDateTime timestamp) {
        this.id = id;
        this.url = url;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
