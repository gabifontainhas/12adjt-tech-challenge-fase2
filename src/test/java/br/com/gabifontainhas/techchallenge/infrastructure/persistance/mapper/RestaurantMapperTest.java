package br.com.gabifontainhas.techchallenge.infrastructure.persistance.mapper;

import br.com.gabifontainhas.techchallenge.domain.entities.Restaurant;
import br.com.gabifontainhas.techchallenge.domain.valueobjects.Address;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.entity.AddressEmbeddable;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.entity.RestaurantJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantMapperTest {
    @Test
    @DisplayName("Should return null when mapping null Restaurant domain to JpaEntity")
    void shouldReturnNullWhenDomainIsNull() {
        assertNull(RestaurantMapper.toJpaEntity(null));
    }

    @Test
    @DisplayName("Should return null when mapping null RestaurantJpaEntity to domain")
    void shouldReturnNullWhenEntityIsNull() {
        assertNull(RestaurantMapper.toDomain(null));
    }

    @Test
    @DisplayName("Should map Restaurant domain to JpaEntity correctly with all attributes and address")
    void shouldMapDomainToJpaEntitySuccessfully() {
        // Arrange
        var restaurantId = UUID.randomUUID();
        var ownerId = UUID.randomUUID();
        var address = new Address("Main Street", "123", "Downtown", "New York", "NY", "12345000");

        var domain = new Restaurant(
                restaurantId,
                "Holy Burger",
                address,
                "Fast Food",
                "08:00-22:00",
                ownerId
        );

        // Act
        var entity = RestaurantMapper.toJpaEntity(domain);

        // Assert
        assertNotNull(entity);
        assertEquals(restaurantId, entity.getId());
        assertEquals("Holy Burger", entity.getName());
        assertEquals("Fast Food", entity.getCuisineType());
        assertEquals("08:00-22:00", entity.getOperatingHours());
        assertEquals(ownerId, entity.getOwnerId());

        assertNotNull(entity.getAddress());
        assertEquals("Main Street", entity.getAddress().getStreet());
        assertEquals("123", entity.getAddress().getNumber());
        assertEquals("Downtown", entity.getAddress().getNeighborhood());
        assertEquals("New York", entity.getAddress().getCity());
        assertEquals("NY", entity.getAddress().getState());
        assertEquals("12345000", entity.getAddress().getZipCode());
    }

    @Test
    @DisplayName("Should map Restaurant domain to JpaEntity with null Address")
    void shouldMapDomainToJpaEntityWithNullAddress() {
        // Arrange
        UUID restaurantId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        var domain = new Restaurant(
                restaurantId,
                "Holy Burger",
                null,
                "Fast Food",
                "08:00-22:00",
                ownerId
        );

        // Act
        var entity = RestaurantMapper.toJpaEntity(domain);

        // Assert
        assertNotNull(entity);
        assertNull(entity.getAddress());
    }


    @Test
    @DisplayName("Should map RestaurantJpaEntity to domain correctly with all attributes and address")
    void shouldMapJpaEntityToDomainSuccessfully() {
        // Arrange
        var restaurantId = UUID.randomUUID();
        var ownerId = UUID.randomUUID();
        var addressEmbeddable = new AddressEmbeddable("Main Street", "123", "Downtown", "New York", "NY", "12345000");

        var entity = new RestaurantJpaEntity(
                restaurantId,
                "Holy Burger",
                "Fast Food",
                "08:00-22:00",
                ownerId,
                addressEmbeddable
        );

        //Act
        var domain = RestaurantMapper.toDomain(entity);

        // Assert
        assertNotNull(domain);
        assertEquals(restaurantId, domain.getId());
        assertEquals("Holy Burger", domain.getName());
        assertEquals("Fast Food", domain.getCuisineType());
        assertEquals("08:00-22:00", domain.getOperatingHours());
        assertEquals(ownerId, domain.getOwnerId());

        assertNotNull(domain.getAddress());
        assertEquals("Main Street", domain.getAddress().street());
        assertEquals("123", domain.getAddress().number());
        assertEquals("Downtown", domain.getAddress().neighborhood());
        assertEquals("New York", domain.getAddress().city());
        assertEquals("NY", domain.getAddress().state());
        assertEquals("12345000", domain.getAddress().zipCode());
    }

    @Test
    @DisplayName("Should map RestaurantJpaEntity to domain with null Address")
    void shouldMapJpaEntityToDomainWithNullAddress() {
        // Arrange
        UUID restaurantId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        var entity = new RestaurantJpaEntity(
                restaurantId,
                "Holy Burger",
                "Fast Food",
                "08:00-22:00",
                ownerId,
                null
        );

        // Act
        var domain = RestaurantMapper.toDomain(entity);

        // Assert
        assertNotNull(domain);
        assertNull(domain.getAddress());
    }
}