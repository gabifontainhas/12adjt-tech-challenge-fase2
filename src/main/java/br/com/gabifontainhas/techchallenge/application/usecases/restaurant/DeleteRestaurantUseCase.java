package br.com.gabifontainhas.techchallenge.application.usecases.restaurant;

import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.application.exception.RestaurantNotFoundException;

import java.util.UUID;

public class DeleteRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public DeleteRestaurantUseCase(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public void delete(UUID id) {
        if (!restaurantRepository.existsById(id)) {
            throw new RestaurantNotFoundException("Could not delete: Restaurant with ID " + id + " not found");
        }
        menuItemRepository.deleteByRestaurantId(id);

        restaurantRepository.delete(id);
    }
}
