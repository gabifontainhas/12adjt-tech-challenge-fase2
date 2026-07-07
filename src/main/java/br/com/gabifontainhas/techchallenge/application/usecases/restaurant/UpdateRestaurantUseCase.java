package br.com.gabifontainhas.techchallenge.application.usecases.restaurant;

import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.application.usecases.dto.RestaurantDTO;
import br.com.gabifontainhas.techchallenge.domain.entities.Restaurant;
import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;

import java.util.UUID;

public class UpdateRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;
    private final OwnerRepository ownerRepository;


    public UpdateRestaurantUseCase(RestaurantRepository restaurantRepository, OwnerRepository ownerRepository) {
        this.restaurantRepository = restaurantRepository;
        this.ownerRepository = ownerRepository;
    }

    public Restaurant update(RestaurantDTO.PutRequest request, UUID id) {
        if (!ownerRepository.existsById(request.ownerId())) {
            throw new UserNotFoundException("Could not create restaurant: The provided Owner does not exist");
        }

        var restaurant = restaurantRepository.findById(id);
        var address = request.address().toDomain();
        restaurant.update(request.name(), address, request.cuisineType(), request.operatingHours(), request.ownerId());
        return restaurantRepository.save(restaurant);
    }
}
