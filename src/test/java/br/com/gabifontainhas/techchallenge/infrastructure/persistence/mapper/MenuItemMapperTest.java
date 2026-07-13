package br.com.gabifontainhas.techchallenge.infrastructure.persistence.mapper;

import br.com.gabifontainhas.techchallenge.domain.entity.MenuItem;
import br.com.gabifontainhas.techchallenge.infrastructure.persistence.entity.MenuItemJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemMapperTest {

    @Test
    @DisplayName("Should return null when mapping null MenuItem domain to JpaEntity")
    void shouldReturnNullWhenDomainIsNull() {
        assertNull(MenuItemMapper.toJpaEntity(null));
    }

    @Test
    @DisplayName("Should return null when mapping null MenuItemJpaEntity to domain")
    void shouldReturnNullWhenEntityIsNull() {
        assertNull(MenuItemMapper.toDomain(null));
    }

    @Test
    @DisplayName("Should map MenuItem domain to JpaEntity correctly with all attributes")
    void shouldMapDomainToJpaEntitySuccessfully() {
        // Arrange
        var menuItemId = UUID.randomUUID();
        var restaurantId = UUID.randomUUID();

        var domain = new MenuItem(
                menuItemId,
                "Cheese Burger",
                "Delicious burger with cheese",
                BigDecimal.valueOf(25.90),
                false,
                "images/cheeseburger.png",
                restaurantId
        );

        // Act
        var entity = MenuItemMapper.toJpaEntity(domain);

        // Assert
        assertNotNull(entity);
        assertEquals(menuItemId, entity.getId());
        assertEquals("Cheese Burger", entity.getName());
        assertEquals("Delicious burger with cheese", entity.getDescription());
        assertEquals(BigDecimal.valueOf(25.90), entity.getPrice());
        assertFalse(entity.isDineInOnly());
        assertEquals("images/cheeseburger.png", entity.getImagePath());
        assertEquals(restaurantId, entity.getRestaurantId());
    }
    @Test
    @DisplayName("Should map MenuItemJpaEntity to domain correctly with all attributes")
    void shouldMapJpaEntityToDomainSuccessfully() {
        // Arrange
        var menuItemId = UUID.randomUUID();
        var restaurantId = UUID.randomUUID();

        var entity = new MenuItemJpaEntity(
                menuItemId,
                "Cheese Burger",
                "Delicious burger with cheese",
                BigDecimal.valueOf(25.90),
                true,
                "images/cheeseburger.png",
                restaurantId
        );

        // Act
        MenuItem domain = MenuItemMapper.toDomain(entity);

        // Assert
        assertNotNull(domain);
        assertEquals(menuItemId, domain.getId());
        assertEquals("Cheese Burger", domain.getName());
        assertEquals("Delicious burger with cheese", domain.getDescription());
        assertEquals(BigDecimal.valueOf(25.90), domain.getPrice());
        assertTrue(domain.isDineInOnly());
        assertEquals("images/cheeseburger.png", domain.getImagePath());
        assertEquals(restaurantId, domain.getRestaurantId());
    }
}