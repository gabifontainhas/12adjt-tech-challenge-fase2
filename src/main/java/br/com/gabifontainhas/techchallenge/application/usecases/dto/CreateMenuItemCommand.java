package br.com.gabifontainhas.techchallenge.application.usecases.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateMenuItemCommand(
        String name,
        String description,
        BigDecimal price,
        boolean dineInOnly,
        String imagePath,
        UUID restaurantId
) {
}
