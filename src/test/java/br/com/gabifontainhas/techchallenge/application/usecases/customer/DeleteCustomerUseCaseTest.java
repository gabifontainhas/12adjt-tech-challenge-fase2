package br.com.gabifontainhas.techchallenge.application.usecases.customer;

import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.CustomerRepository;
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
public class DeleteCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private DeleteCustomerUseCase deleteCustomerUseCase;

    @Test
    @DisplayName("Should delete customer successfully when customer exists")
    void shouldDeleteCustomerSuccessfully() {
        // Arrange
        var customerId = UUID.randomUUID();

        when(customerRepository.existsById(customerId)).thenReturn(true);
        doNothing().when(customerRepository).delete(customerId);

        // Act & Assert
        assertDoesNotThrow(() -> deleteCustomerUseCase.delete(customerId));

        verify(customerRepository, times(1)).delete(customerId);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when trying to delete a non-existent customer")
    void shouldThrowExceptionWhenCustomerDoesNotExist() {
        // Arrange
        var nonExistentId = UUID.randomUUID();

        when(customerRepository.existsById(nonExistentId)).thenReturn(false);

        // Act & Assert
        var exception = assertThrows(
                UserNotFoundException.class,
                () -> deleteCustomerUseCase.delete(nonExistentId)
        );

        var expectedMessage = "Could not delete: Customer with ID " + nonExistentId + " not found";
        assertEquals(expectedMessage, exception.getMessage());

        verify(customerRepository, never()).delete(any(UUID.class));
    }

}
