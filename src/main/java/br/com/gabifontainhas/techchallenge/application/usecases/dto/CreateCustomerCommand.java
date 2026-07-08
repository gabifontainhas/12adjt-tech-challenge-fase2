package br.com.gabifontainhas.techchallenge.application.usecases.dto;


public record CreateCustomerCommand(
        String email,
        String name,
        String phoneNumber) {
}
