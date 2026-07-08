package br.com.gabifontainhas.techchallenge.application.usecases.customer;

import br.com.gabifontainhas.techchallenge.application.exception.EmailAlreadyExistsException;
import br.com.gabifontainhas.techchallenge.application.gateway.CustomerRepository;
import br.com.gabifontainhas.techchallenge.application.usecases.dto.CustomerDTO;
import br.com.gabifontainhas.techchallenge.domain.entities.Customer;
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
public class CreateCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CreateCustomerUseCase createCustomerUseCase;

    @Test
    @DisplayName("Should create a customer successfully when data is valid and the email does not exist")
    void shouldCreateCustomerSuccessfully() {
        // Arrange
        var request = new CustomerDTO.PostRequest("jimhalpert@dundermifflin.com", "Jim Halpert", "11999999999");
        var savedCustomer = new Customer(request.email(), request.name(), request.phoneNumber());

        when(customerRepository.existsByEmail(request.email())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        // Act
        var result = createCustomerUseCase.create(request);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(request.name(), result.getName());
        assertEquals(request.email(), result.getEmail());
        assertEquals(request.phoneNumber(), result.
                getPhoneNumber());
    }

    @Test
    @DisplayName("Should throw EmailAlreadyExistsException when email is already registered")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Arrange
        var request = new CustomerDTO.PostRequest("jimhalpert@dundermifflin.com", "Jim Halpert", "11999999999");

        when(customerRepository.existsByEmail(request.email())).thenReturn(true);

        // Act & Assert
        var exception = assertThrows(
                EmailAlreadyExistsException.class,
                () -> createCustomerUseCase.create(request)
        );

        assertEquals("E-mail already exists", exception.getMessage());

        verify(customerRepository, never()).save(any(Customer.class));
    }
}
