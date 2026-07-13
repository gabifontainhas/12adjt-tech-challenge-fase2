package br.com.gabifontainhas.techchallenge.application.usecase.restaurant;

import br.com.gabifontainhas.techchallenge.application.exception.RestaurantAlreadyExistsException;
import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.application.usecase.dto.CreateRestaurantCommand;
import br.com.gabifontainhas.techchallenge.domain.entity.Restaurant;

public class CreateRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;
    private final OwnerRepository ownerRepository;

    public CreateRestaurantUseCase(RestaurantRepository restaurantRepository, OwnerRepository ownerRepository) {
        this.restaurantRepository = restaurantRepository;
        this.ownerRepository = ownerRepository;
    }

    public Restaurant create(CreateRestaurantCommand request) {
        if (restaurantRepository.existsByName(request.name())) {
            throw new RestaurantAlreadyExistsException("Restaurant already exists");
        }

        if (!ownerRepository.existsById(request.ownerId())) {
            throw new UserNotFoundException("Could not create restaurant: The provided Owner does not exist");
        }

        var restaurant = new Restaurant(request.name(), request.address(), request.cuisineType(), request.operatingHours(), request.ownerId());
        return restaurantRepository.save(restaurant);
    }
}
