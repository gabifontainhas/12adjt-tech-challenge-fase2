package br.com.gabifontainhas.techchallenge.application.usecase.restaurant;

import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.domain.entity.Restaurant;

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
