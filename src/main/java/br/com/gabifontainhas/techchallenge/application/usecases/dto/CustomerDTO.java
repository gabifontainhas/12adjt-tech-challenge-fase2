package br.com.gabifontainhas.techchallenge.application.usecases.dto;

import br.com.gabifontainhas.techchallenge.domain.entities.Customer;

import java.time.LocalDate;
import java.util.UUID;


public final class CustomerDTO {

    public record PostRequest(
            String email,
            String name,
            String phoneNumber

    ) {
    }

    public record PutRequest(
            String name,
            String phoneNumber
    ) {
    }

    public record Response(
            UUID id,
            String email,
            String name,
            LocalDate lastUpdate,
            String phoneNumber
    ) {
        public Response(Customer c) {
            this(c.getId(), c.getEmail(), c.getName(), c.getLastUpdate(), c.getPhoneNumber());
        }
    }
}
