package br.com.gabifontainhas.techchallenge.application.usecase.dto;

import br.com.gabifontainhas.techchallenge.domain.valueobject.Address;

import java.util.UUID;

public record UpdateRestaurantCommand(
        String name,
        Address address,
        String cuisineType,
        String operatingHours,
        UUID ownerId
) {
}
