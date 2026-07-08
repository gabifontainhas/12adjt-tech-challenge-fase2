package br.com.gabifontainhas.techchallenge.application.usecases.owner;

import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.domain.entities.Owner;

import java.util.List;
import java.util.UUID;

public class ListOwnersUseCase {

    private final OwnerRepository ownerRepository;

    public ListOwnersUseCase(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public List<Owner> getAllOwners() {
        return ownerRepository.findAll();
    }

    public Owner getOwnerById(UUID uuid) {
        return ownerRepository.findById(uuid).orElseThrow(() -> new UserNotFoundException("Owner not found"));
    }
}
