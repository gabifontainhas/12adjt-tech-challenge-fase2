package br.com.gabifontainhas.techchallenge.application.usecase.menuitem;

import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;
import br.com.gabifontainhas.techchallenge.domain.entity.MenuItem;

import java.util.List;

public class ListMenuItemsUseCase {
    private final MenuItemRepository menuItemRepository;

    public ListMenuItemsUseCase(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

}
