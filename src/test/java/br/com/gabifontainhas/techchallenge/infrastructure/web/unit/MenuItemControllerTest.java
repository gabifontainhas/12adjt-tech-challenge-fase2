package br.com.gabifontainhas.techchallenge.infrastructure.web.unit;

import br.com.gabifontainhas.techchallenge.application.exception.MenuItemAlreadyExistsException;
import br.com.gabifontainhas.techchallenge.application.exception.MenuItemNotFoundException;
import br.com.gabifontainhas.techchallenge.application.usecase.menuitem.*;
import br.com.gabifontainhas.techchallenge.domain.entity.MenuItem;
import br.com.gabifontainhas.techchallenge.infrastructure.web.dto.MenuItemDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateMenuItemUseCase createMenuItemUseCase;

    @MockitoBean
    private ListMenuItemsUseCase listMenuItemsUseCase;

    @MockitoBean
    private ListMenuItemByIdUseCase listMenuItemByIdUseCase;

    @MockitoBean
    private ListMenuItemByRestaurantIdUseCase listMenuItemByRestaurantIdUseCase;

    @MockitoBean
    private DeleteMenuItemUseCase deleteMenuItemUseCase;

    @MockitoBean
    private UpdateMenuItemUseCase updateMenuItemUseCase;

    @Nested
    @DisplayName("Tests for Menu Item Creation (POST /v1/menuItems)")
    class CreateMenuItemTests {

        @Test
        @DisplayName("Should create menu item successfully and return 201 Created")
        void shouldCreateMenuItemWithSuccess() throws Exception {
            // Arrange
            var restaurantId = UUID.randomUUID();
            var request = new MenuItemDTO.PostRequest("Cheese Burger",
                    "Delicious burger with cheese",
                    BigDecimal.valueOf(25.90),
                    false,
                    "images/cheeseburger.png",
                    restaurantId
            );

            var createdItem = new MenuItem("Cheese Burger",
                    "Delicious burger with cheese",
                    BigDecimal.valueOf(25.90),
                    false,
                    "images/cheeseburger.png",
                    restaurantId);

            when(createMenuItemUseCase.create(any())).thenReturn(createdItem);

            // Act & Assert
            mockMvc.perform(post("/v1/menuItems")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value("Cheese Burger"))
                    .andExpect(jsonPath("$.description").value("Delicious burger with cheese"))
                    .andExpect(jsonPath("$.price").value(25.90))
                    .andExpect(jsonPath("$.dineInOnly").value(false))
                    .andExpect(jsonPath("$.imagePath").value("images/cheeseburger.png"))
                    .andExpect(jsonPath("$.restaurantId").value(restaurantId.toString()));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when validation fails due to empty mandatory fields")
        void shouldReturn400WhenPayloadIsInvalid() throws Exception {
            // Arrange
            var restaurantId = UUID.randomUUID();
            var invalidRequest = new MenuItemDTO.PostRequest(
                    "",
                    "Delicious burger with cheese",
                    null,
                    false,
                    "images/cheeseburger.png",
                    restaurantId);

            // Act & Assert
            mockMvc.perform(post("/v1/menuItems")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            Mockito.verifyNoInteractions(createMenuItemUseCase);
        }

        @Test
        @DisplayName("Should return 422 Unprocessable Content when menuItem already exists in restaurant")
        void shouldReturn422WhenMenuItemAlreadyExistsInRestaurant() throws Exception {
            // Arrange
            var restaurantId = UUID.randomUUID();
            var request = new MenuItemDTO.PostRequest("Cheese Burger",
                    "Delicious burger with cheese",
                    BigDecimal.valueOf(25.90),
                    false,
                    "images/cheeseburger.png",
                    restaurantId
            );

            when(createMenuItemUseCase.create(any())).thenThrow(new MenuItemAlreadyExistsException("MenuItem already exists in the restaurant"));

            // Act & Assert
            mockMvc.perform(post("/v1/menuItems")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnprocessableContent());
        }
    }

    @Nested
    @DisplayName("Tests for Listing Menu Items (GET /v1/menuItems)")
    class ListMenuItemsTests {

        @Test
        @DisplayName("Should return list of all menu items and 200 OK")
        void shouldReturnListOfAllMenuItemsWithSuccess() throws Exception {
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

            when(listMenuItemsUseCase.getAllMenuItems()).thenReturn(List.of(menuItem1, menuItem2));

            // Act & Assert
            mockMvc.perform(get("/v1/menuItems"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))

                    .andExpect(jsonPath("$[0].name").value("Cheese Burger"))
                    .andExpect(jsonPath("$[0].description").value("Delicious burger with cheese"))
                    .andExpect(jsonPath("$[0].price").value(25.90))
                    .andExpect(jsonPath("$[0].dineInOnly").value(false))
                    .andExpect(jsonPath("$[0].imagePath").value("images/cheeseburger.png"))
                    .andExpect(jsonPath("$[0].restaurantId").value(restaurantId.toString()))

                    .andExpect(jsonPath("$[1].name").value("Chocolate Milkshake"))
                    .andExpect(jsonPath("$[1].description").value("Delicious milkshake with chocolate syrup"))
                    .andExpect(jsonPath("$[1].price").value(14.90))
                    .andExpect(jsonPath("$[1].dineInOnly").value(true))
                    .andExpect(jsonPath("$[1].imagePath").value("images/milkshake.png"))
                    .andExpect(jsonPath("$[1].restaurantId").value(restaurantId.toString()));
        }

        @Test
        @DisplayName("Should return specific menu item by ID with 200 OK")
        void shouldReturnMenuItemByIdWithSuccess() throws Exception {
            // Arrange
            var menuItemId = UUID.randomUUID();
            var restaurantId = UUID.randomUUID();
            var menuItem = new MenuItem(
                    menuItemId,
                    "Cheese Burger",
                    "Delicious burger with cheese",
                    BigDecimal.valueOf(25.90),
                    false,
                    "images/cheeseburger.png",
                    restaurantId
            );

            when(listMenuItemByIdUseCase.getMenuItemById(menuItemId)).thenReturn(menuItem);

            // Act & Assert
            mockMvc.perform(get("/v1/menuItems/{id}", menuItemId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(menuItemId.toString()))
                    .andExpect(jsonPath("$.name").value("Cheese Burger"))
                    .andExpect(jsonPath("$.description").value("Delicious burger with cheese"))
                    .andExpect(jsonPath("$.price").value(25.90))
                    .andExpect(jsonPath("$.dineInOnly").value(false))
                    .andExpect(jsonPath("$.imagePath").value("images/cheeseburger.png"))
                    .andExpect(jsonPath("$.restaurantId").value(restaurantId.toString()));
        }

        @Test
        @DisplayName("Should return 404 Not Found when menu item by ID does not exist")
        void shouldReturn404WhenMenuItemDoesNotExist() throws Exception {
            // Arrange
            var nonExistentId = UUID.randomUUID();
            when(listMenuItemByIdUseCase.getMenuItemById(nonExistentId))
                    .thenThrow(new MenuItemNotFoundException("Menu item not found"));

            // Act & Assert
            mockMvc.perform(get("/v1/menuItems/{id}", nonExistentId))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should return list of menu items of a specific restaurant and 200 OK")
        void shouldReturnListByRestaurantIdWithSuccess() throws Exception {
            // Arrange
            var restaurantId = UUID.randomUUID();
            var menuItem = new MenuItem(
                    "Cheese Burger",
                    "Delicious burger with cheese",
                    BigDecimal.valueOf(25.90),
                    false,
                    "images/cheeseburger.png",
                    restaurantId
            );

            when(listMenuItemByRestaurantIdUseCase.getAllMenuItemsByRestaurant(restaurantId)).thenReturn(List.of(menuItem));

            // Act & Assert
            mockMvc.perform(get("/v1/menuItems/restaurant/{restaurantId}", restaurantId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))

                    .andExpect(jsonPath("$[0].name").value("Cheese Burger"))
                    .andExpect(jsonPath("$[0].description").value("Delicious burger with cheese"))
                    .andExpect(jsonPath("$[0].price").value(25.90))
                    .andExpect(jsonPath("$[0].dineInOnly").value(false))
                    .andExpect(jsonPath("$[0].imagePath").value("images/cheeseburger.png"))
                    .andExpect(jsonPath("$[0].restaurantId").value(restaurantId.toString()));

        }
    }

    @Nested
    @DisplayName("Tests for Menu Item Deletion (DELETE /v1/menuItems/{id})")
    class DeleteMenuItemTests {

        @Test
        @DisplayName("Should delete menu item successfully and return 204 No Content")
        void shouldDeleteMenuItemWithSuccess() throws Exception {
            // Arrange
            var itemId = UUID.randomUUID();
            doNothing().when(deleteMenuItemUseCase).delete(itemId);

            // Act & Assert
            mockMvc.perform(delete("/v1/menuItems/{id}", itemId))
                    .andExpect(status().isNoContent());

            verify(deleteMenuItemUseCase, times(1)).delete(itemId);
        }

        @Test
        @DisplayName("Should return 404 Not Found when trying to delete a non-existent menu item")
        void shouldReturn404WhenMenuItemToDeleteDoesNotExist() throws Exception {
            // Arrange
            var itemId = UUID.randomUUID();
            doThrow(new MenuItemNotFoundException("Menu item not found"))
                    .when(deleteMenuItemUseCase).delete(itemId);

            // Act & Assert
            mockMvc.perform(delete("/v1/menuItems/{id}", itemId))
                    .andExpect(status().isNotFound());

        }
    }

    @Nested
    @DisplayName("Tests for Menu Item Update (PUT /v1/menuItems/{id})")
    class UpdateMenuItemTests {

        @Test
        @DisplayName("Should update menu item details successfully and return 200 OK")
        void shouldUpdateMenuItemWithSuccess() throws Exception {
            // Arrange
            var itemId = UUID.randomUUID();
            var restaurantId = UUID.randomUUID();
            var putRequest = new MenuItemDTO.PutRequest("Cheese Burger",
                    "Delicious burger with cheese",
                    BigDecimal.valueOf(25.90),
                    false,
                    "images/cheeseburger.png");

            var updatedItem = new MenuItem(
                    itemId,
                    "Cheese Burger",
                    "Delicious burger with cheese",
                    BigDecimal.valueOf(25.90),
                    false,
                    "images/cheeseburger.png",
                    restaurantId
            );

            when(updateMenuItemUseCase.update(any(), eq(itemId))).thenReturn(updatedItem);

            // Act & Assert
            mockMvc.perform(put("/v1/menuItems/{id}", itemId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(putRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(itemId.toString()))
                    .andExpect(jsonPath("$.name").value("Cheese Burger"))
                    .andExpect(jsonPath("$.description").value("Delicious burger with cheese"))
                    .andExpect(jsonPath("$.price").value(25.90))
                    .andExpect(jsonPath("$.dineInOnly").value(false))
                    .andExpect(jsonPath("$.imagePath").value("images/cheeseburger.png"))
                    .andExpect(jsonPath("$.restaurantId").value(restaurantId.toString())
                    );
        }
        @Test
        @DisplayName("Should return 400 Bad Request when update payload contains empty fields")
        void shouldReturn400WhenUpdatePayloadIsInvalid() throws Exception {
            // Arrange
            var itemId = UUID.randomUUID();
            var invalidPutRequest = new MenuItemDTO.PutRequest(
                    "",
                    "",
                    null,
                    false,
                    "");

            // Act & Assert
            mockMvc.perform(put("/v1/menuItems/{id}", itemId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidPutRequest)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(updateMenuItemUseCase);
        }


        @Test
        @DisplayName("Should return 404 Not Found when trying to update a non-existent menu item")
        void shouldReturn404WhenMenuItemToUpdateDoesNotExist() throws Exception {
            // Arrange
            var itemId = UUID.randomUUID();
            var putRequest = new MenuItemDTO.PutRequest(
                    "Cheese Burger",
                    "Delicious burger with cheese",
                    BigDecimal.valueOf(25.90),
                    false,
                    "images/cheeseburger.png");

            when(updateMenuItemUseCase.update(any(), eq(itemId)))
                    .thenThrow(new MenuItemNotFoundException("Menu item not found"));

            // Act & Assert
            mockMvc.perform(put("/v1/menuItems/{id}", itemId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(putRequest)))
                    .andExpect(status().isNotFound());

        }
    }
}
