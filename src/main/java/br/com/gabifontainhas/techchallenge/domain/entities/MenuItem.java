package br.com.gabifontainhas.techchallenge.domain.entities;

import java.math.BigDecimal;
import java.util.UUID;

public class MenuItem {
    private final UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean dineInOnly;
    private String imagePath;
    private final UUID restaurantId;

    public MenuItem(UUID id, String name, String description, BigDecimal price, boolean dineInOnly, String imagePath, UUID restaurantId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dineInOnly = dineInOnly;
        this.imagePath = imagePath;
        this.restaurantId = restaurantId;
    }

    public MenuItem(String name, String description, BigDecimal price, boolean dineInOnly, String imagePath, UUID restaurantId) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.price = price;
        this.dineInOnly = dineInOnly;
        this.imagePath = imagePath;
        this.restaurantId = restaurantId;
    }

    public void update(String name, String description, BigDecimal price, boolean dineInOnly, String imagePath) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.dineInOnly = dineInOnly;
        this.imagePath = imagePath;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isDineInOnly() {
        return dineInOnly;
    }

    public String getImagePath() {
        return imagePath;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }
}
