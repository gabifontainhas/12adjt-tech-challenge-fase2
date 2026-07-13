package br.com.gabifontainhas.techchallenge.application.usecase.menuitem;

import br.com.gabifontainhas.techchallenge.application.exception.RestaurantNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.domain.entity.MenuItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListMenuItemByRestaurantIdUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private ListMenuItemByRestaurantIdUseCase listMenuItemByRestaurantIdUseCase;

    @Test
    @DisplayName("Should return menu items of a specific restaurant successfully when restaurant exists")
    void shouldReturnMenuItemsByRestaurantSuccessfully() {
        // Arrange

        var restaurantId = UUID.randomUUID();

        var menuItem =  new MenuItem(
                "Cheese Burger",
                "Delicious burger with cheese",
                BigDecimal.valueOf(25.90),
                false,
                "images/cheeseburger.png",
                restaurantId
        );

        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);
        when(menuItemRepository.findByRestaurantId(restaurantId)).thenReturn(List.of(menuItem));

        // Act
        var result = listMenuItemByRestaurantIdUseCase.getAllMenuItemsByRestaurant(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        assertNotNull(result.getFirst().getId());
        assertEquals("Cheese Burger", result.getFirst().getName());
        assertEquals("Delicious burger with cheese", result.getFirst().getDescription());
        assertEquals(BigDecimal.valueOf(25.90), result.getFirst().getPrice());
        assertFalse(result.getFirst().isDineInOnly());
        assertEquals("images/cheeseburger.png", result.getFirst().getImagePath());
        assertEquals(restaurantId, result.getFirst().getRestaurantId());

    }


    @Test
    @DisplayName("Should throw RestaurantNotFoundException when trying to list items of a non-existent restaurant")
    void shouldThrowExceptionWhenRestaurantDoesNotExist() {
        // Arrange
        var nonExistentId = UUID.randomUUID();
        when(restaurantRepository.existsById(nonExistentId)).thenReturn(false);

        // Act & Assert
        var exception = assertThrows(
                RestaurantNotFoundException.class,
                () -> listMenuItemByRestaurantIdUseCase.getAllMenuItemsByRestaurant(nonExistentId)
        );

        assertEquals("Could not list menu items: The provided Restaurant does not exist", exception.getMessage());
        verify(menuItemRepository, never()).findByRestaurantId(any(UUID.class));
    }

}
