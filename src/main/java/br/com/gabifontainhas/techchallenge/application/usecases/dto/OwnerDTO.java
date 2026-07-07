package br.com.gabifontainhas.techchallenge.application.usecases.dto;

import br.com.gabifontainhas.techchallenge.domain.entities.Owner;

import java.time.LocalDate;
import java.util.UUID;

public final class OwnerDTO {
    public record PostRequest(
            String email,
            String name,
            String restaurantName
    ) {
    }

    public record PutRequest(
            String name,
            String restaurantName
    ) {
    }

    public record Response(
            UUID id,
            String email,
            String name,
            LocalDate lastUpdate,
            String restaurantName
    ) {
        public Response(Owner o) {
            this(o.getId(), o.getEmail(), o.getName(), o.getLastUpdate(), o.getRestaurantName());
        }
    }

}
