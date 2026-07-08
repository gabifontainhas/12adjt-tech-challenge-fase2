package br.com.gabifontainhas.techchallenge.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemTest {

    @Test
    @DisplayName("Should instantiate menuItem correctly using the creation constructor")
    void shouldCreateMenuItemWithCreationConstructor() {
        // Arrange
        var restaurantId = UUID.randomUUID();

        // Act
        var menuItem = new MenuItem(
                "X-Burguer",
                "Delicious burger with cheese",
                BigDecimal.valueOf(25.90),
                false,
                "images/xburguer.png",
                restaurantId
        );

        // Assert
        assertNotNull(menuItem);
        assertNotNull(menuItem.getId());

        assertEquals("X-Burguer", menuItem.getName());
        assertEquals("Delicious burger with cheese", menuItem.getDescription());
        assertEquals(BigDecimal.valueOf(25.90), menuItem.getPrice());
        assertFalse(menuItem.isDineInOnly());
        assertEquals("images/xburguer.png", menuItem.getImagePath());
        assertEquals(restaurantId, menuItem.getRestaurantId());
    }

    @Test
    @DisplayName("Should instantiate menuItem correctly using the reconstruction constructor")
    void shouldCreateMenuItemWithReconstructionConstructor() {
        // Arrange
        var menuItemId = UUID.randomUUID();
        var restaurantId = UUID.randomUUID();

        // Act
        var menuItem = new MenuItem(
                menuItemId,
                "X-Burguer",
                "Delicious burger with cheese",
                BigDecimal.valueOf(25.90),
                false,
                "images/xburguer.png",
                restaurantId
        );

        // Assert
        assertNotNull(menuItem);
        assertEquals(menuItemId, menuItem.getId());

        assertEquals("X-Burguer", menuItem.getName());
        assertEquals("Delicious burger with cheese", menuItem.getDescription());
        assertEquals(BigDecimal.valueOf(25.90), menuItem.getPrice());
        assertFalse(menuItem.isDineInOnly());
        assertEquals("images/xburguer.png", menuItem.getImagePath());
        assertEquals(restaurantId, menuItem.getRestaurantId());
    }

    @Test
    @DisplayName("Should update menu item details successfully")
    void shouldUpdateMenuItemDetailsSuccessfully() {
        // Arrange
        var menuItemId = UUID.randomUUID();
        var restaurantId = UUID.randomUUID();

        var menuItem = new MenuItem(
                menuItemId,
                "X-Burguer",
                "Delicious burger with cheese",
                BigDecimal.valueOf(25.90),
                false,
                "images/xburguer.png",
                restaurantId
        );

        // Act
        menuItem.update(
                "Chocolate Milkshake",
                "Delicious milkshake with chocolate syrup",
                BigDecimal.valueOf(14.90),
                true,
                "images/milkshake.png"
        );

        // Assert
        assertEquals(menuItemId, menuItem.getId());

        assertEquals("Chocolate Milkshake", menuItem.getName());
        assertEquals("Delicious milkshake with chocolate syrup", menuItem.getDescription());
        assertEquals(BigDecimal.valueOf(14.90), menuItem.getPrice());
        assertTrue(menuItem.isDineInOnly());
        assertEquals("images/milkshake.png", menuItem.getImagePath());

        assertEquals(restaurantId, menuItem.getRestaurantId());
    }
}