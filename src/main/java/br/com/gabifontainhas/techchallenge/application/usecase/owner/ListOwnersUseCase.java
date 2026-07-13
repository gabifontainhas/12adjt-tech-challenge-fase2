package br.com.gabifontainhas.techchallenge.application.usecase.owner;

import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.domain.entity.Owner;

import java.util.List;

public class ListOwnersUseCase {

    private final OwnerRepository ownerRepository;

    public ListOwnersUseCase(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public List<Owner> getAllOwners() {
        return ownerRepository.findAll();
    }

}
