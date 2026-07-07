package br.com.gabifontainhas.techchallenge.infrastructure.gateways;

import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.domain.entities.Owner;
import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.OwnerJpaRepository;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.mapper.OwnerMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OwnerRepositoryAdapter implements OwnerRepository {

    private final OwnerJpaRepository ownerJpaRepository;

    public OwnerRepositoryAdapter(OwnerJpaRepository ownerJpaRepository) {
        this.ownerJpaRepository = ownerJpaRepository;
    }

    @Override
    public Owner save(Owner owner) {
        var savedEntity = ownerJpaRepository.save(OwnerMapper.toJpaEntity(owner));
        return OwnerMapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByEmail(String email) {
        return ownerJpaRepository.existsByEmail(email);
    }

    @Override
    public List<Owner> findAll() {
        return ownerJpaRepository.findAll().stream()
                .map(OwnerMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Owner findById(UUID id) {
        var ownerEntity = ownerJpaRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Owner not found"));
        return OwnerMapper.toDomain(ownerEntity);
    }

    @Override
    public void delete(UUID id) {
        this.ownerJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return this.ownerJpaRepository.existsById(id);
    }
}
