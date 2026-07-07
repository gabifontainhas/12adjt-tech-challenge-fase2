package br.com.gabifontainhas.techchallenge.application.usecases.menuitem;

import br.com.gabifontainhas.techchallenge.application.exception.RestaurantNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;

import java.util.UUID;

public class DeleteMenuItemUseCase {
    private final MenuItemRepository menuItemRepository;

    public DeleteMenuItemUseCase(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public void delete(UUID id) {
        if (!menuItemRepository.existsById(id)) {
            throw new RestaurantNotFoundException("Could not delete: MenuItem with ID " + id + " not found");
        }
        menuItemRepository.delete(id);
    }
}
