package br.com.gabifontainhas.techchallenge.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
public abstract class User {
    private final UUID id;
    private String email;
    private String name;
    private LocalDate lastUpdate;

    protected User(String name, String email) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.lastUpdate = LocalDate.now();
    }

    protected void update(String name) {
        this.name = name;
    }
}
