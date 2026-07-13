package br.com.gabifontainhas.techchallenge.application.usecase.menuitem;

import br.com.gabifontainhas.techchallenge.application.exception.MenuItemNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;
import br.com.gabifontainhas.techchallenge.application.usecase.dto.UpdateMenuItemCommand;
import br.com.gabifontainhas.techchallenge.domain.entity.MenuItem;

import java.util.UUID;

public class UpdateMenuItemUseCase {
    private final MenuItemRepository menuItemRepository;

    public UpdateMenuItemUseCase(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public MenuItem update(UpdateMenuItemCommand request, UUID id) {
        var menuItem = menuItemRepository.findById(id).orElseThrow(() -> new MenuItemNotFoundException("Menu item not found"));
        menuItem.update(request.name(), request.description(), request.price(), request.dineInOnly(), request.imagePath());
        return menuItemRepository.save(menuItem);
    }

}
