package br.com.gabifontainhas.techchallenge.application.usecase.customer;

import br.com.gabifontainhas.techchallenge.application.gateway.CustomerRepository;
import br.com.gabifontainhas.techchallenge.domain.entity.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

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
}
