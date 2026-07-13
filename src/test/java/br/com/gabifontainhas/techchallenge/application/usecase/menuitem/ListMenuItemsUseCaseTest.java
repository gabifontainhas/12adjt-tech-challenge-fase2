package br.com.gabifontainhas.techchallenge.application.usecase.menuitem;

import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;
import br.com.gabifontainhas.techchallenge.domain.entity.MenuItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListMenuItemsUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private ListMenuItemsUseCase listMenuItemsUseCase;

    @Test
    @DisplayName("Should return a list of all menu items in the system")
    void shouldReturnAllMenuItems() {

        // Arrange
        var restaurantId = UUID.randomUUID();

        var menuItem1 = new MenuItem(
                "Cheese Burger",
                "Delicious burger with cheese",
                BigDecimal.valueOf(25.90),
                false,
                "images/cheeseburger.png",
                restaurantId
        );

        var menuItem2 = new MenuItem(
                "Chocolate Milkshake",
                "Delicious milkshake with chocolate syrup",
                BigDecimal.valueOf(14.90),
                true,
                "images/milkshake.png",
                restaurantId
        );

        when(menuItemRepository.findAll()).thenReturn(List.of(menuItem1, menuItem2));

        // Act
        var result = listMenuItemsUseCase.getAllMenuItems();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertNotNull(result.getFirst().getId());
        assertEquals("Cheese Burger", result.getFirst().getName());
        assertEquals("Delicious burger with cheese", result.getFirst().getDescription());
        assertEquals(BigDecimal.valueOf(25.90), result.getFirst().getPrice());
        assertFalse(result.getFirst().isDineInOnly());
        assertEquals("images/cheeseburger.png", result.getFirst().getImagePath());
        assertEquals(restaurantId, result.getFirst().getRestaurantId());

        assertNotNull(result.get(1).getId());
        assertEquals("Chocolate Milkshake", result.get(1).getName());
        assertEquals("Delicious milkshake with chocolate syrup", result.get(1).getDescription());
        assertEquals(BigDecimal.valueOf(14.90), result.get(1).getPrice());
        assertTrue(result.get(1).isDineInOnly());
        assertEquals("images/milkshake.png", result.get(1).getImagePath());
        assertEquals(restaurantId, result.get(1).getRestaurantId());


    }


    @Test
    @DisplayName("Should return an empty list when there are no menu items registered in the system")
    void shouldReturnEmptyListWhenNoMenuItemsExist() {
        // Arrange
        when(menuItemRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        var result = listMenuItemsUseCase.getAllMenuItems();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
