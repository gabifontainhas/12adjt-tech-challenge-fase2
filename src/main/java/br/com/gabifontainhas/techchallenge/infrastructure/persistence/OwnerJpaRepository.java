package br.com.gabifontainhas.techchallenge.infrastructure.persistence;

import br.com.gabifontainhas.techchallenge.infrastructure.persistence.entity.OwnerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OwnerJpaRepository extends JpaRepository<OwnerJpaEntity, UUID> {
    boolean existsByEmail(String email);
}

