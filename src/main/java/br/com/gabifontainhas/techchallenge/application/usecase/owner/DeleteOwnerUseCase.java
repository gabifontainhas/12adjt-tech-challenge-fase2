package br.com.gabifontainhas.techchallenge.application.usecase.owner;

import br.com.gabifontainhas.techchallenge.application.exception.CannotDeleteOwnerIfHasRestaurant;
import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;

import java.util.UUID;

public class DeleteOwnerUseCase {
    private final OwnerRepository ownerRepository;
    private final RestaurantRepository restaurantRepository;

    public DeleteOwnerUseCase(OwnerRepository ownerRepository, RestaurantRepository restaurantRepository) {
        this.ownerRepository = ownerRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public void delete(UUID id) {
        if (!ownerRepository.existsById(id)) {
            throw new UserNotFoundException("Could not delete: Owner with ID " + id + " not found");
        }

        if (restaurantRepository.existsByOwnerId(id)) {
            throw new CannotDeleteOwnerIfHasRestaurant("Could not delete owner. There are restaurants associated with this owner.");
        }

        ownerRepository.delete(id);
    }
}
