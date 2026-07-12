package br.com.gabifontainhas.techchallenge.application.usecases.menuitem;

import br.com.gabifontainhas.techchallenge.application.exception.MenuItemAlreadyExistsException;
import br.com.gabifontainhas.techchallenge.application.exception.RestaurantNotFoundException;
import br.com.gabifontainhas.techchallenge.application.gateway.MenuItemRepository;
import br.com.gabifontainhas.techchallenge.application.gateway.RestaurantRepository;
import br.com.gabifontainhas.techchallenge.application.usecases.dto.CreateMenuItemCommand;
import br.com.gabifontainhas.techchallenge.domain.entities.MenuItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateMenuItemUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private CreateMenuItemUseCase createMenuItemUseCase;

    @Test
    @DisplayName("Should create a menu item successfully when restaurant exists and item is unique")
    void shouldCreateMenuItemSuccessfully() {

        // Arrange
        var restaurantId = UUID.randomUUID();
        var request = new CreateMenuItemCommand(
                "Cheese Burger",
                "Delicious burger with cheese",
                BigDecimal.valueOf(25.90),
                false,
                "images/cheeseburger.png",
                restaurantId
        );
        var expectedMenuItem = new MenuItem(
                request.name(),
                request.description(),
                request.price(),
                request.dineInOnly(),
                request.imagePath(),
                request.restaurantId()
        );

        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);
        when(menuItemRepository.existsByNameAndRestaurantId(request.name(), restaurantId)).thenReturn(false);
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(expectedMenuItem);

        // Act
        var result = createMenuItemUseCase.create(request);


        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(request.name(), result.getName());
        assertEquals(request.description(), result.getDescription());
        assertEquals(request.price(), result.getPrice());
        assertFalse(result.isDineInOnly());
        assertEquals(request.imagePath(), result.getImagePath());
        assertEquals(restaurantId, result.getRestaurantId());

    }

    @Test
    @DisplayName("Should throw RestaurantNotFoundException when the provided restaurant does not exist")
    void shouldThrowExceptionWhenRestaurantDoesNotExist() {

        // Arrange
        var restaurantId = UUID.randomUUID();
        var request = new CreateMenuItemCommand(
                "Cheese Burger",
                "Delicious burger with cheese",
                BigDecimal.valueOf(25.90),
                false,
                "images/cheeseburger.png",
                restaurantId
        );

        when(restaurantRepository.existsById(restaurantId)).thenReturn(false);

        // Act & Assert
        var exception = assertThrows(
                RestaurantNotFoundException.class,
                () -> createMenuItemUseCase.create(request)
        );

        assertEquals("Could not create menu item: The provided Restaurant does not exist", exception.getMessage());

        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }

    @Test
    @DisplayName("Should throw MenuItemAlreadyExistsException when item with same name already exists in restaurant")
    void shouldThrowExceptionWhenMenuItemAlreadyExists() {
        // Arrange
        var restaurantId = UUID.randomUUID();
        var request = new CreateMenuItemCommand(
                "Cheese Burger",
                "Delicious burger with cheese",
                BigDecimal.valueOf(25.90),
                false,
                "images/cheeseburger.png",
                restaurantId
        );

        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);
        when(menuItemRepository.existsByNameAndRestaurantId(request.name(), restaurantId)).thenReturn(true);

        // Act & Assert
        var exception = assertThrows(
                MenuItemAlreadyExistsException.class,
                () -> createMenuItemUseCase.create(request)
        );

        assertEquals("MenuItem already exists in the restaurant", exception.getMessage());

        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }
}
