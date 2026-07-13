package br.com.gabifontainhas.techchallenge.application.usecase.owner;

import br.com.gabifontainhas.techchallenge.application.gateway.OwnerRepository;
import br.com.gabifontainhas.techchallenge.domain.entity.Owner;
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
class ListOwnersUseCaseTest {

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private ListOwnersUseCase listOwnersUseCase;

    @Test
    @DisplayName("Should return a list of owners when owners exist in the database")
    void shouldReturnListOfOwners() {
        // Arrange
        var owner1 = new Owner("jim.halpert@dundermifflin.com","Jim Halpert", "11999999999");
        var owner2 = new Owner("michael.scott@dundermifflin.com", "Michael Scott","11888888888");

        when(ownerRepository.findAll()).thenReturn(List.of(owner1, owner2));

        // Act
        var result = listOwnersUseCase.getAllOwners();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Jim Halpert", result.getFirst().getName());
        assertEquals("jim.halpert@dundermifflin.com", result.getFirst().getEmail());
        assertEquals("11999999999", result.getFirst().getBusinessPhone());
        assertNotNull(result.getFirst().getId());

        assertEquals("Michael Scott", result.get(1).getName());
        assertEquals("michael.scott@dundermifflin.com", result.get(1).getEmail());
        assertEquals("11888888888", result.get(1).getBusinessPhone());
        assertNotNull(result.get(1).getId());
    }


    @Test
    @DisplayName("Should return an empty list when no owners are registered")
    void shouldReturnEmptyListWhenNoOwnersExist() {
        // Arrange
        when(ownerRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        var result = listOwnersUseCase.getAllOwners();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
