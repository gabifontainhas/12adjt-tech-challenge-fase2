package br.com.gabifontainhas.techchallenge.application.usecases.dto;

public record CreateOwnerCommand (
            String email,
            String name,
            String businessPhone
    ) {
    }
