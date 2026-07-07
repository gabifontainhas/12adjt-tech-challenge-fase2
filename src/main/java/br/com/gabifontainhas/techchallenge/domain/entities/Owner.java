package br.com.gabifontainhas.techchallenge.domain.entities;

import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class Owner extends User {
    private String restaurantName;

    public Owner(String email, String name, String restaurantName) {
        super(name, email);
        this.restaurantName = restaurantName;
    }

    public Owner(UUID id, String email, String name, LocalDate lastUpdate, String restaurantName) {
        super(id, email, name, lastUpdate);
        this.restaurantName = restaurantName;
    }

    public void update(String name, String restaurantName) {
        super.update(name);
        this.restaurantName = restaurantName;
    }
}
