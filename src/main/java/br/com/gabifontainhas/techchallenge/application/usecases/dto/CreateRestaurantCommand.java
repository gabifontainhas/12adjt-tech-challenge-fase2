package br.com.gabifontainhas.techchallenge.application.usecases.dto;

import br.com.gabifontainhas.techchallenge.domain.valueobjects.Address;

import java.util.UUID;

public record CreateRestaurantCommand(
        String name,
        Address address,
        String cuisineType,
        String operatingHours,
        UUID ownerId
) {
}
