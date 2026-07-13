package br.com.gabifontainhas.techchallenge.application.usecase.dto;


public record UpdateCustomerCommand(
        String name,
        String phoneNumber
) {
}

