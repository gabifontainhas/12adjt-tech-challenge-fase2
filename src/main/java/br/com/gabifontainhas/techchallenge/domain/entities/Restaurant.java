package br.com.gabifontainhas.techchallenge.domain.entities;

import br.com.gabifontainhas.techchallenge.domain.valueobjects.Address;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Restaurant {
    private UUID id;
    private String name;
    private Address address;
    private String cuisineType;
    private String operatingHours;
    private UUID ownerId;

    public Restaurant(String name, Address address, String cuisineType, String operatingHours, UUID ownerId) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.address = address;
        this.cuisineType = cuisineType;
        this.operatingHours = operatingHours;
        this.ownerId = ownerId;
    }

    public Restaurant(UUID id, String name, Address address, String cuisineType, String operatingHours, UUID ownerId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.cuisineType = cuisineType;
        this.operatingHours = operatingHours;
        this.ownerId = ownerId;
    }

    public void update(String name, Address address, String cuisineType, String operatingHours, UUID ownerId) {
        this.name = name;
        this.address = address;
        this.cuisineType = cuisineType;
        this.operatingHours = operatingHours;
        this.ownerId = ownerId;
    }
}
