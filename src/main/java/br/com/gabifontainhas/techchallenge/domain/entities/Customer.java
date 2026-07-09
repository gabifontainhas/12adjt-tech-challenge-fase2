package br.com.gabifontainhas.techchallenge.domain.entities;

import java.time.LocalDate;
import java.util.UUID;


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

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
