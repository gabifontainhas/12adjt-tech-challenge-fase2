package br.com.gabifontainhas.techchallenge.application.usecases.dto;

import br.com.gabifontainhas.techchallenge.domain.entities.MenuItem;
import br.com.gabifontainhas.techchallenge.domain.entities.Restaurant;

import java.math.BigDecimal;
import java.util.UUID;

public class MenuItemDTO {
    public record PostRequest(
            String name,
            String description,
            BigDecimal price,
            boolean dineInOnly,
            String imagePath,
            UUID restaurantId
    ) {
    }

    public record PutRequest(
            String name,
            String description,
            BigDecimal price,
            boolean dineInOnly,
            String imagePath
    ) {
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
