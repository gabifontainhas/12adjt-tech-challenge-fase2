package br.com.gabifontainhas.techchallenge.infrastructure.persistence.mapper;

import br.com.gabifontainhas.techchallenge.domain.entity.Customer;
import br.com.gabifontainhas.techchallenge.infrastructure.persistence.entity.CustomerJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    @Test
    @DisplayName("Should return null when mapping null Customer domain to JpaEntity")
    void shouldReturnNullWhenDomainIsNull() {
        assertNull(CustomerMapper.toJpaEntity(null));
    }

    @Test
    @DisplayName("Should return null when mapping null CustomerJpaEntity to domain")
    void shouldReturnNullWhenEntityIsNull() {
        assertNull(CustomerMapper.toDomain(null));
    }

    @Test
    @DisplayName("Should map Customer domain to JpaEntity with all inherited and specific attributes")
    void shouldMapDomainToJpaEntitySuccessfully() {
        // Arrange
        var id = UUID.randomUUID();
        var lastUpdate = LocalDate.now().minusDays(5);

        var domain = new Customer(id,"jimhalpert@dundermifflin.com", "Jim Halpert", lastUpdate, "11999999999");

        // Act
        var entity = CustomerMapper.toJpaEntity(domain);

        // Assert
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("Jim Halpert", entity.getName());
        assertEquals("jimhalpert@dundermifflin.com", entity.getEmail());
        assertEquals(lastUpdate, entity.getLastUpdate());
        assertEquals("11999999999", entity.getPhoneNumber());
    }

    @Test
    @DisplayName("Should map CustomerJpaEntity to Customer domain with all attributes")
    void shouldMapJpaEntityToDomainSuccessfully() {
        // Arrange
        var id = UUID.randomUUID();
        var lastUpdate = LocalDate.now().minusDays(5);

        var entity = new CustomerJpaEntity(id, "jimhalpert@dundermifflin.com", "Jim Halpert", "11999999999", lastUpdate);

        // Act
        var domain = CustomerMapper.toDomain(entity);

        // Assert
        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals("Jim Halpert", domain.getName());
        assertEquals("jimhalpert@dundermifflin.com", domain.getEmail());
        assertEquals(lastUpdate, domain.getLastUpdate());
        assertEquals("11999999999", domain.getPhoneNumber());
    }
}