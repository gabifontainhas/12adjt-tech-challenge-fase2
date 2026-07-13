package br.com.gabifontainhas.techchallenge.infrastructure.persistence;

import br.com.gabifontainhas.techchallenge.infrastructure.persistence.entity.RestaurantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantJpaEntity, UUID> {

    boolean existsByName(String name);

    boolean existsByOwnerId(UUID ownerId);
}
