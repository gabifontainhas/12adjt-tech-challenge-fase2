package br.com.gabifontainhas.techchallenge.application.usecases.customer;

import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.CustomerRepository;
import br.com.gabifontainhas.techchallenge.domain.entities.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListCustomersUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ListCustomersUseCase listCustomersUseCase;

    @Test
    @DisplayName("Should return a list of customers when customers exist in the database")
    void shouldReturnListOfCustomers() {
        // Arrange
        var customer1 = new Customer("jim.halpert@dundermifflin.com","Jim Halpert", "11999999999");
        var customer2 = new Customer("michael.scott@dundermifflin.com", "Michael Scott","11888888888");

        when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));

        // Act
        var result = listCustomersUseCase.getAllCustomers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Jim Halpert", result.getFirst().getName());
        assertEquals("jim.halpert@dundermifflin.com", result.getFirst().getEmail());
        assertEquals("11999999999", result.getFirst().getPhoneNumber());
        assertNotNull(result.getFirst().getId());

        assertEquals("Michael Scott", result.get(1).getName());
        assertEquals("michael.scott@dundermifflin.com", result.get(1).getEmail());
        assertEquals("11888888888", result.get(1).getPhoneNumber());
        assertNotNull(result.get(1).getId());
    }


    @Test
    @DisplayName("Should return an empty list when no customers are registered")
    void shouldReturnEmptyListWhenNoCustomersExist() {
        // Arrange
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        var result = listCustomersUseCase.getAllCustomers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return a customer by ID when the customer exists")
    void shouldReturnCustomerById() {
        // Arrange
        var customerId = UUID.randomUUID();
        var expectedCustomer = new Customer(customerId, "jim.halpert@dundermifflin.com", "Jim Halpert", LocalDate.now(),"11999999999");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(expectedCustomer));

        // Act
        var result = listCustomersUseCase.getCustomersById(customerId);

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
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> listCustomersUseCase.getCustomersById(nonExistentId)
        );

        assertEquals("Customer not found", exception.getMessage());
    }
}
