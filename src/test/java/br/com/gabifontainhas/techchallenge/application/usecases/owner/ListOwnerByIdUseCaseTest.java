package br.com.gabifontainhas.techchallenge.application.usecases.owner;

import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.domain.entities.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListOwnerByIdUseCaseTest {

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private ListOwnerByIdUseCase listOwnerByIdUseCase;

    @Test
    @DisplayName("Should return an owner by ID when the owner exists")
    void shouldReturnOwnerById() {
        // Arrange
        var ownerId = UUID.randomUUID();
        var expectedOwner = new Owner(ownerId, "jim.halpert@dundermifflin.com", "Jim Halpert", LocalDate.now(),"11999999999");

        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(expectedOwner));

        // Act
        var result = listOwnerByIdUseCase.getOwnerById(ownerId);

        // Assert
        assertNotNull(result);
        assertEquals(ownerId, result.getId());
        assertEquals("Jim Halpert", result.getName());
        assertEquals("jim.halpert@dundermifflin.com", result.getEmail());
        assertEquals("11999999999", result.getBusinessPhone());
        assertNotNull(result.getLastUpdate());
    }


    @Test
    @DisplayName("Should throw UserNotFoundException when customer by ID is not found")
    void shouldThrowExceptionWhenCustomerByIdDoesNotExist() {
        // Arrange
        var nonExistentId = UUID.randomUUID();
        when(ownerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> listOwnerByIdUseCase.getOwnerById(nonExistentId)
        );

        assertEquals("Owner not found", exception.getMessage());
    }
}
