package br.com.gabifontainhas.techchallenge.application.usecases.restaurant;

import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.application.usecases.dto.AddressDTO;
import br.com.gabifontainhas.techchallenge.domain.entities.Restaurant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListRestaurantsUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private ListRestaurantsUseCase listRestaurantsUseCase;

    @Test
    @DisplayName("Should return a list of restaurants when restaurants exist in the database")
    void shouldReturnListOfRestaurants() {
        // Arrange
        var ownerId1 = UUID.randomUUID();
        var ownerId2 = UUID.randomUUID();

        var addressRequest = new AddressDTO.Request("Main Street", "123", "Downtown", "New York", "NY", "12345000");
        var addressDomain = addressRequest.toDomain();

        var restaurant1 = new Restaurant(
                "Holy Burger",
                addressDomain,
                "Fast Food",
                "08:00-22:00",
                ownerId1
        );

        var restaurant2 = new Restaurant(
                "Mario Pizza",
                addressDomain,
                "Italian Food",
                "18:00-23:00",
                ownerId2
        );

        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant1, restaurant2));

        // Act
        var result = listRestaurantsUseCase.getAllRestaurants();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Holy Burger", result.getFirst().getName());
        assertEquals("Fast Food", result.getFirst().getCuisineType());
        assertEquals("08:00-22:00", result.getFirst().getOperatingHours());
        assertEquals(ownerId1, result.getFirst().getOwnerId());
        assertNotNull(result.getFirst().getId());

        assertEquals("Mario Pizza", result.get(1).getName());
        assertEquals("Italian Food", result.get(1).getCuisineType());
        assertEquals("18:00-23:00", result.get(1).getOperatingHours());
        assertEquals(ownerId2, result.get(1).getOwnerId());
        assertNotNull(result.get(1).getId());

        assertEquals(addressRequest.street(), result.getFirst().getAddress().street());
        assertEquals(addressRequest.number(), result.getFirst().getAddress().number());
        assertEquals(addressRequest.neighborhood(), result.getFirst().getAddress().neighborhood());
        assertEquals(addressRequest.city(), result.getFirst().getAddress().city());
        assertEquals(addressRequest.state(), result.getFirst().getAddress().state());
        assertEquals(addressRequest.zipCode(), result.getFirst().getAddress().zipCode());

        assertEquals(addressRequest.street(), result.get(1).getAddress().street());
        assertEquals(addressRequest.number(), result.get(1).getAddress().number());
        assertEquals(addressRequest.neighborhood(), result.get(1).getAddress().neighborhood());
        assertEquals(addressRequest.city(), result.get(1).getAddress().city());
        assertEquals(addressRequest.state(), result.get(1).getAddress().state());
        assertEquals(addressRequest.zipCode(), result.get(1).getAddress().zipCode());
    }


    @Test
    @DisplayName("Should return an empty list when no restaurants are registered")
    void shouldReturnEmptyListWhenNoRestaurantsExist() {
        // Arrange
        when(restaurantRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        var result = listRestaurantsUseCase.getAllRestaurants();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return a restaurant by ID when the restaurant exists")
    void shouldReturnRestaurantById() {
        // Arrange

        var restaurantId = UUID.randomUUID();
        var ownerId = UUID.randomUUID();

        var addressRequest = new AddressDTO.Request("Main Street", "123", "Downtown", "New York", "NY", "12345000");
        var addressDomain = addressRequest.toDomain();

        var expectedRestaurant = new Restaurant(restaurantId,
                "Holy Burger",
                addressDomain,
                "Fast Food",
                "08:00-22:00",
                ownerId
        );

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(expectedRestaurant));

        // Act
        var result = listRestaurantsUseCase.getRestaurantById(restaurantId);

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
                () -> listRestaurantsUseCase.getRestaurantById(nonExistentId)
        );

        assertEquals("Restaurant not found", exception.getMessage());
    }
}
