package br.com.gabifontainhas.techchallenge.application.usecases.menuitem;

import br.com.gabifontainhas.techchallenge.application.exception.MenuItemNotFoundException;
import br.com.gabifontainhas.techchallenge.application.exception.RestaurantNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.domain.entities.MenuItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListMenuItemsUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private ListMenuItemsUseCase listMenuItemsUseCase;

    @Test
    @DisplayName("Should return a list of all menu items in the system")
    void shouldReturnAllMenuItems() {

        // Arrange
        var restaurantId = UUID.randomUUID();

        var menuItem1 =  new MenuItem(
                "X-Burger",
                "Delicious burger with cheese",
                BigDecimal.valueOf(25.90),
                false,
                "images/xburger.png",
                restaurantId
        );

        var menuItem2 =  new MenuItem(
                "Chocolate Milkshake",
                "Delicious milkshake with chocolate syrup",
                BigDecimal.valueOf(14.90),
                true,
                "images/milkshake.png",
                restaurantId
        );

        when(menuItemRepository.findAll()).thenReturn(List.of(menuItem1, menuItem2));

        // Act
        var result = listMenuItemsUseCase.getAllMenuItems();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertNotNull(result.getFirst().getId());
        assertEquals("X-Burger", result.getFirst().getName());
        assertEquals("Delicious burger with cheese", result.getFirst().getDescription());
        assertEquals(BigDecimal.valueOf(25.90), result.getFirst().getPrice());
        assertFalse(result.getFirst().isDineInOnly());
        assertEquals("images/xburger.png", result.getFirst().getImagePath());
        assertEquals(restaurantId, result.getFirst().getRestaurantId());

        assertNotNull(result.get(1).getId());
        assertEquals("Chocolate Milkshake", result.get(1).getName());
        assertEquals("Delicious milkshake with chocolate syrup", result.get(1).getDescription());
        assertEquals(BigDecimal.valueOf(14.90), result.get(1).getPrice());
        assertTrue(result.get(1).isDineInOnly());
        assertEquals("images/milkshake.png", result.get(1).getImagePath());
        assertEquals(restaurantId, result.get(1).getRestaurantId());


    }


    @Test
    @DisplayName("Should return an empty list when there are no menu items registered in the system")
    void shouldReturnEmptyListWhenNoMenuItemsExist() {
        // Arrange
        when(menuItemRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        var result = listMenuItemsUseCase.getAllMenuItems();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    @DisplayName("Should return menu items of a specific restaurant successfully when restaurant exists")
    void shouldReturnMenuItemsByRestaurantSuccessfully() {
        // Arrange

        var restaurantId = UUID.randomUUID();

        var menuItem =  new MenuItem(
                "X-Burger",
                "Delicious burger with cheese",
                BigDecimal.valueOf(25.90),
                false,
                "images/xburger.png",
                restaurantId
        );

        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);
        when(menuItemRepository.findByRestaurantId(restaurantId)).thenReturn(List.of(menuItem));

        // Act
        var result = listMenuItemsUseCase.getAllMenuItemsByRestaurant(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        assertNotNull(result.getFirst().getId());
        assertEquals("X-Burger", result.getFirst().getName());
        assertEquals("Delicious burger with cheese", result.getFirst().getDescription());
        assertEquals(BigDecimal.valueOf(25.90), result.getFirst().getPrice());
        assertFalse(result.getFirst().isDineInOnly());
        assertEquals("images/xburger.png", result.getFirst().getImagePath());
        assertEquals(restaurantId, result.getFirst().getRestaurantId());

    }


    @Test
    @DisplayName("Should throw RestaurantNotFoundException when trying to list items of a non-existent restaurant")
    void shouldThrowExceptionWhenRestaurantDoesNotExist() {
        // Arrange
        var nonExistentId = UUID.randomUUID();
        when(restaurantRepository.existsById(nonExistentId)).thenReturn(false);

        // Act & Assert
        var exception = assertThrows(
                RestaurantNotFoundException.class,
                () -> listMenuItemsUseCase.getAllMenuItemsByRestaurant(nonExistentId)
        );

        assertEquals("Could not list menu items: The provided Restaurant does not exist", exception.getMessage());
        verify(menuItemRepository, never()).findByRestaurantId(any(UUID.class));
    }


    @Test
    @DisplayName("Should return a specific menu item by ID when it exists")
    void shouldReturnMenuItemById() {
        // Arrange
        var menuItemId = UUID.randomUUID();
        var restaurantId = UUID.randomUUID();
        var expectedItem =  new MenuItem(
                "X-Burger",
                "Delicious burger with cheese",
                BigDecimal.valueOf(25.90),
                false,
                "images/xburger.png",
                restaurantId
        );

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(expectedItem));

        // Act
        var result = listMenuItemsUseCase.getMenuItemById(menuItemId);

        // Assert
        assertNotNull(result);

        assertNotNull(result.getId());
        assertEquals("X-Burger", result.getName());
        assertEquals("Delicious burger with cheese", result.getDescription());
        assertEquals(BigDecimal.valueOf(25.90), result.getPrice());
        assertFalse(result.isDineInOnly());
        assertEquals("images/xburger.png", result.getImagePath());
        assertEquals(restaurantId, result.getRestaurantId());
    }

    @Test
    @DisplayName("Should throw MenuItemNotFoundException when menu item by ID is not found")
    void shouldThrowExceptionWhenMenuItemDoesNotExist() {
        // Arrange
        var nonExistentId = UUID.randomUUID();

        when(menuItemRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(
                MenuItemNotFoundException.class,
                () -> listMenuItemsUseCase.getMenuItemById(nonExistentId)
        );

        assertEquals("Menu item not found", exception.getMessage());
        verify(menuItemRepository, times(1)).findById(nonExistentId);
    }

}
