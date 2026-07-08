package br.com.gabifontainhas.techchallenge.application.usecases.menuitem;

import br.com.gabifontainhas.techchallenge.application.exception.MenuItemNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;
import br.com.gabifontainhas.techchallenge.application.usecases.dto.MenuItemDTO;
import br.com.gabifontainhas.techchallenge.domain.entities.MenuItem;

import java.util.UUID;

public class UpdateMenuItemUseCase {
    private final MenuItemRepository menuItemRepository;

    public UpdateMenuItemUseCase(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public MenuItem update(MenuItemDTO.PutRequest request, UUID id) {
        var menuItem = menuItemRepository.findById(id).orElseThrow(() -> new MenuItemNotFoundException("Menu item not found"));
        menuItem.update(request.name(), request.description(), request.price(), request.dineInOnly(), request.imagePath());
        return menuItemRepository.save(menuItem);
    }

}
