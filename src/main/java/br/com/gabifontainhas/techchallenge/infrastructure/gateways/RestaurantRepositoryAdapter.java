package br.com.gabifontainhas.techchallenge.infrastructure.gateways;

import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.domain.entities.Restaurant;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.RestaurantJpaRepository;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.mapper.RestaurantMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class RestaurantRepositoryAdapter implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;

    public RestaurantRepositoryAdapter(RestaurantJpaRepository restaurantJpaRepository) {
        this.restaurantJpaRepository = restaurantJpaRepository;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        var savedEntity = restaurantJpaRepository.save(RestaurantMapper.toJpaEntity(restaurant));
        return RestaurantMapper.toDomain(savedEntity);
    }

    @Override
    public List<Restaurant> findAll() {
        return restaurantJpaRepository.findAll().stream()
                .map(RestaurantMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Restaurant> findById(UUID id) {
        return restaurantJpaRepository.findById(id).map(RestaurantMapper::toDomain);
    }

    @Override
    public void delete(UUID id) {
        this.restaurantJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return this.restaurantJpaRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return this.restaurantJpaRepository.existsByName(name);
    }

    @Override
    public boolean existsByOwnerId(UUID ownerId) { return this.restaurantJpaRepository.existsByOwnerId(ownerId);   }
}
