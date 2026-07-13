package br.com.gabifontainhas.techchallenge.infrastructure.gateway;

import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.domain.entity.Owner;
import br.com.gabifontainhas.techchallenge.infrastructure.persistence.OwnerJpaRepository;
import br.com.gabifontainhas.techchallenge.infrastructure.persistence.mapper.OwnerMapper;

import java.util.List;
import java.util.Optional;
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
    public Optional<Owner> findById(UUID id) {
        return ownerJpaRepository.findById(id).map(OwnerMapper::toDomain);
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
