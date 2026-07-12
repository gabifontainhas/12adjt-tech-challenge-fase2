package br.com.gabifontainhas.techchallenge.application.usecases.restaurant;

import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.domain.entities.Restaurant;
import br.com.gabifontainhas.techchallenge.domain.valueobjects.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
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

        var address = new Address("Main Street", "123", "Downtown", "New York", "NY", "12345000");

        var restaurant1 = new Restaurant(
                "Holy Burger",
                address,
                "Fast Food",
                "08:00-22:00",
                ownerId1
        );

        var restaurant2 = new Restaurant(
                "Mario Pizza",
                address,
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

        assertEquals(address.street(), result.getFirst().getAddress().street());
        assertEquals(address.number(), result.getFirst().getAddress().number());
        assertEquals(address.neighborhood(), result.getFirst().getAddress().neighborhood());
        assertEquals(address.city(), result.getFirst().getAddress().city());
        assertEquals(address.state(), result.getFirst().getAddress().state());
        assertEquals(address.zipCode(), result.getFirst().getAddress().zipCode());

        assertEquals(address.street(), result.get(1).getAddress().street());
        assertEquals(address.number(), result.get(1).getAddress().number());
        assertEquals(address.neighborhood(), result.get(1).getAddress().neighborhood());
        assertEquals(address.city(), result.get(1).getAddress().city());
        assertEquals(address.state(), result.get(1).getAddress().state());
        assertEquals(address.zipCode(), result.get(1).getAddress().zipCode());
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
}
