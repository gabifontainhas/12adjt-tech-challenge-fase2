package br.com.gabifontainhas.techchallenge.application.usecases.restaurant;

import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.domain.entities.Restaurant;

import java.util.List;
import java.util.UUID;

public class ListRestaurantsUseCase {
    private final RestaurantRepository restaurantRepository;

    public ListRestaurantsUseCase(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantById(UUID uuid) {
        return restaurantRepository.findById(uuid).orElseThrow(() -> new UserNotFoundException("Restaurant not found"));
    }
}
