package br.com.gabifontainhas.techchallenge.application.usecases.restaurant;

import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.domain.entities.Restaurant;

import java.util.List;

public class ListRestaurantsUseCase {
    private final RestaurantRepository restaurantRepository;

    public ListRestaurantsUseCase(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

}
