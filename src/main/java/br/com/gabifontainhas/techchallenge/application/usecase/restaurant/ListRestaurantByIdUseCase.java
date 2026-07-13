package br.com.gabifontainhas.techchallenge.application.usecase.restaurant;

import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.domain.entity.Restaurant;

import java.util.UUID;

public class ListRestaurantByIdUseCase {
    private final RestaurantRepository restaurantRepository;

    public ListRestaurantByIdUseCase(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant getRestaurantById(UUID uuid) {
        return restaurantRepository.findById(uuid).orElseThrow(() -> new UserNotFoundException("Restaurant not found"));
    }
}
