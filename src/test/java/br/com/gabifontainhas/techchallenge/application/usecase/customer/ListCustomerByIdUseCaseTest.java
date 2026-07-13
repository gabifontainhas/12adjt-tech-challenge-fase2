package br.com.gabifontainhas.techchallenge.application.usecase.customer;

import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.CustomerRepository;
import br.com.gabifontainhas.techchallenge.domain.entity.Customer;
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
public class ListCustomerByIdUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ListCustomerByIdUseCase listCustomerByIdUseCase;

    @Test
    @DisplayName("Should return a customer by ID when the customer exists")
    void shouldReturnCustomerById() {
        // Arrange
        var customerId = UUID.randomUUID();
        var expectedCustomer = new Customer(customerId, "jim.halpert@dundermifflin.com", "Jim Halpert", LocalDate.now(),"11999999999");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(expectedCustomer));

        // Act
        var result = listCustomerByIdUseCase.getCustomersById(customerId);

        // Assert
        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals("Jim Halpert", result.getName());
        assertEquals("jim.halpert@dundermifflin.com", result.getEmail());
        assertEquals("11999999999", result.getPhoneNumber());
        assertNotNull(result.getLastUpdate());
    }


    @Test
    @DisplayName("Should throw UserNotFoundException when customer by ID is not found")
    void shouldThrowExceptionWhenCustomerByIdDoesNotExist() {
        // Arrange
        var nonExistentId = UUID.randomUUID();
        when(customerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(
                UserNotFoundException.class,
                () -> listCustomerByIdUseCase.getCustomersById(nonExistentId)
        );

        assertEquals("Customer not found", exception.getMessage());
    }
}
