package br.com.gabifontainhas.techchallenge.application.usecase.restaurant;

import br.com.gabifontainhas.techchallenge.application.exception.RestaurantNotFoundException;
import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.application.usecase.dto.UpdateRestaurantCommand;
import br.com.gabifontainhas.techchallenge.domain.entity.Restaurant;

import java.util.UUID;

public class UpdateRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;
    private final OwnerRepository ownerRepository;


    public UpdateRestaurantUseCase(RestaurantRepository restaurantRepository, OwnerRepository ownerRepository) {
        this.restaurantRepository = restaurantRepository;
        this.ownerRepository = ownerRepository;
    }

    public Restaurant update(UpdateRestaurantCommand request, UUID id) {
        if (!ownerRepository.existsById(request.ownerId())) {
            throw new UserNotFoundException("Could not update restaurant: The provided Owner does not exist");
        }

        var restaurant = restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));
        restaurant.update(request.name(), request.address(), request.cuisineType(), request.operatingHours(), request.ownerId());
        return restaurantRepository.save(restaurant);
    }
}
