package br.com.gabifontainhas.techchallenge.application.usecases.owner;

import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;

import java.util.UUID;

public class DeleteOwnerUseCase {
    private final OwnerRepository ownerRepository;

    public DeleteOwnerUseCase(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public void delete(UUID id) {
        if (!ownerRepository.existsById(id)) {
            throw new UserNotFoundException("Could not delete: Owner with ID " + id + " not found");
        }
        ownerRepository.delete(id);
    }
}
