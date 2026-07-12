package br.com.gabifontainhas.techchallenge.config;

import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.application.usecases.restaurant.*;
import br.com.gabifontainhas.techchallenge.infrastructure.gateways.RestaurantRepositoryAdapter;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.RestaurantJpaRepository;
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
    public DeleteRestaurantUseCase deleteRestaurantUseCase(RestaurantRepository restaurantRepository) {
        return new DeleteRestaurantUseCase(restaurantRepository);
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
