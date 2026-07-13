package br.com.gabifontainhas.techchallenge.infrastructure.web.dto;

import br.com.gabifontainhas.techchallenge.application.usecase.dto.CreateMenuItemCommand;
import br.com.gabifontainhas.techchallenge.application.usecase.dto.UpdateMenuItemCommand;
import br.com.gabifontainhas.techchallenge.domain.entity.MenuItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public final class MenuItemDTO {
    private MenuItemDTO() {
    }

    public record PostRequest(

            @NotBlank
            String name,

            @NotBlank
            String description,

            @NotNull
            BigDecimal price,

            @NotNull
            boolean dineInOnly,

            @NotBlank
            String imagePath,

            @NotNull
            UUID restaurantId
    ) {
        public CreateMenuItemCommand toCommand() {
            return new CreateMenuItemCommand(
                    this.name,
                    this.description,
                    this.price,
                    this.dineInOnly,
                    this.imagePath,
                    this.restaurantId
            );
        }
    }

    public record PutRequest(

            @NotBlank
            String name,

            @NotBlank
            String description,

            @NotNull
            BigDecimal price,

            @NotNull
            boolean dineInOnly,

            @NotBlank
            String imagePath
    ) {
        public UpdateMenuItemCommand toCommand() {
            return new UpdateMenuItemCommand(
                    this.name,
                    this.description,
                    this.price,
                    this.dineInOnly,
                    this.imagePath
            );
        }
    }

    public record Response(
            UUID id,
            String name,
            String description,
            BigDecimal price,
            boolean dineInOnly,
            String imagePath,
            UUID restaurantId
    ) {
        public Response(MenuItem m) {
            this(m.getId(), m.getName(), m.getDescription(), m.getPrice(), m.isDineInOnly(), m.getImagePath(), m.getRestaurantId());
        }
    }
}
