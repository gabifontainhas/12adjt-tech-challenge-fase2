package br.com.gabifontainhas.techchallenge.application.usecase.restaurant;

import br.com.gabifontainhas.techchallenge.application.exception.RestaurantNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
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
class DeleteRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private DeleteRestaurantUseCase deleteRestaurantUseCase;

    @Test
    @DisplayName("Should delete restaurant successfully when restaurant exists")
    void shouldDeleteRestaurantSuccessfully() {
        // Arrange
        var restaurantId = UUID.randomUUID();

        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);
        doNothing().when(restaurantRepository).delete(restaurantId);

        // Act & Assert
        assertDoesNotThrow(() -> deleteRestaurantUseCase.delete(restaurantId));

        verify(menuItemRepository, times(1)).deleteByRestaurantId(restaurantId);
        verify(restaurantRepository, times(1)).delete(restaurantId);
    }

    @Test
    @DisplayName("Should throw RestaurantNotFoundException when trying to delete a non-existent restaurant")
    void shouldThrowExceptionWhenRestaurantDoesNotExist() {
        // Arrange
        var nonExistentId = UUID.randomUUID();

        when(restaurantRepository.existsById(nonExistentId)).thenReturn(false);

        // Act & Assert
        var exception = assertThrows(
                RestaurantNotFoundException.class,
                () -> deleteRestaurantUseCase.delete(nonExistentId)
        );

        var expectedMessage = "Could not delete: Restaurant with ID " + nonExistentId + " not found";
        assertEquals(expectedMessage, exception.getMessage());

        verify(restaurantRepository, never()).delete(any(UUID.class));
    }

}
