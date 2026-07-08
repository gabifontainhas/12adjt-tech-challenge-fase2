package br.com.gabifontainhas.techchallenge.application.usecases.menuitem;

import br.com.gabifontainhas.techchallenge.application.exception.RestaurantNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteMenuItemUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private DeleteMenuItemUseCase deleteMenuItemUseCase;

    @Test
    @DisplayName("Should delete menu item successfully when item exists")
    void shouldDeleteMenuItemSuccessfully() {
        // Arrange
        var menuItemId = UUID.randomUUID();

        when(menuItemRepository.existsById(menuItemId)).thenReturn(true);
        doNothing().when(menuItemRepository).delete(menuItemId);

        // Act & Assert
        assertDoesNotThrow(() -> deleteMenuItemUseCase.delete(menuItemId));

        verify(menuItemRepository, times(1)).delete(menuItemId);
    }

    @Test
    @DisplayName("Should throw RestaurantNotFoundException when trying to delete a non-existent menu item")
    void shouldThrowExceptionWhenMenuItemDoesNotExist() {
        // Arrange
        var nonExistentId = UUID.randomUUID();

        when(menuItemRepository.existsById(nonExistentId)).thenReturn(false);

        // Act & Assert
        var exception = assertThrows(
                RestaurantNotFoundException.class,
                () -> deleteMenuItemUseCase.delete(nonExistentId)
        );

        var expectedMessage = "Could not delete: MenuItem with ID " + nonExistentId + " not found";
        assertEquals(expectedMessage, exception.getMessage());

        verify(menuItemRepository, never()).delete(any(UUID.class));
    }
}
