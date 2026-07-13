package br.com.gabifontainhas.techchallenge.application.usecase.dto;

public record UpdateOwnerCommand(
        String name,
        String businessPhone
) {
}

