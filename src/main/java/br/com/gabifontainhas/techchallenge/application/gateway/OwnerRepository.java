package br.com.gabifontainhas.techchallenge.application.gateway;

import br.com.gabifontainhas.techchallenge.domain.entities.Customer;
import br.com.gabifontainhas.techchallenge.domain.entities.Owner;

import java.util.List;
import java.util.UUID;

public interface OwnerRepository {
    Owner save(Owner owner);

    boolean existsByEmail(String email);

    List<Owner> findAll();

    Owner findById(UUID uuid);

    void delete(UUID id);

    boolean existsById(UUID id);
}
