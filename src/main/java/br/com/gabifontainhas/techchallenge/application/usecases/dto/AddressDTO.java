package br.com.gabifontainhas.techchallenge.application.usecases.dto;

import br.com.gabifontainhas.techchallenge.domain.valueobjects.Address;

public final class AddressDTO {
    public record Request(

            String street,

            String number,

            String neighborhood,

            String city,

            String state,

            String zipCode
    ) {
        public Address toDomain() {
            return new Address(
                    this.street,
                    this.number,
                    this.neighborhood,
                    this.city,
                    this.state,
                    this.zipCode
            );
        }
    }

    public record Response(

            String street,

            String number,

            String neighborhood,

            String city,

            String state,

            String zipCode
    ) {
        public Response(Address a) {
            this(a.street(), a.number(), a.neighborhood(), a.city(), a.state(), a.zipCode());
        }
    }
}
