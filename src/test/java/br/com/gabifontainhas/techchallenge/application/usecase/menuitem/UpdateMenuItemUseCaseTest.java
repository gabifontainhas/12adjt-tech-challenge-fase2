package br.com.gabifontainhas.techchallenge.application.usecase.menuitem;

import br.com.gabifontainhas.techchallenge.application.exception.MenuItemNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;
import br.com.gabifontainhas.techchallenge.application.usecase.dto.UpdateMenuItemCommand;
import br.com.gabifontainhas.techchallenge.domain.entity.MenuItem;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateMenuItemUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private UpdateMenuItemUseCase updateMenuItemUseCase;

    @Test
    @DisplayName("Should update menu item details successfully when item exists and request is valid")
    void shouldUpdateMenuItemSuccessfully() {

        // Arrange
        var menuItemId = UUID.randomUUID();
        var restaurantId = UUID.randomUUID();

        var existingMenuItem = new MenuItem(
                menuItemId,
                "Cheese Burger",
                "Delicious burger with cheese",
                BigDecimal.valueOf(25.90),
                false,
                "images/cheeseburger.png",
                restaurantId
        );
        var putRequest = new UpdateMenuItemCommand(
                "Chocolate Milkshake",
                "Delicious milkshake with chocolate syrup",
                BigDecimal.valueOf(14.90),
                true,
                "images/milkshake.png"
        );

        var updatedMenuItem = new MenuItem(
                menuItemId,
                "Chocolate Milkshake",
                "Delicious milkshake with chocolate syrup",
                BigDecimal.valueOf(14.90),
                true,
                "images/milkshake.png",
                restaurantId
        );

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(updatedMenuItem);

        // Act
        var result = updateMenuItemUseCase.update(putRequest, menuItemId);

        // Assert
        assertNotNull(result);
        assertEquals(menuItemId, result.getId());
        assertEquals(putRequest.name(), result.getName());
        assertEquals(putRequest.description(), result.getDescription());
        assertEquals(putRequest.price(), result.getPrice());
        assertTrue(result.isDineInOnly());
        assertEquals(putRequest.imagePath(), result.getImagePath());
        assertEquals(restaurantId, result.getRestaurantId());
    }

    @Test
    @DisplayName("Should throw MenuItemNotFoundException when trying to update a non-existent menu item")
    void shouldThrowExceptionWhenMenuItemDoesNotExist() {
        // Arrange

        var nonExistentId = UUID.randomUUID();
        var putRequest = new UpdateMenuItemCommand(
                "Cheese Burger",
                "Delicious burger with cheese",
                BigDecimal.valueOf(25.90),
                false,
                "images/cheeseburger.png"
        );

        when(menuItemRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(
                MenuItemNotFoundException.class,
                () -> updateMenuItemUseCase.update(putRequest, nonExistentId)
        );

        assertEquals("Menu item not found", exception.getMessage());

        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }
}
