package br.com.gabifontainhas.techchallenge.infrastructure.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "restaurants")
@NoArgsConstructor
@Getter
public class RestaurantJpaEntity {

    @Id
    private UUID id;

    @Column(unique = true)
    private String name;

    private String cuisineType;

    private String operatingHours;

    private UUID ownerId;

    @Embedded
    private AddressEmbeddable address;

    public RestaurantJpaEntity(UUID id, String name, String cuisineType, String operatingHours, UUID ownerId, AddressEmbeddable address) {
        this.id = id;
        this.name = name;
        this.cuisineType = cuisineType;
        this.operatingHours = operatingHours;
        this.ownerId = ownerId;
        this.address = address;
    }
}

