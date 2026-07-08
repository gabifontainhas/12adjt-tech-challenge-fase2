package br.com.gabifontainhas.techchallenge.application.usecases.owner;

import br.com.gabifontainhas.techchallenge.application.exception.EmailAlreadyExistsException;
import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.application.usecases.dto.CreateOwnerCommand;
import br.com.gabifontainhas.techchallenge.domain.entities.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateOwnerUseCaseTest {

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private CreateOwnerUseCase createOwnerUseCase;

    @Test
    @DisplayName("Should create an owner successfully when request is valid and email does not exist")
    void shouldCreateOwnerSuccessfully() {
        // Arrange
        var request = new CreateOwnerCommand("michael.scott@dundermifflin.com", "Michael Scott", "11999999999");
        var savedOwner = new Owner(
                request.email(),
                request.name(),
                request.businessPhone()
        );

        when(ownerRepository.existsByEmail(request.email())).thenReturn(false);
        when(ownerRepository.save(any(Owner.class))).thenReturn(savedOwner);

        // Act
        var result = createOwnerUseCase.create(request);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(request.name(), result.getName());
        assertEquals(request.email(), result.getEmail());
        assertEquals(request.businessPhone(), result.getBusinessPhone());
    }

    @Test
    @DisplayName("Should throw EmailAlreadyExistsException when trying to register an email already in use")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Arrange
        var request = new CreateOwnerCommand("michael.scott@dundermifflin.com", "Michael Scott", "11999999999");

        when(ownerRepository.existsByEmail(request.email())).thenReturn(true);

        // Act & Assert
        var exception = assertThrows(
                EmailAlreadyExistsException.class,
                () -> createOwnerUseCase.create(request)
        );

        assertEquals("E-mail already exists", exception.getMessage());

        verify(ownerRepository, never()).save(any(Owner.class));
    }
}
