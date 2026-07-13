package br.com.gabifontainhas.techchallenge.infrastructure.persistence.mapper;

import br.com.gabifontainhas.techchallenge.domain.entity.Owner;
import br.com.gabifontainhas.techchallenge.infrastructure.persistence.entity.OwnerJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OwnerMapperTest {

    @Test
    @DisplayName("Should return null when mapping null Owner domain to JpaEntity")
    void shouldReturnNullWhenDomainIsNull() {
        assertNull(OwnerMapper.toJpaEntity(null));
    }

    @Test
    @DisplayName("Should return null when mapping null OwnerJpaEntity to domain")
    void shouldReturnNullWhenEntityIsNull() {
        assertNull(OwnerMapper.toDomain(null));
    }

    @Test
    @DisplayName("Should map Owner domain to JpaEntity with all attributes")
    void shouldMapDomainToJpaEntitySuccessfully() {
        // Arrange
        var id = UUID.randomUUID();
        var lastUpdate = LocalDate.now().minusDays(5);

        var domain = new Owner(id,"michael.scott@dundermifflin.com", "Michael Scott", lastUpdate, "11999999999");

        // Act
        var entity = OwnerMapper.toJpaEntity(domain);

        // Assert
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("Michael Scott", entity.getName());
        assertEquals("michael.scott@dundermifflin.com", entity.getEmail());
        assertEquals(lastUpdate, entity.getLastUpdate());
        assertEquals("11999999999", entity.getBusinessPhone());
    }

    @Test
    @DisplayName("Should map OwnerJpaEntity to Owner domain correctly")
    void shouldMapJpaEntityToDomainSuccessfully() {
        // Arrange
        var id = UUID.randomUUID();
        var lastUpdate = LocalDate.now().minusDays(5);

        var entity = new OwnerJpaEntity(id, "michael.scott@dundermifflin.com", "Michael Scott", "11999999999", lastUpdate);

        // Act
        var domain = OwnerMapper.toDomain(entity);

        // Assert
        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals("Michael Scott", domain.getName());
        assertEquals("michael.scott@dundermifflin.com", domain.getEmail());
        assertEquals(lastUpdate, domain.getLastUpdate());
        assertEquals("11999999999", domain.getBusinessPhone());
    }
}