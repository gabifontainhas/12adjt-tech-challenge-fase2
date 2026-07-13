package br.com.gabifontainhas.techchallenge.application.usecase.owner;

import br.com.gabifontainhas.techchallenge.application.exception.CannotDeleteOwnerIfHasRestaurant;
import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
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
class DeleteOwnerUseCaseTest {

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private DeleteOwnerUseCase deleteOwnerUseCase;


    @Test
    @DisplayName("Should delete owner successfully when owner exists")
    void shouldDeleteOwnerSuccessfully() {
        // Arrange
        var ownerId = UUID.randomUUID();

        when(ownerRepository.existsById(ownerId)).thenReturn(true);
        when(restaurantRepository.existsByOwnerId(ownerId)).thenReturn(false);

        doNothing().when(ownerRepository).delete(ownerId);

        // Act & Assert
        assertDoesNotThrow(() -> deleteOwnerUseCase.delete(ownerId));

        verify(ownerRepository, times(1)).delete(ownerId);
        verify(restaurantRepository, times(1)).existsByOwnerId(ownerId);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when trying to delete a non-existent owner")
    void shouldThrowExceptionWhenOwnerDoesNotExist() {
        // Arrange
        var nonExistentId = UUID.randomUUID();

        when(ownerRepository.existsById(nonExistentId)).thenReturn(false);

        // Act & Assert
        var exception = assertThrows(
                UserNotFoundException.class,
                () -> deleteOwnerUseCase.delete(nonExistentId)
        );

        var expectedMessage = "Could not delete: Owner with ID " + nonExistentId + " not found";
        assertEquals(expectedMessage, exception.getMessage());

        verify(ownerRepository, never()).delete(any(UUID.class));
    }

    @Test
    @DisplayName("Should throw CannotDeleteOwnerIfHasRestaurant when owner has associated restaurants")
    void shouldThrowExceptionWhenOwnerHasRestaurants() {
        // Arrange
        var ownerId = UUID.randomUUID();

        when(ownerRepository.existsById(ownerId)).thenReturn(true);
        when(restaurantRepository.existsByOwnerId(ownerId)).thenReturn(true);

        // Act & Assert
        assertThrows(CannotDeleteOwnerIfHasRestaurant.class, () -> deleteOwnerUseCase.delete(ownerId));

        // Garantias cruciais de segurança:
        verify(restaurantRepository, times(1)).existsByOwnerId(ownerId);
        verify(ownerRepository, never()).delete(ownerId);
    }

}
