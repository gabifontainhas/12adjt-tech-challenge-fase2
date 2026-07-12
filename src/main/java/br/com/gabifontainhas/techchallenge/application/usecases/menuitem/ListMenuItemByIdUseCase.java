package br.com.gabifontainhas.techchallenge.application.usecases.menuitem;

import br.com.gabifontainhas.techchallenge.application.exception.MenuItemNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;
import br.com.gabifontainhas.techchallenge.domain.entities.MenuItem;

import java.util.UUID;

public class ListMenuItemByIdUseCase {
    private final MenuItemRepository menuItemRepository;

    public ListMenuItemByIdUseCase(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public MenuItem getMenuItemById(UUID uuid) {
        return menuItemRepository.findById(uuid).orElseThrow(() -> new MenuItemNotFoundException("Menu item not found"));
    }

}
