package br.com.gabifontainhas.techchallenge.domain.valueobjects;


public record Address(
        String street,
        String number,
        String neighborhood,
        String city,
        String state,
        String zipCode
) {
}
