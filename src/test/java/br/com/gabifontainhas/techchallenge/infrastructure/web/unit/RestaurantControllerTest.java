package br.com.gabifontainhas.techchallenge.infrastructure.web.unit;

import br.com.gabifontainhas.techchallenge.application.exception.RestaurantAlreadyExistsException;
import br.com.gabifontainhas.techchallenge.application.exception.RestaurantNotFoundException;
import br.com.gabifontainhas.techchallenge.application.usecases.restaurant.*;
import br.com.gabifontainhas.techchallenge.domain.entities.Restaurant;
import br.com.gabifontainhas.techchallenge.domain.valueobjects.Address;
import br.com.gabifontainhas.techchallenge.infrastructure.web.dto.AddressDTO;
import br.com.gabifontainhas.techchallenge.infrastructure.web.dto.RestaurantDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

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
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateRestaurantUseCase createRestaurantUseCase;

    @MockitoBean
    private ListRestaurantsUseCase listRestaurantsUseCase;

    @MockitoBean
    private ListRestaurantByIdUseCase listRestaurantByIdUseCase;

    @MockitoBean
    private DeleteRestaurantUseCase deleteRestaurantUseCase;

    @MockitoBean
    private UpdateRestaurantUseCase updateRestaurantUseCase;

    @Nested
    @DisplayName("Tests for Restaurant Creation (POST /v1/restaurants)")
    class CreateRestaurantTests {

        @Test
        @DisplayName("Should create restaurant successfully and return 201 Created")
        void shouldCreateRestaurantWithSuccess() throws Exception {
            // Arrange
            var addressRequest = new AddressDTO.Request("Main Street", "123", "Downtown", "New York", "NY", "12345000");
            var ownerId = UUID.randomUUID();
            var request = new RestaurantDTO.PostRequest(
                    "Holy Burger",
                    addressRequest,
                    "Fast Food",
                    "08:00-22:00",
                    ownerId
            );

            var createdRestaurant = new Restaurant(
                    "Holy Burger",
                    addressRequest.toDomain(),
                    "Fast Food",
                    "08:00-22:00",
                    ownerId
            );

            when(createRestaurantUseCase.create(any())).thenReturn(createdRestaurant);

            //Act & Assert
            mockMvc.perform(post("/v1/restaurants")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value("Holy Burger"))
                    .andExpect(jsonPath("$.cuisineType").value("Fast Food"))
                    .andExpect(jsonPath("$.operatingHours").value("08:00-22:00"))
                    .andExpect(jsonPath("$.ownerId").value(ownerId.toString()))

                    .andExpect(jsonPath("$.address.street").value("Main Street"))
                    .andExpect(jsonPath("$.address.number").value("123"))
                    .andExpect(jsonPath("$.address.neighborhood").value("Downtown"))
                    .andExpect(jsonPath("$.address.city").value("New York"))
                    .andExpect(jsonPath("$.address.state").value("NY"))
                    .andExpect(jsonPath("$.address.zipCode").value("12345000"));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when validation fails")
        void shouldReturn400WhenPayloadIsInvalid() throws Exception {
            //Arrange
            var invalidRequest = new RestaurantDTO.PostRequest(
                    "",
                    null,
                    "",
                    "",
                    null
            );

            //Act & Assert
            mockMvc.perform(post("/v1/restaurants")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
            verifyNoInteractions(createRestaurantUseCase);
        }

        @Test
        @DisplayName("Should return 422 Unprocessable Content when restaurant already exists")
        void shouldReturn422WhenRestaurantAlreadyExists() throws Exception {
            // Arrange
            var addressRequest = new AddressDTO.Request("Main Street", "123", "Downtown", "New York", "NY", "12345000");
            var ownerId = UUID.randomUUID();
            var request = new RestaurantDTO.PostRequest(
                    "Holy Burger",
                    addressRequest,
                    "Fast Food",
                    "08:00-22:00",
                    ownerId
            );

            when(createRestaurantUseCase.create(any())).thenThrow(new RestaurantAlreadyExistsException("Restaurant already exists"));

            // Act & Assert
            mockMvc.perform(post("/v1/restaurants")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnprocessableContent());
        }
    }

    @Nested
    @DisplayName("Tests for Listing Restaurants (GET /v1/restaurants)")
    class ListRestaurantsTests {

        @Test
        @DisplayName("Should return list of restaurants and 200 OK")
        void shouldReturnListOfRestaurantsWithSuccess() throws Exception {
            // Arrange
            var address = new Address("Main Street", "123", "Downtown", "New York", "NY", "12345000");
            var ownerId1 = UUID.randomUUID();
            var ownerId2 = UUID.randomUUID();

            var restaurant1 = new Restaurant(
                    "Holy Burger",
                    address,
                    "Fast Food",
                    "08:00-22:00",
                    ownerId1
            );

            var restaurant2 = new Restaurant(
                    "Mario Pizza",
                    address,
                    "Italian Food",
                    "18:00-23:00",
                    ownerId2
            );

            when(listRestaurantsUseCase.getAllRestaurants()).thenReturn(List.of(restaurant1, restaurant2));

            // Act & Assert
            mockMvc.perform(get("/v1/restaurants"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))


                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].name").value("Holy Burger"))
                    .andExpect(jsonPath("$[0].cuisineType").value("Fast Food"))
                    .andExpect(jsonPath("$[0].operatingHours").value("08:00-22:00"))
                    .andExpect(jsonPath("$[0].ownerId").value(ownerId1.toString()))

                    .andExpect(jsonPath("$[0].address.street").value("Main Street"))
                    .andExpect(jsonPath("$[0].address.number").value("123"))
                    .andExpect(jsonPath("$[0].address.neighborhood").value("Downtown"))
                    .andExpect(jsonPath("$[0].address.city").value("New York"))
                    .andExpect(jsonPath("$[0].address.state").value("NY"))
                    .andExpect(jsonPath("$[0].address.zipCode").value("12345000"))

                    .andExpect(jsonPath("$[1].id").exists())
                    .andExpect(jsonPath("$[1].name").value("Mario Pizza"))
                    .andExpect(jsonPath("$[1].cuisineType").value("Italian Food"))
                    .andExpect(jsonPath("$[1].operatingHours").value("18:00-23:00"))
                    .andExpect(jsonPath("$[1].ownerId").value(ownerId2.toString()))

                    .andExpect(jsonPath("$[1].address.street").value("Main Street"))
                    .andExpect(jsonPath("$[1].address.number").value("123"))
                    .andExpect(jsonPath("$[1].address.neighborhood").value("Downtown"))
                    .andExpect(jsonPath("$[1].address.city").value("New York"))
                    .andExpect(jsonPath("$[1].address.state").value("NY"))
                    .andExpect(jsonPath("$[1].address.zipCode").value("12345000"));

        }

        @Test
        @DisplayName("Should return a specific restaurant by ID with 200 OK")
        void shouldReturnRestaurantByIdWithSuccess() throws Exception {
            // Arrange
            var restaurantId = UUID.randomUUID();
            var ownerId = UUID.randomUUID();
            var address = new Address("Main Street", "123", "Downtown", "New York", "NY", "12345000");

            var restaurant = new Restaurant(
                    restaurantId,
                    "Holy Burger",
                    address,
                    "Fast Food",
                    "08:00-22:00",
                    ownerId
            );

            when(listRestaurantByIdUseCase.getRestaurantById(restaurantId)).thenReturn(restaurant);

            // Act & Assert
            mockMvc.perform(get("/v1/restaurants/{id}", restaurantId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(restaurantId.toString()))
                    .andExpect(jsonPath("$.name").value("Holy Burger"))
                    .andExpect(jsonPath("$.cuisineType").value("Fast Food"))
                    .andExpect(jsonPath("$.operatingHours").value("08:00-22:00"))
                    .andExpect(jsonPath("$.ownerId").value(ownerId.toString()))

                    .andExpect(jsonPath("$.address.street").value("Main Street"))
                    .andExpect(jsonPath("$.address.number").value("123"))
                    .andExpect(jsonPath("$.address.neighborhood").value("Downtown"))
                    .andExpect(jsonPath("$.address.city").value("New York"))
                    .andExpect(jsonPath("$.address.state").value("NY"))
                    .andExpect(jsonPath("$.address.zipCode").value("12345000"));
        }

        @Test
        @DisplayName("Should return 404 Not Found when restaurant is not registered")
        void shouldReturn404WhenRestaurantDoesNotExist() throws Exception {
            // Arrange
            var nonExistentId = UUID.randomUUID();
            when(listRestaurantByIdUseCase.getRestaurantById(nonExistentId))
                    .thenThrow(new RestaurantNotFoundException("Restaurant not found"));

            // Act & Assert
            mockMvc.perform(get("/v1/restaurants/{id}", nonExistentId))
                    .andExpect(status().isNotFound());

        }
    }

    @Nested
    @DisplayName("Tests for Restaurant Deletion (DELETE /v1/restaurants/{id})")
    class DeleteRestaurantTests {

        @Test
        @DisplayName("Should delete restaurant successfully and return 204 No Content")
        void shouldDeleteRestaurantWithSuccess() throws Exception {
            // Arrange
            var restaurantId = UUID.randomUUID();
            doNothing().when(deleteRestaurantUseCase).delete(restaurantId);

            // Act & Assert
            mockMvc.perform(delete("/v1/restaurants/{id}", restaurantId))
                    .andExpect(status().isNoContent());

        }

        @Test
        @DisplayName("Should return 404 Not Found when trying to delete a non-existent restaurant")
        void shouldReturn404WhenRestaurantToDeleteDoesNotExist() throws Exception {
            // Arrange
            var restaurantId = UUID.randomUUID();
            doThrow(new RestaurantNotFoundException("Restaurant not found"))
                    .when(deleteRestaurantUseCase).delete(restaurantId);

            // Act & Assert
            mockMvc.perform(delete("/v1/restaurants/{id}", restaurantId))
                    .andExpect(status().isNotFound());

        }
    }

    @Nested
    @DisplayName("Tests for Restaurant Update (PUT /v1/restaurants/{id})")
    class UpdateRestaurantTests {

        @Test
        @DisplayName("Should update restaurant details successfully and return 200 OK")
        void shouldUpdateRestaurantWithSuccess() throws Exception {
            // Arrange
            var restaurantId = UUID.randomUUID();
            var ownerId = UUID.randomUUID();
            var address = new AddressDTO.Request("Main Street", "123", "Downtown", "New York", "NY", "12345000");

            var putRequest = new RestaurantDTO.PutRequest("Mario Pizza", address, "Italian Food", "18:00-23:00", ownerId);
            var updatedRestaurant = new Restaurant(restaurantId, "Mario Pizza", address.toDomain(), "Italian Food", "18:00-23:00", ownerId);

            when(updateRestaurantUseCase.update(any(), eq(restaurantId))).thenReturn(updatedRestaurant);

            // Act & Assert
            mockMvc.perform(put("/v1/restaurants/{id}", restaurantId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(putRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(restaurantId.toString()))
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value("Mario Pizza"))
                    .andExpect(jsonPath("$.cuisineType").value("Italian Food"))
                    .andExpect(jsonPath("$.operatingHours").value("18:00-23:00"))
                    .andExpect(jsonPath("$.ownerId").value(ownerId.toString()))

                    .andExpect(jsonPath("$.address.street").value("Main Street"))
                    .andExpect(jsonPath("$.address.number").value("123"))
                    .andExpect(jsonPath("$.address.neighborhood").value("Downtown"))
                    .andExpect(jsonPath("$.address.city").value("New York"))
                    .andExpect(jsonPath("$.address.state").value("NY"))
                    .andExpect(jsonPath("$.address.zipCode").value("12345000"));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when update payload contains empty fields")
        void shouldReturn400WhenUpdatePayloadIsInvalid() throws Exception {
            // Arrange
            var restaurantId = UUID.randomUUID();
            var invalidPutRequest = new RestaurantDTO.PutRequest("", null, "Fast Food", "18:00-23:00", null);

            // Act & Assert
            mockMvc.perform(put("/v1/restaurants/{id}", restaurantId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidPutRequest)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(updateRestaurantUseCase);
        }

        @Test
        @DisplayName("Should return 404 Not Found when trying to update a non-existent restaurant")
        void shouldReturn404WhenRestaurantToUpdateDoesNotExist() throws Exception {
            // Arrange
            var restaurantId = UUID.randomUUID();
            var ownerId = UUID.randomUUID();
            var address = new AddressDTO.Request("Main Street", "123", "Downtown", "New York", "NY", "12345000");
            var putRequest = new RestaurantDTO.PutRequest("Mario Pizza", address, "Italian Food", "18:00-23:00", ownerId);

            when(updateRestaurantUseCase.update(any(), eq(restaurantId)))
                    .thenThrow(new RestaurantNotFoundException("Restaurant not found"));

            // Act & Assert
            mockMvc.perform(put("/v1/restaurants/{id}", restaurantId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(putRequest)))
                    .andExpect(status().isNotFound());
        }
    }
}