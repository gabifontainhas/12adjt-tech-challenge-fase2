package br.com.gabifontainhas.techchallenge.domain.entities;

import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class Owner extends User {
    private String businessPhone;

    public Owner(String email, String name, String businessPhone) {
        super(name, email);
        this.businessPhone = businessPhone;
    }

    public Owner(UUID id, String email, String name, LocalDate lastUpdate, String businessPhone) {
        super(id, email, name, lastUpdate);
        this.businessPhone = businessPhone;
    }

    public void update(String name, String restaurantName) {
        super.update(name);
        this.businessPhone = restaurantName;
    }
}
