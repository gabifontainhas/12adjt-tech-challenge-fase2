package br.com.gabifontainhas.techchallenge.config;

import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.application.usecase.menuitem.*;
import br.com.gabifontainhas.techchallenge.infrastructure.gateway.MenuItemRepositoryAdapter;
import br.com.gabifontainhas.techchallenge.infrastructure.persistence.MenuItemJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MenuItemConfig {
    @Bean
    public CreateMenuItemUseCase createMenuItemUseCase(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        return new CreateMenuItemUseCase(restaurantRepository, menuItemRepository);
    }

    @Bean
    public ListMenuItemsUseCase listMenuItemsUseCase(MenuItemRepository menuItemRepository) {
        return new ListMenuItemsUseCase(menuItemRepository);
    }

    @Bean
    public ListMenuItemByIdUseCase listMenuItemByIdUseCase(MenuItemRepository menuItemRepository) {
        return new ListMenuItemByIdUseCase(menuItemRepository);
    }

    @Bean
    public ListMenuItemByRestaurantIdUseCase listMenuItemByRestaurantIdUseCase(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        return new ListMenuItemByRestaurantIdUseCase(restaurantRepository, menuItemRepository);
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
