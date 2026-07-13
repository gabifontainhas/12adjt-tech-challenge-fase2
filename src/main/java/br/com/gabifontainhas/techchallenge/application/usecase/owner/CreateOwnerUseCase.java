package br.com.gabifontainhas.techchallenge.application.usecase.owner;

import br.com.gabifontainhas.techchallenge.application.exception.EmailAlreadyExistsException;
import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.application.usecase.dto.CreateOwnerCommand;
import br.com.gabifontainhas.techchallenge.domain.entity.Owner;

public class CreateOwnerUseCase {
    private final OwnerRepository ownerRepository;

    public CreateOwnerUseCase(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Owner create(CreateOwnerCommand request) {
        if (ownerRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("E-mail already exists");
        }
        var owner = new Owner(request.email(), request.name(), request.businessPhone());
        return ownerRepository.save(owner);
    }
}