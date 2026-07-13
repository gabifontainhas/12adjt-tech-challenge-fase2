package br.com.gabifontainhas.techchallenge.application.usecase.dto;


public record CreateCustomerCommand(
        String email,
        String name,
        String phoneNumber) {
}
