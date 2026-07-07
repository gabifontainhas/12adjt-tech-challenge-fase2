package br.com.gabifontainhas.techchallenge.application.usecases.owner;

import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.application.usecases.dto.OwnerDTO;
import br.com.gabifontainhas.techchallenge.domain.entities.Owner;
import br.com.gabifontainhas.techchallenge.application.exception.EmailAlreadyExistsException;

public class CreateOwnerUseCase {
    private final OwnerRepository ownerRepository;

    public CreateOwnerUseCase(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Owner create(OwnerDTO.PostRequest request) {
        if (ownerRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("E-mail already exists");
        }
        var owner = new Owner(request.email(), request.name(), request.restaurantName());
        return ownerRepository.save(owner);
    }
}