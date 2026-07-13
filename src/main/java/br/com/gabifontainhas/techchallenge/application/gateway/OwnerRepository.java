package br.com.gabifontainhas.techchallenge.application.gateway;

import br.com.gabifontainhas.techchallenge.domain.entity.Owner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OwnerRepository {
    Owner save(Owner owner);

    boolean existsByEmail(String email);

    List<Owner> findAll();

    Optional<Owner> findById(UUID uuid);

    void delete(UUID id);

    boolean existsById(UUID id);
}
