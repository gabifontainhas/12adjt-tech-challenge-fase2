package br.com.gabifontainhas.techchallenge.infrastructure.web.integration;

import br.com.gabifontainhas.techchallenge.domain.entities.Owner;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.CustomerJpaRepository;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.MenuItemJpaRepository;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.OwnerJpaRepository;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.RestaurantJpaRepository;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.mapper.OwnerMapper;
import br.com.gabifontainhas.techchallenge.infrastructure.web.dto.AddressDTO;
import br.com.gabifontainhas.techchallenge.infrastructure.web.dto.RestaurantDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestaurantControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

    @Autowired
    private OwnerJpaRepository ownerJpaRepository;

    @Autowired
    private MenuItemJpaRepository menuItemJpaRepository;

    @Autowired
    private CustomerJpaRepository customerJpaRepository;

    private UUID existingOwnerId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/v1/restaurants";

        menuItemJpaRepository.deleteAll();
        restaurantJpaRepository.deleteAll();
        ownerJpaRepository.deleteAll();
        customerJpaRepository.deleteAll();

        var defaultOwner = new Owner("michael.scott@dundermifflin.com", "Michael Scott", "11999999999");
        var savedOwner = ownerJpaRepository.save(OwnerMapper.toJpaEntity(defaultOwner));

        this.existingOwnerId = savedOwner.getId();
    }

    @Nested
    @DisplayName("Tests for Restaurant Creation (POST)")
    class CreateRestaurantIntegrationTests {

        @Test
        @DisplayName("Should create restaurant successfully and return 201 Created")
        void shouldCreateRestaurantWithSuccess() {
            var addressRequest = new AddressDTO.Request("Main Street", "123", "Downtown", "New York", "NY", "12345000");
            var requestPayload = new RestaurantDTO.PostRequest(
                    "Holy Burger",
                    addressRequest,
                    "Fast Food",
                    "08:00-22:00",
                    existingOwnerId
            );

            given()
                    .contentType(ContentType.JSON)
                    .body(requestPayload)
                    .when()
                    .post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("id", notNullValue())
                    .body("name", equalTo("Holy Burger"))
                    .body("cuisineType", equalTo("Fast Food"))
                    .body("operatingHours", equalTo("08:00-22:00"))
                    .body("ownerId", equalTo(existingOwnerId.toString()))

                    .body("address.street", equalTo("Main Street"))
                    .body("address.number", equalTo("123"))
                    .body("address.neighborhood", equalTo("Downtown"))
                    .body("address.city", equalTo("New York"))
                    .body("address.state", equalTo("NY"))
                    .body("address.zipCode", equalTo("12345000"));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when validation fails due to empty fields")
        void shouldReturn400WhenPayloadIsInvalid() {
            // Tentativa de criação sem nome e com dados ausentes
            var invalidPayload = new RestaurantDTO.PostRequest(
                    "",
                    null,
                    "Fast Food",
                    "08:00-22:00",
                    null
            );

            given()
                    .contentType(ContentType.JSON)
                    .body(invalidPayload)
                    .when()
                    .post()
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("Should return 404 Not Found when the provided Owner ID does not exist")
        void shouldReturn404WhenOwnerDoesNotExist() {
            var nonExistentOwnerId = UUID.randomUUID();
            var addressRequest = new AddressDTO.Request("Main Street", "123", "Downtown", "New York", "NY", "12345000");
            var requestPayload = new RestaurantDTO.PostRequest(
                    "Holy Burger",
                    addressRequest,
                    "Fast Food",
                    "08:00-22:00",
                    nonExistentOwnerId
            );

            given()
                    .contentType(ContentType.JSON)
                    .body(requestPayload)
                    .when()
                    .post()
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @Nested
    @DisplayName("Tests for Listing Restaurants (GET)")
    class ListRestaurantsIntegrationTests {

        @Test
        @DisplayName("Should return a list of registered restaurants and 200 OK")
        void shouldReturnListOfRestaurantsSuccessfully() {
            // Arrange
            var addressRequest = new AddressDTO.Request("Main Street", "123", "Downtown", "New York", "NY", "12345000");

            var requestPayload1 = new RestaurantDTO.PostRequest(
                    "Holy Burger",
                    addressRequest,
                    "Fast Food",
                    "08:00-22:00",
                    existingOwnerId
            );
            var requestPayload2 = new RestaurantDTO.PostRequest(
                    "Mario Pizza",
                    addressRequest,
                    "Italian Food",
                    "18:00-23:00",
                    existingOwnerId
            );

            given().contentType(ContentType.JSON).body(requestPayload1).post();
            given().contentType(ContentType.JSON).body(requestPayload2).post();

            // Act & Assert
            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", hasSize(2))

                    .body("[0].name", equalTo("Holy Burger"))
                    .body("[0].cuisineType", equalTo("Fast Food"))
                    .body("[0].operatingHours", equalTo("08:00-22:00"))
                    .body("[0].ownerId", equalTo(existingOwnerId.toString()))

                    .body("[0].address.street", equalTo("Main Street"))
                    .body("[0].address.number", equalTo("123"))
                    .body("[0].address.neighborhood", equalTo("Downtown"))
                    .body("[0].address.city", equalTo("New York"))
                    .body("[0].address.state", equalTo("NY"))
                    .body("[0].address.zipCode", equalTo("12345000"))

                    .body("[1].name", equalTo("Mario Pizza"))
                    .body("[1].cuisineType", equalTo("Italian Food"))
                    .body("[1].operatingHours", equalTo("18:00-23:00"))
                    .body("[1].ownerId", equalTo(existingOwnerId.toString()))

                    .body("[1].address.street", equalTo("Main Street"))
                    .body("[1].address.number", equalTo("123"))
                    .body("[1].address.neighborhood", equalTo("Downtown"))
                    .body("[1].address.city", equalTo("New York"))
                    .body("[1].address.state", equalTo("NY"))
                    .body("[1].address.zipCode", equalTo("12345000"));
        }

        @Test
        @DisplayName("Should return a specific restaurant by ID with 200 OK")
        void shouldReturnRestaurantByIdSuccessfully() {
            // Arrange
            var addressRequest = new AddressDTO.Request("Main Street", "123", "Downtown", "New York", "NY", "12345000");

            var requestPayload = new RestaurantDTO.PostRequest(
                    "Holy Burger",
                    addressRequest,
                    "Fast Food",
                    "08:00-22:00",
                    existingOwnerId
            );

            String createdId = given()
                    .contentType(ContentType.JSON)
                    .body(requestPayload)
                    .post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id");

            // Act & Assert
            given()
                    .pathParam("id", createdId)
                    .when()
                    .get("/{id}")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(createdId))

                    .body("name", equalTo("Holy Burger"))
                    .body("cuisineType", equalTo("Fast Food"))
                    .body("operatingHours", equalTo("08:00-22:00"))
                    .body("ownerId", equalTo(existingOwnerId.toString()))

                    .body("address.street", equalTo("Main Street"))
                    .body("address.number", equalTo("123"))
                    .body("address.neighborhood", equalTo("Downtown"))
                    .body("address.city", equalTo("New York"))
                    .body("address.state", equalTo("NY"))
                    .body("address.zipCode", equalTo("12345000"));
        }
        @Test
        @DisplayName("Should return 404 Not Found when restaurant ID does not exist")
        void shouldReturn404WhenIdDoesNotExist() {
            var nonExistentId = UUID.randomUUID();

            given()
                    .pathParam("id", nonExistentId)
                    .when()
                    .get("/{id}")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @Nested
    @DisplayName("Tests for Restaurant Update (PUT)")
    class UpdateRestaurantIntegrationTests {

        @Test
        @DisplayName("Should update restaurant details successfully and return 200 OK")
        void shouldUpdateRestaurantWithSuccess() {
            // Arrange

            var addressRequest = new AddressDTO.Request("Main Street", "123", "Downtown", "New York", "NY", "12345000");

            var requestPayload = new RestaurantDTO.PostRequest(
                    "Holy Burger",
                    addressRequest,
                    "Fast Food",
                    "08:00-22:00",
                    existingOwnerId
            );

            String createdId = given()
                    .contentType(ContentType.JSON)
                    .body(requestPayload)
                    .post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id");

            var updatedAddress = new AddressDTO.Request("5th Street", "987", "Downtown", "New Jersey", "NY", "12345999");
            var updatePayload = new RestaurantDTO.PutRequest(
                    "Mario Pizza",
                    updatedAddress,
                    "Italian Food",
                    "18:00-23:00",
                    existingOwnerId
            );

            // Act & Assert
            given()
                    .contentType(ContentType.JSON)
                    .pathParam("id", createdId)
                    .body(updatePayload)
                    .when()
                    .put("/{id}")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(createdId))
                    .body("name", equalTo("Mario Pizza"))
                    .body("cuisineType", equalTo("Italian Food"))
                    .body("operatingHours", equalTo("18:00-23:00"))
                    .body("ownerId", equalTo(existingOwnerId.toString()))

                    .body("address.street", equalTo("5th Street"))
                    .body("address.number", equalTo("987"))
                    .body("address.neighborhood", equalTo("Downtown"))
                    .body("address.city", equalTo("New Jersey"))
                    .body("address.state", equalTo("NY"))
                    .body("address.zipCode", equalTo("12345999"));

            var persistedRestaurant = restaurantJpaRepository.findById(UUID.fromString(createdId));
            assertTrue(persistedRestaurant.isPresent());
            equalTo("Tasty Burger Gourmet").matches(persistedRestaurant.get().getName());
        }
    }

    @Nested
    @DisplayName("Tests for Restaurant Deletion (DELETE)")
    class DeleteRestaurantIntegrationTests {

        @Test
        @DisplayName("Should delete restaurant successfully and return 204 No Content")
        void shouldDeleteRestaurantWithSuccess() {
            // Arrange
            var addressRequest = new AddressDTO.Request("Main Street", "123", "Downtown", "New York", "NY", "12345000");

            var requestPayload = new RestaurantDTO.PostRequest(
                    "Holy Burger",
                    addressRequest,
                    "Fast Food",
                    "08:00-22:00",
                    existingOwnerId
            );

            String createdId = given()
                    .contentType(ContentType.JSON)
                    .body(requestPayload)
                    .post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id");

            var restaurantId = UUID.fromString(createdId);

            assertTrue(restaurantJpaRepository.existsById(restaurantId));

            // Act & Assert
            given()
                    .pathParam("id", createdId)
                    .when()
                    .delete("/{id}")
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // Garante que o registro foi removido definitivamente do H2
            assertFalse(restaurantJpaRepository.existsById(restaurantId));
        }
    }
}
