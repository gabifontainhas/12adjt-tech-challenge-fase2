package br.com.gabifontainhas.techchallenge.application.gateway;

import br.com.gabifontainhas.techchallenge.domain.entities.Restaurant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository {
    Restaurant save(Restaurant restaurant);

    List<Restaurant> findAll();

    Optional<Restaurant> findById(UUID uuid);

    void delete(UUID id);

    boolean existsById(UUID id);

    boolean existsByName(String name);

    boolean existsByOwnerId(UUID ownerId);
}
