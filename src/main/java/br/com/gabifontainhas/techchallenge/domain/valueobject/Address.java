package br.com.gabifontainhas.techchallenge.domain.valueobject;


public record Address(
        String street,
        String number,
        String neighborhood,
        String city,
        String state,
        String zipCode
) {
}
