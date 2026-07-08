package br.com.gabifontainhas.techchallenge.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    @DisplayName("Should instantiate customer correctly using the convenience domain constructor")
    void shouldCreateCustomerWithConvenienceConstructor() {
        // Arrange & Act
        var customer = new Customer("jim.halpert@dundermifflin.com","Jim Halpert", "11999999999");

        // Assert
        assertNotNull(customer);
        assertNotNull(customer.getId());

        assertEquals("Jim Halpert", customer.getName());
        assertEquals("jim.halpert@dundermifflin.com", customer.getEmail());
        assertEquals("11999999999", customer.getPhoneNumber());
    }

    @Test
    @DisplayName("Should instantiate customer correctly using the constructor with all arguments")
    void shouldCreateCustomerWithConstructorWithAllArguments() {
        // Arrange & Act
        var customerId = UUID.randomUUID();
        var lastUpdate = LocalDate.now();
        var customer = new Customer(customerId, "jim.halpert@dundermifflin.com","Jim Halpert", lastUpdate,"11999999999");

        // Assert
        assertNotNull(customer);
        assertEquals(customerId, customer.getId());

        assertEquals("Jim Halpert", customer.getName());
        assertEquals("jim.halpert@dundermifflin.com", customer.getEmail());
        assertEquals("11999999999", customer.getPhoneNumber());
        assertEquals(lastUpdate, customer.getLastUpdate());
    }

    @Test
    @DisplayName("Should update customer details and refresh lastUpdate timestamp successfully")
    void shouldUpdateCustomerDetailsSuccessfully() {
        // Arrange
        var customer = new Customer("jim.halpert@dundermifflin.com","Jim Halpert", "11999999999");

        customer.update("Michael Scott", "11888888888");

        assertEquals("Michael Scott", customer.getName());
        assertEquals("11888888888", customer.getPhoneNumber());
        assertEquals("jim.halpert@dundermifflin.com", customer.getEmail());
        assertEquals(LocalDate.now(), customer.getLastUpdate());
    }

}