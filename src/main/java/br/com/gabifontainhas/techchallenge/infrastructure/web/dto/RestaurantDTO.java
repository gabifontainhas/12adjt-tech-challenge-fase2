package br.com.gabifontainhas.techchallenge.infrastructure.web.dto;

import br.com.gabifontainhas.techchallenge.application.usecase.dto.CreateRestaurantCommand;
import br.com.gabifontainhas.techchallenge.application.usecase.dto.UpdateRestaurantCommand;
import br.com.gabifontainhas.techchallenge.domain.entity.Restaurant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class RestaurantDTO {
    private RestaurantDTO() {
    }

    public record PostRequest(

            @NotBlank
            String name,

            @NotNull
            AddressDTO.Request address,

            @NotBlank
            String cuisineType,

            @NotBlank
            String operatingHours,

            @NotNull
            UUID ownerId
    ) {
        public CreateRestaurantCommand toCommand() {
            return new CreateRestaurantCommand(
                    this.name,
                    this.address.toDomain(),
                    this.cuisineType,
                    this.operatingHours,
                    this.ownerId
            );
        }
    }

    public record PutRequest(

            @NotBlank
            String name,

            @NotNull
            AddressDTO.Request address,

            @NotBlank
            String cuisineType,

            @NotBlank
            String operatingHours,

            @NotNull
            UUID ownerId
    ) {
        public UpdateRestaurantCommand toCommand() {
            return new UpdateRestaurantCommand(
                    this.name,
                    this.address.toDomain(),
                    this.cuisineType,
                    this.operatingHours,
                    this.ownerId
            );
        }
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
