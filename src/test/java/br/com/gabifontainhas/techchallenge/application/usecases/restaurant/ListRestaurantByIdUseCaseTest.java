package br.com.gabifontainhas.techchallenge.application.usecases.restaurant;

import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.domain.entities.Restaurant;
import br.com.gabifontainhas.techchallenge.domain.valueobjects.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListRestaurantByIdUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private ListRestaurantByIdUseCase listRestaurantByIdUseCase;

    @Test
    @DisplayName("Should return a restaurant by ID when the restaurant exists")
    void shouldReturnRestaurantById() {
        // Arrange

        var restaurantId = UUID.randomUUID();
        var ownerId = UUID.randomUUID();

        var address = new Address("Main Street", "123", "Downtown", "New York", "NY", "12345000");

        var expectedRestaurant = new Restaurant(restaurantId,
                "Holy Burger",
                address,
                "Fast Food",
                "08:00-22:00",
                ownerId
        );

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(expectedRestaurant));

        // Act
        var result = listRestaurantByIdUseCase.getRestaurantById(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(restaurantId, result.getId());
        assertEquals("Holy Burger", result.getName());
        assertEquals("Fast Food", result.getCuisineType());
        assertEquals("08:00-22:00", result.getOperatingHours());
        assertEquals(ownerId, result.getOwnerId());

    }


    @Test
    @DisplayName("Should throw UserNotFoundException when restaurant by ID is not found")
    void shouldThrowExceptionWhenRestaurantByIdDoesNotExist() {
        // Arrange
        var nonExistentId = UUID.randomUUID();
        when(restaurantRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(
                UserNotFoundException.class,
                () -> listRestaurantByIdUseCase.getRestaurantById(nonExistentId)
        );

        assertEquals("Restaurant not found", exception.getMessage());
    }
}
