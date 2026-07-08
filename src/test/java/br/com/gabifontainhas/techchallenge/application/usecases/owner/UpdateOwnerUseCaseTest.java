package br.com.gabifontainhas.techchallenge.application.usecases.owner;

import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.application.usecases.dto.UpdateOwnerCommand;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateOwnerUseCaseTest {

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private UpdateOwnerUseCase updateOwnerUseCase;

    @Test
    @DisplayName("Should update owner details successfully when owner exists and request is valid")
    void shouldUpdateOwnerSuccessfully() {
        // Arrange
        var ownerId = UUID.randomUUID();
        var existingOwner = new Owner(ownerId, "jim.halpert@dundermifflin.com", "Jim Halpert", LocalDate.now(), "11999999999");
        var putRequest = new UpdateOwnerCommand("Michael Scott", "11988888888");
        var updatedOwner = new Owner(ownerId, "jim.halpert@dundermifflin.com", "Michael Scott", LocalDate.now(), "11988888888");

        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(existingOwner));
        when(ownerRepository.save(any(Owner.class))).thenReturn(updatedOwner);

        // Act
        var result = updateOwnerUseCase.update(putRequest, ownerId);

        // Assert - Validando os novos valores no retorno
        assertNotNull(result);
        assertEquals(ownerId, result.getId());
        assertEquals("Michael Scott", result.getName());
        assertEquals("11988888888", result.getBusinessPhone());
        assertEquals("jim.halpert@dundermifflin.com", result.getEmail());
        assertNotNull(result.getLastUpdate());
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when trying to update a non-existent owner")
    void shouldThrowExceptionWhenOwnerDoesNotExist() {
        // Arrange
        var nonExistentId = UUID.randomUUID();
        var putRequest = new UpdateOwnerCommand("Michael Scott", "11988888888");

        when(ownerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert - Executando e validando se lança a exceção esperada
        var exception = assertThrows(
                UserNotFoundException.class,
                () -> updateOwnerUseCase.update(putRequest, nonExistentId)
        );

        assertEquals("Owner not found", exception.getMessage());

        verify(ownerRepository, never()).save(any(Owner.class));
    }
}
