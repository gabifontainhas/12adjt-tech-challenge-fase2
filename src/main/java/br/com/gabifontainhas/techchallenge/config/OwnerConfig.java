package br.com.gabifontainhas.techchallenge.config;

import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.application.usecase.owner.*;
import br.com.gabifontainhas.techchallenge.infrastructure.gateway.OwnerRepositoryAdapter;
import br.com.gabifontainhas.techchallenge.infrastructure.persistence.OwnerJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OwnerConfig {
    @Bean
    public CreateOwnerUseCase createOwnerUseCase(OwnerRepository ownerRepository) {
        return new CreateOwnerUseCase(ownerRepository);
    }

    @Bean
    public ListOwnersUseCase listOwnersUseCase(OwnerRepository ownerRepository) {
        return new ListOwnersUseCase(ownerRepository);
    }

    @Bean
    public ListOwnerByIdUseCase listOwnerByIdUseCase(OwnerRepository ownerRepository) {
        return new ListOwnerByIdUseCase(ownerRepository);
    }

    @Bean
    public DeleteOwnerUseCase deleteOwnerUseCase(OwnerRepository ownerRepository, RestaurantRepository restaurantRepository) {
        return new DeleteOwnerUseCase(ownerRepository, restaurantRepository);
    }

    @Bean
    public UpdateOwnerUseCase updateOwnerUseCase(OwnerRepository ownerRepository) {
        return new UpdateOwnerUseCase(ownerRepository);
    }

    @Bean
    public OwnerRepositoryAdapter createOwnerRepositoryAdapter(OwnerJpaRepository ownerJpaRepository) {
        return new OwnerRepositoryAdapter(ownerJpaRepository);
    }
}

