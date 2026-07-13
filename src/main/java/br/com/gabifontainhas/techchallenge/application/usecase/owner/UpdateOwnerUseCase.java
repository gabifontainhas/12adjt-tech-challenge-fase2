package br.com.gabifontainhas.techchallenge.application.usecase.owner;

import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.application.usecase.dto.UpdateOwnerCommand;
import br.com.gabifontainhas.techchallenge.domain.entity.Owner;

import java.util.UUID;

public class UpdateOwnerUseCase {
    private final OwnerRepository ownerRepository;

    public UpdateOwnerUseCase(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }


    public Owner update(UpdateOwnerCommand request, UUID id) {
        var owner = ownerRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Owner not found"));
        owner.update(request.name(), request.businessPhone());
        return ownerRepository.save(owner);
    }
}
