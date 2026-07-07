package br.com.gabifontainhas.techchallenge.config;

import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.application.usecases.menuitem.CreateMenuItemUseCase;
import br.com.gabifontainhas.techchallenge.application.usecases.menuitem.DeleteMenuItemUseCase;
import br.com.gabifontainhas.techchallenge.application.usecases.menuitem.ListMenuItemsUseCase;
import br.com.gabifontainhas.techchallenge.application.usecases.menuitem.UpdateMenuItemUseCase;
import br.com.gabifontainhas.techchallenge.infrastructure.gateways.MenuItemRepositoryAdapter;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.MenuItemJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MenuItemConfig {
    @Bean
    public CreateMenuItemUseCase createMenuItemUseCase(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        return new CreateMenuItemUseCase(restaurantRepository, menuItemRepository);
    }

    @Bean
    public ListMenuItemsUseCase listMenuItemsUseCase(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        return new ListMenuItemsUseCase(restaurantRepository, menuItemRepository);
    }

    @Bean
    public DeleteMenuItemUseCase deleteMenuItemUseCase(MenuItemRepository menuItemRepository) {
        return new DeleteMenuItemUseCase(menuItemRepository);
    }

    @Bean
    public UpdateMenuItemUseCase updateMenuItemUseCase(MenuItemRepository menuItemRepository) {
        return new UpdateMenuItemUseCase(menuItemRepository);
    }

    @Bean
    public MenuItemRepositoryAdapter createMenuItemRepositoryAdapter(MenuItemJpaRepository menuItemJpaRepository) {
        return new MenuItemRepositoryAdapter(menuItemJpaRepository);
    }
}
