package br.com.gabifontainhas.techchallenge.application.usecases.dto;

import java.math.BigDecimal;

public record UpdateMenuItemCommand(
        String name,
        String description,
        BigDecimal price,
        boolean dineInOnly,
        String imagePath
) {
}

