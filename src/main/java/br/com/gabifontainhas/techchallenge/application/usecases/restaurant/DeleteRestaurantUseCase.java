package br.com.gabifontainhas.techchallenge.application.usecases.restaurant;

import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.application.exception.RestaurantNotFoundException;

import java.util.UUID;

public class DeleteRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;

    public DeleteRestaurantUseCase(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public void delete(UUID id) {
        if (!restaurantRepository.existsById(id)) {
            throw new RestaurantNotFoundException("Could not delete: Restaurant with ID " + id + " not found");
        }
        restaurantRepository.delete(id);
    }
}
