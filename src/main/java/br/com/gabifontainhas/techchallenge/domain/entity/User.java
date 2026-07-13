package br.com.gabifontainhas.techchallenge.domain.entity;

import java.time.LocalDate;
import java.util.UUID;

public abstract class User {
    private final UUID id;
    private String email;
    private String name;
    private LocalDate lastUpdate;

    protected User(String email, String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.lastUpdate = LocalDate.now();
    }

    public User(UUID id, String email, String name, LocalDate lastUpdate) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.lastUpdate = lastUpdate;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    protected void update(String name) {
        this.name = name;
    }
}
