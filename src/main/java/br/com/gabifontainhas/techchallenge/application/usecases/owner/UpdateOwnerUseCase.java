package br.com.gabifontainhas.techchallenge.application.usecases.owner;

import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.application.usecases.dto.OwnerDTO;
import br.com.gabifontainhas.techchallenge.domain.entities.Owner;

import java.util.UUID;

public class UpdateOwnerUseCase {
    private final OwnerRepository ownerRepository;

    public UpdateOwnerUseCase(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }


    public Owner update(OwnerDTO.PutRequest request, UUID id) {
        var owner = ownerRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Owner not found"));
        owner.update(request.name(), request.businessPhone());
        return ownerRepository.save(owner);
    }
}
