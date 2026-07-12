package br.com.gabifontainhas.techchallenge.application.usecases.menuitem;

import br.com.gabifontainhas.techchallenge.application.exception.MenuItemNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;
import br.com.gabifontainhas.techchallenge.domain.entities.MenuItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListMenuItemByIdUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private ListMenuItemByIdUseCase listMenuItemByIdUseCase;

    @Test
    @DisplayName("Should return a specific menu item by ID when it exists")
    void shouldReturnMenuItemById() {
        // Arrange
        var menuItemId = UUID.randomUUID();
        var restaurantId = UUID.randomUUID();
        var expectedItem =  new MenuItem(
                "Cheese Burger",
                "Delicious burger with cheese",
                BigDecimal.valueOf(25.90),
                false,
                "images/cheeseburger.png",
                restaurantId
        );

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(expectedItem));

        // Act
        var result = listMenuItemByIdUseCase.getMenuItemById(menuItemId);

        // Assert
        assertNotNull(result);

        assertNotNull(result.getId());
        assertEquals("Cheese Burger", result.getName());
        assertEquals("Delicious burger with cheese", result.getDescription());
        assertEquals(BigDecimal.valueOf(25.90), result.getPrice());
        assertFalse(result.isDineInOnly());
        assertEquals("images/cheeseburger.png", result.getImagePath());
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
                () -> listMenuItemByIdUseCase.getMenuItemById(nonExistentId)
        );

        assertEquals("Menu item not found", exception.getMessage());
        verify(menuItemRepository, times(1)).findById(nonExistentId);
    }

}
