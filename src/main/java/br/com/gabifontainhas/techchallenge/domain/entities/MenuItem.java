package br.com.gabifontainhas.techchallenge.domain.entities;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
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
}
