package br.com.gabifontainhas.techchallenge.application.usecases.dto;


public record UpdateCustomerCommand(
        String name,
        String phoneNumber
) {
}

