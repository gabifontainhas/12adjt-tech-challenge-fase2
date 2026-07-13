package br.com.gabifontainhas.techchallenge.infrastructure.persistence;

import br.com.gabifontainhas.techchallenge.infrastructure.persistence.entity.CustomerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerJpaRepository extends JpaRepository<CustomerJpaEntity, UUID> {
    boolean existsByEmail(String email);
}
