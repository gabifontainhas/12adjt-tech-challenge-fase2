package br.com.gabifontainhas.techchallenge.infrastructure.web.dto;

import br.com.gabifontainhas.techchallenge.domain.valueobject.Address;
import jakarta.validation.constraints.NotBlank;

public final class AddressDTO {
    private AddressDTO() {
    }

    public record Request(

            @NotBlank
            String street,

            @NotBlank
            String number,

            @NotBlank
            String neighborhood,

            @NotBlank
            String city,

            @NotBlank
            String state,

            @NotBlank
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
