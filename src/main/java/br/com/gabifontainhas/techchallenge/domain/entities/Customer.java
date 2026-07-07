package br.com.gabifontainhas.techchallenge.domain.entities;

import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class Customer extends User {
    private String phoneNumber;

    public Customer(String email, String name, String phoneNumber) {
        super(name, email);
        this.phoneNumber = phoneNumber;
    }

    public Customer(UUID id, String email, String name, LocalDate lastUpdate, String phoneNumber) {
        super(id, email, name, lastUpdate);
        this.phoneNumber = phoneNumber;
    }

    public void update(String name, String phoneNumber) {
        super.update(name);
        this.phoneNumber = phoneNumber;
    }
}
