package br.com.gabifontainhas.techchallenge.application.usecase.dto;

import java.math.BigDecimal;

public record UpdateMenuItemCommand(
        String name,
        String description,
        BigDecimal price,
        boolean dineInOnly,
        String imagePath
) {
}

