package br.com.gabifontainhas.techchallenge.application.usecases.menuitem;

import br.com.gabifontainhas.techchallenge.application.exception.RestaurantNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.domain.entities.MenuItem;

import java.util.List;
import java.util.UUID;

public class ListMenuItemByRestaurantIdUseCase {
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public ListMenuItemByRestaurantIdUseCase(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> getAllMenuItemsByRestaurant(UUID restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new RestaurantNotFoundException("Could not list menu items: The provided Restaurant does not exist");
        }
        return menuItemRepository.findByRestaurantId(restaurantId);
    }
}
