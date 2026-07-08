package br.com.gabifontainhas.techchallenge.application.usecases.restaurant;

import br.com.gabifontainhas.techchallenge.application.exception.RestaurantAlreadyExistsException;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private CreateRestaurantUseCase createRestaurantUseCase;

    @Test
    @DisplayName("Should create restaurant successfully when request is valid, name is unique and owner exists")
    void shouldCreateRestaurantSuccessfully() {

        // Arrange
        var ownerId = UUID.randomUUID();

        var addressRequest = new AddressDTO.Request("Main Street", "123", "Downtown", "New York", "NY", "12345000");
        var addressDomain = addressRequest.toDomain();

        var request = new RestaurantDTO.PostRequest(
                "Holy Burger",
                addressRequest,
                "Fast Food",
                "08:00-22:00",
                ownerId
        );
        var expectedRestaurant = new Restaurant(
                "Holy Burger",
                addressDomain,
                "Fast Food",
                "08:00-22:00",
                ownerId
        );

        when(restaurantRepository.existsByName(request.name())).thenReturn(false);
        when(ownerRepository.existsById(ownerId)).thenReturn(true);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(expectedRestaurant);

        // Act
        var result = createRestaurantUseCase.create(request);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(request.name(), result.getName());
        assertEquals(request.cuisineType(), result.getCuisineType());
        assertEquals(request.operatingHours(), result.getOperatingHours());
        assertEquals(request.ownerId(), result.getOwnerId());

        assertEquals(request.address().street(), result.getAddress().street());
        assertEquals(request.address().number(), result.getAddress().number());
        assertEquals(request.address().neighborhood(), result.getAddress().neighborhood());
        assertEquals(request.address().city(), result.getAddress().city());
        assertEquals(request.address().state(), result.getAddress().state());
        assertEquals(request.address().zipCode(), result.getAddress().zipCode());


    }

    @Test
    @DisplayName("Should throw RestaurantAlreadyExistsException when restaurant name already exists")
    void shouldThrowExceptionWhenRestaurantNameAlreadyExists() {
        // Arrange
        var ownerId = UUID.randomUUID();

        var addressRequest = new AddressDTO.Request("Main Street", "123", "Downtown", "New York", "NY", "12345000");

        var request = new RestaurantDTO.PostRequest(
                "Holy Burger",
                addressRequest,
                "Fast Food",
                "08:00-22:00",
                ownerId
        );

        when(restaurantRepository.existsByName(request.name())).thenReturn(true);

        // Act & Assert
        var exception = assertThrows(
                RestaurantAlreadyExistsException.class,
                () -> createRestaurantUseCase.create(request)
        );

        assertEquals("Restaurant already exists", exception.getMessage());

        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }
}
