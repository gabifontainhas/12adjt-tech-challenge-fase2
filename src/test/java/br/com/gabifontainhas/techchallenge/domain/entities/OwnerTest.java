package br.com.gabifontainhas.techchallenge.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OwnerTest {

    @Test
    @DisplayName("Should instantiate owner correctly using the creation constructor")
    void shouldCreateOwnerWithCreationConstructor() {
        // Arrange & Act
        var owner = new Owner("michael.scott@dundermifflin.com", "Michael Scott", "11988888888");

        // Assert
        assertNotNull(owner);
        assertNotNull(owner.getId());
        assertEquals("Michael Scott", owner.getName());
        assertEquals("michael.scott@dundermifflin.com", owner.getEmail());
        assertEquals("11988888888", owner.getBusinessPhone());
        assertEquals(LocalDate.now(), owner.getLastUpdate());

    }

    @Test
    @DisplayName("Should instantiate owner correctly using the reconstruction constructor")
    void shouldCreateOwnerWithReconstructionConstructor() {
        // Arrange & Act
        var ownerId = UUID.randomUUID();
        var lastUpdate = LocalDate.now().minusDays(10);
        var owner = new Owner(ownerId,"michael.scott@dundermifflin.com", "Michael Scott", lastUpdate,"11988888888");

        // Assert
        assertNotNull(owner);
        assertEquals(ownerId, owner.getId());
        assertEquals("Michael Scott", owner.getName());
        assertEquals("michael.scott@dundermifflin.com", owner.getEmail());
        assertEquals("11988888888", owner.getBusinessPhone());
        assertEquals(lastUpdate, owner.getLastUpdate());

    }

    @Test
    @DisplayName("Should update owner name and business phone successfully")
    void shouldUpdateOwnerDetailsSuccessfully() {
        // Arrange
        var owner = new Owner("michael.scott@dundermifflin.com", "Michael Scott", "11988888888");

        // Act
        owner.update("Michael Gary Scott", "11977777777");

        // Assert
        assertEquals("Michael Gary Scott", owner.getName());
        assertEquals("michael.scott@dundermifflin.com", owner.getEmail());
        assertEquals("11977777777", owner.getBusinessPhone());
        assertEquals(LocalDate.now(), owner.getLastUpdate());

    }
}