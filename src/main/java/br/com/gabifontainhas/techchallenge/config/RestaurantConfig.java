package br.com.gabifontainhas.techchallenge.config;

import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;
import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.application.usecase.restaurant.*;
import br.com.gabifontainhas.techchallenge.infrastructure.gateway.RestaurantRepositoryAdapter;
import br.com.gabifontainhas.techchallenge.infrastructure.persistence.RestaurantJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantConfig {
    @Bean
    public CreateRestaurantUseCase createRestaurantUseCase(RestaurantRepository restaurantRepository, OwnerRepository ownerRepository) {
        return new CreateRestaurantUseCase(restaurantRepository, ownerRepository);
    }

    @Bean
    public ListRestaurantsUseCase listRestaurantsUseCase(RestaurantRepository restaurantRepository) {
        return new ListRestaurantsUseCase(restaurantRepository);
    }

    @Bean
    public ListRestaurantByIdUseCase listRestaurantByIdUseCase(RestaurantRepository restaurantRepository) {
        return new ListRestaurantByIdUseCase(restaurantRepository);
    }

    @Bean
    public DeleteRestaurantUseCase deleteRestaurantUseCase(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        return new DeleteRestaurantUseCase(restaurantRepository, menuItemRepository);
    }

    @Bean
    public UpdateRestaurantUseCase updateRestaurantUseCase(RestaurantRepository restaurantRepository, OwnerRepository ownerRepository) {
        return new UpdateRestaurantUseCase(restaurantRepository, ownerRepository);
    }

    @Bean
    public RestaurantRepositoryAdapter createRestaurantRepositoryAdapter(RestaurantJpaRepository restaurantJpaRepository) {
        return new RestaurantRepositoryAdapter(restaurantJpaRepository);
    }
}
