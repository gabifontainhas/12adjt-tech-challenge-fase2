package br.com.gabifontainhas.techchallenge.application.usecases.restaurant;

import br.com.gabifontainhas.techchallenge.application.exception.RestaurantNotFoundException;
import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.application.usecases.dto.AddressDTO;
import br.com.gabifontainhas.techchallenge.application.usecases.dto.RestaurantDTO;
import br.com.gabifontainhas.techchallenge.domain.entities.Restaurant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private UpdateRestaurantUseCase updateRestaurantUseCase;

    @Test
    @DisplayName("Should update restaurant details successfully when owner and restaurant exist and request is valid")
    void shouldUpdateRestaurantSuccessfully() {
        // Arrange
        var ownerId = UUID.randomUUID();
        var restaurantId = UUID.randomUUID();

        var addressOld = new AddressDTO.Request("Main Street", "123", "Downtown", "New York", "NY", "12345000");
        var addressOldDomain = addressOld.toDomain();

        var oldRestaurant = new Restaurant(restaurantId,
                "Mario Pizza",
                addressOldDomain,
                "Italian Food",
                "18:00-23:00",
                ownerId
        );

        var addressRequest = new AddressDTO.Request("5th Street", "987", "Center", "New Jersey", "NY", "12345999");
        var addressDomain = addressRequest.toDomain();

        var putRequest = new RestaurantDTO.PutRequest(
                "Holy Burger",
                addressRequest,
                "Fast Food",
                "08:00-22:00",
                ownerId
        );
        var updatedRestaurant = new Restaurant(restaurantId,
                "Holy Burger",
                addressDomain,
                "Fast Food",
                "08:00-22:00",
                ownerId
        );

        when(ownerRepository.existsById(ownerId)).thenReturn(true);
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(oldRestaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(updatedRestaurant);

        // Act
        var result = updateRestaurantUseCase.update(putRequest, restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(restaurantId, result.getId());
        assertEquals("Holy Burger", result.getName());
        assertEquals("Fast Food", result.getCuisineType());
        assertEquals("08:00-22:00", result.getOperatingHours());
        assertEquals(ownerId, result.getOwnerId());

        assertEquals(addressRequest.street(), result.getAddress().street());
        assertEquals(addressRequest.number(), result.getAddress().number());
        assertEquals(addressRequest.neighborhood(), result.getAddress().neighborhood());
        assertEquals(addressRequest.city(), result.getAddress().city());
        assertEquals(addressRequest.state(), result.getAddress().state());
        assertEquals(addressRequest.zipCode(), result.getAddress().zipCode());
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when the provided owner does not exist")
    void shouldThrowExceptionWhenOwnerDoesNotExist() {
        // Arrange

        var invalidOwnerId = UUID.randomUUID();
        var restaurantId = UUID.randomUUID();

        var addressRequest = new AddressDTO.Request("5th Street", "987", "Center", "New Jersey", "NY", "12345999");

        var putRequest = new RestaurantDTO.PutRequest(
                "Holy Burger",
                addressRequest,
                "Fast Food",
                "08:00-22:00",
                invalidOwnerId
        );

        when(ownerRepository.existsById(invalidOwnerId)).thenReturn(false);

        // Act & Assert - Executando e validando se lança a exceção esperada
        var exception = assertThrows(
                UserNotFoundException.class,
                () -> updateRestaurantUseCase.update(putRequest, restaurantId)
        );

        assertEquals("Could not update restaurant: The provided Owner does not exist", exception.getMessage());

        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("Should throw RestaurantNotFoundException when restaurant to update is not found")
    void shouldThrowExceptionWhenRestaurantDoesNotExist() {
        // Arrange

        var nonExistentRestaurantId = UUID.randomUUID();
        var ownerId = UUID.randomUUID();

        var addressRequest = new AddressDTO.Request("5th Street", "987", "Center", "New Jersey", "NY", "12345999");

        var putRequest = new RestaurantDTO.PutRequest(
                "Holy Burger",
                addressRequest,
                "Fast Food",
                "08:00-22:00",
                ownerId
        );

        when(ownerRepository.existsById(ownerId)).thenReturn(true);
        when(restaurantRepository.findById(nonExistentRestaurantId)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(
                RestaurantNotFoundException.class,
                () -> updateRestaurantUseCase.update(putRequest, nonExistentRestaurantId)
        );

        assertEquals("Restaurant not found", exception.getMessage());

        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }
}
