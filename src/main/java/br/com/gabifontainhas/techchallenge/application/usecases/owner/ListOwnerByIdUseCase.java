package br.com.gabifontainhas.techchallenge.application.usecases.owner;

import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.domain.entities.Owner;

import java.util.UUID;

public class ListOwnerByIdUseCase {

    private final OwnerRepository ownerRepository;

    public ListOwnerByIdUseCase(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Owner getOwnerById(UUID uuid) {
        return ownerRepository.findById(uuid).orElseThrow(() -> new UserNotFoundException("Owner not found"));
    }
}
