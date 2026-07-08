package br.com.gabifontainhas.techchallenge.application.usecases.customer;

import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.CustomerRepository;
import br.com.gabifontainhas.techchallenge.application.usecases.dto.UpdateCustomerCommand;
import br.com.gabifontainhas.techchallenge.domain.entities.Customer;
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
public class UpdateCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private UpdateCustomerUseCase updateCustomerUseCase;

    @Test
    @DisplayName("Should update customer details successfully when customer exists and request is valid")
    void shouldUpdateCustomerSuccessfully() {
        // Arrange
        var customerId = UUID.randomUUID();
        var existingCustomer = new Customer(customerId, "jim.halpert@dundermifflin.com", "Jim Halpert", LocalDate.now(),"11999999999");

        var putRequest = new UpdateCustomerCommand("James Halpert", "11988888888");

        var updatedCustomer = new Customer(customerId, "jim.halpert@dundermifflin.com", "James Halpert", LocalDate.now(),"11988888888");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        // Act
        var result = updateCustomerUseCase.update(putRequest, customerId);

        // Assert - Validando os novos valores no retorno
        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals("James Halpert", result.getName());
        assertEquals("11988888888", result.getPhoneNumber());
        assertEquals("jim.halpert@dundermifflin.com", result.getEmail());
        assertNotNull(result.getLastUpdate());
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when trying to update a non-existent customer")
    void shouldThrowExceptionWhenCustomerDoesNotExist() {
        // Arrange
        var nonExistentId = UUID.randomUUID();
        var putRequest = new UpdateCustomerCommand("James Halpert", "11988888888");

        when(customerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert - Executando e validando se lança a exceção esperada
        var exception = assertThrows(
                UserNotFoundException.class,
                () -> updateCustomerUseCase.update(putRequest, nonExistentId)
        );

        assertEquals("Customer not found", exception.getMessage());

        verify(customerRepository, never()).save(any(Customer.class));
    }
}
