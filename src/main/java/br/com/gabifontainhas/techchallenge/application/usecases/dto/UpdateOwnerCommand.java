package br.com.gabifontainhas.techchallenge.application.usecases.dto;

public record UpdateOwnerCommand(
        String name,
        String businessPhone
) {
}

