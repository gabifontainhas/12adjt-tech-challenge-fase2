package br.com.gabifontainhas.techchallenge.infrastructure.persistance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "customers")
@NoArgsConstructor
@Getter
public class CustomerJpaEntity extends UserJpaEntity {

    private String phoneNumber;

    public CustomerJpaEntity(UUID id, String email, String name, String phoneNumber, LocalDate lastUpdate) {
        super(id, email, name, lastUpdate);
        this.phoneNumber = phoneNumber;
    }
}
