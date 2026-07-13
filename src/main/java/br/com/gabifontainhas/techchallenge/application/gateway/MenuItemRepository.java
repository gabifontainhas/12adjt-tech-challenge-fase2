package br.com.gabifontainhas.techchallenge.application.gateway;

import br.com.gabifontainhas.techchallenge.domain.entity.MenuItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuItemRepository {
    MenuItem save(MenuItem menuItem);

    List<MenuItem> findAll();

    Optional<MenuItem> findById(UUID id);

    void delete(UUID id);

    boolean existsById(UUID id);

    boolean existsByNameAndRestaurantId(String name, UUID id);

    List<MenuItem> findByRestaurantId(UUID id);

    void deleteByRestaurantId(UUID restaurantId);
}
