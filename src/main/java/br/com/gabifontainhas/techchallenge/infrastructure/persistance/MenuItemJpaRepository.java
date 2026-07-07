package br.com.gabifontainhas.techchallenge.infrastructure.persistance;

import br.com.gabifontainhas.techchallenge.infrastructure.persistance.entity.MenuItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MenuItemJpaRepository extends JpaRepository<MenuItemJpaEntity, UUID> {
    List<MenuItemJpaEntity> findByRestaurantId(UUID restaurantId);
    boolean existsByNameAndRestaurantId(String name, UUID restaurantId);
}
