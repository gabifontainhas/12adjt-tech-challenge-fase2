package br.com.gabifontainhas.techchallenge.infrastructure.persistance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "owners")
@NoArgsConstructor
@Getter
public class OwnerJpaEntity extends UserJpaEntity {
    private String restaurantName;

    public OwnerJpaEntity(UUID id, String email, String name, String restaurantName) {
        super(id, email, name);
        this.restaurantName = restaurantName;
    }

    public OwnerJpaEntity(UUID id, String email, String name, String restaurantName, LocalDate lastUpdate) {
        super(id, email, name, lastUpdate);
        this.restaurantName = restaurantName;
    }
}
