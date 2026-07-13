package br.com.gabifontainhas.techchallenge.domain.entity;

import br.com.gabifontainhas.techchallenge.domain.valueobject.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    @Test
    @DisplayName("Should instantiate restaurant correctly using the creation constructor")
    void shouldCreateRestaurantWithCreationConstructor() {
        // Arrange
        var address = new Address("Main Street", "123", "Downtown", "New York", "NY", "12345000");
        var ownerId = UUID.randomUUID();

        // Act
        var restaurant = new Restaurant("Holy Burger", address, "Fast Food", "08:00-22:00", ownerId);

        // Assert
        assertNotNull(restaurant);
        assertNotNull(restaurant.getId());
        assertEquals("Holy Burger", restaurant.getName());
        assertEquals(address, restaurant.getAddress());
        assertEquals("Fast Food", restaurant.getCuisineType());
        assertEquals("08:00-22:00", restaurant.getOperatingHours());
        assertEquals(ownerId, restaurant.getOwnerId());
    }

    @Test
    @DisplayName("Should instantiate restaurant correctly using the reconstruction constructor")
    void shouldCreateRestaurantWithReconstructionConstructor() {
        // Arrange
        var address = new Address("Main Street", "123", "Downtown", "New York", "NY", "12345000");
        var ownerId = UUID.randomUUID();
        var restaurantId = UUID.randomUUID();

        // Act
        var restaurant = new Restaurant(restaurantId, "Holy Burger", address, "Fast Food", "08:00-22:00", ownerId);

        // Assert
        assertNotNull(restaurant);
        assertEquals(restaurantId, restaurant.getId());
        assertEquals("Holy Burger", restaurant.getName());
        assertEquals(address, restaurant.getAddress());
        assertEquals("Fast Food", restaurant.getCuisineType());
        assertEquals("08:00-22:00", restaurant.getOperatingHours());
        assertEquals(ownerId, restaurant.getOwnerId());
    }

    @Test
    @DisplayName("Should update restaurant details successfully")
    void shouldUpdateRestaurantDetailsSuccessfully() {
        // Arrange
        var restaurantId = UUID.randomUUID();
        var oldAddress = new Address("Old Street", "987", "Old District", "Old City", "NY", "00000999");
        var oldOwnerId = UUID.randomUUID();
        var restaurant = new Restaurant(restaurantId, "Old Restaurant", oldAddress, "Fast Food", "10:00-22:00", oldOwnerId);

        var newAddress = new Address("Main Street", "123", "Downtown", "New York", "NY", "12345000");
        var newOwnerId = UUID.randomUUID();

        // Act
        restaurant.update("Holy Burger", newAddress, "Fast Food", "08:00-22:00", newOwnerId);

        // Assert
        assertEquals("Holy Burger", restaurant.getName());
        assertEquals(newAddress, restaurant.getAddress());
        assertEquals("Fast Food", restaurant.getCuisineType());
        assertEquals("08:00-22:00", restaurant.getOperatingHours());
        assertEquals(newOwnerId, restaurant.getOwnerId());

    }
}