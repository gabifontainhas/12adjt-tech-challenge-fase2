package br.com.gabifontainhas.techchallenge.application.usecases.menuitem;

import br.com.gabifontainhas.techchallenge.application.exception.MenuItemAlreadyExistsException;
import br.com.gabifontainhas.techchallenge.application.exception.RestaurantNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.application.usecases.dto.CreateMenuItemCommand;
import br.com.gabifontainhas.techchallenge.domain.entities.MenuItem;

public class CreateMenuItemUseCase {
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public CreateMenuItemUseCase(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public MenuItem create(CreateMenuItemCommand request) {
        if (!restaurantRepository.existsById(request.restaurantId())) {
            throw new RestaurantNotFoundException("Could not create menu item: The provided Restaurant does not exist");
        }
        if (menuItemRepository.existsByNameAndRestaurantId(request.name(), request.restaurantId())) {
            throw new MenuItemAlreadyExistsException("MenuItem already exists in the restaurant");
        }

        var menuItem = new MenuItem(request.name(), request.description(), request.price(), request.dineInOnly(), request.imagePath(), request.restaurantId());
        return menuItemRepository.save(menuItem);
    }
}
