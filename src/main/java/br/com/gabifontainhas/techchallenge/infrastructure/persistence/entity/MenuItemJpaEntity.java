package br.com.gabifontainhas.techchallenge.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "menu_items")
@NoArgsConstructor
@Getter
public class MenuItemJpaEntity {
    @Id
    private UUID id;

    private String name;

    private String description;

    private BigDecimal price;

    private boolean dineInOnly;

    private String imagePath;

    private UUID restaurantId;

    public MenuItemJpaEntity(UUID id, String name, String description, BigDecimal price, boolean dineInOnly, String imagePath, UUID restaurantId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dineInOnly = dineInOnly;
        this.imagePath = imagePath;
        this.restaurantId = restaurantId;
    }
}
