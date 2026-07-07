package br.com.gabifontainhas.techchallenge.application.usecases.dto;

import br.com.gabifontainhas.techchallenge.domain.entities.Restaurant;

import java.util.UUID;

public class RestaurantDTO {

    public record PostRequest(
            String name,
            AddressDTO.Request address,
            String cuisineType,
            String operatingHours,
            UUID ownerId
    ) {
    }

    public record PutRequest(
            String name,
            AddressDTO.Request address,
            String cuisineType,
            String operatingHours,
            UUID ownerId
    ) {
    }

    public record Response(
            UUID id,
            String name,
            AddressDTO.Response address,
            String cuisineType,
            String operatingHours,
            UUID ownerId
    ) {
        public Response(Restaurant r) {
            this(r.getId(), r.getName(), new AddressDTO.Response(r.getAddress()), r.getCuisineType(), r.getOperatingHours(), r.getOwnerId());
        }
    }

}
