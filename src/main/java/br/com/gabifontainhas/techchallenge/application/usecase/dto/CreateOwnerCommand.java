package br.com.gabifontainhas.techchallenge.application.usecase.dto;

public record CreateOwnerCommand (
            String email,
            String name,
            String businessPhone
    ) {
    }
