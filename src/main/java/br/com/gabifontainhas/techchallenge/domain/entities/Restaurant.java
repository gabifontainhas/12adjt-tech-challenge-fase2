package br.com.gabifontainhas.techchallenge.domain.entities;

import br.com.gabifontainhas.techchallenge.domain.valueobjects.Address;

import java.util.UUID;

public class Restaurant {
    private final UUID id;
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

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public String getOperatingHours() {
        return operatingHours;
    }

    public UUID getOwnerId() {
        return ownerId;
    }
}
