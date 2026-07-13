package br.com.gabifontainhas.techchallenge.application.usecases.menuitem;

import br.com.gabifontainhas.techchallenge.application.exception.MenuItemNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;

import java.util.UUID;

public class DeleteMenuItemUseCase {
    private final MenuItemRepository menuItemRepository;

    public DeleteMenuItemUseCase(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public void delete(UUID id) {
        if (!menuItemRepository.existsById(id)) {
            throw new MenuItemNotFoundException("Could not delete: MenuItem with ID " + id + " not found");
        }
        menuItemRepository.delete(id);
    }
}
