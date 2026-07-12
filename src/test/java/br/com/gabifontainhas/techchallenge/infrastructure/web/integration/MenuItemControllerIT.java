package br.com.gabifontainhas.techchallenge.infrastructure.web.integration;

import br.com.gabifontainhas.techchallenge.domain.entities.MenuItem;
import br.com.gabifontainhas.techchallenge.domain.entities.Restaurant;
import br.com.gabifontainhas.techchallenge.domain.valueobjects.Address;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.MenuItemJpaRepository;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.RestaurantJpaRepository;
import br.com.gabifontainhas.techchallenge.infrastructure.persistance.mapper.RestaurantMapper;
import br.com.gabifontainhas.techchallenge.infrastructure.web.dto.MenuItemDTO;
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

import java.math.BigDecimal;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MenuItemControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private MenuItemJpaRepository menuItemJpaRepository;

    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

    private UUID existingRestaurantId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/v1/menuItems";

        menuItemJpaRepository.deleteAll();
        restaurantJpaRepository.deleteAll();

        var address = new Address("Main Street", "123", "Downtown", "New York", "NY", "12345000");

        var existingOwnerId = UUID.randomUUID();

        var defaultRestaurant = new Restaurant("Holy Burger",
                address,
                "Fast Food",
                "08:00-22:00",
                existingOwnerId
        );

        var savedRestaurant = restaurantJpaRepository.save(RestaurantMapper.toJpaEntity(defaultRestaurant));

        this.existingRestaurantId = savedRestaurant.getId();
    }

    @Nested
    @DisplayName("Tests for MenuItem Registration (POST)")
    class RegisterMenuItemIntegrationTests {

        @Test
        @DisplayName("Should register a menu item successfully and return 201 Created with persisted data")
        void shouldRegisterMenuItemWithSuccess() {
            var requestPayload = new MenuItemDTO.PostRequest("Cheese Burger",
                    "Delicious burger with cheese",
                    BigDecimal.valueOf(25.90),
                    false,
                    "images/cheeseburger.png",
                    existingRestaurantId
            );

            given()
                    .contentType(ContentType.JSON)
                    .body(requestPayload)
                    .when()
                    .post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("id", notNullValue())
                    .body("name", equalTo("Cheese Burger"))
                    .body("description", equalTo("Delicious burger with cheese"))
                    //.body("price", floatValue());
                    .body("dineInOnly", equalTo(false))
                    .body("imagePath", equalTo("images/cheeseburger.png"))
                    .body("restaurantId", equalTo(existingRestaurantId.toString()));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when payload violates validation constraints")
        void shouldReturn400WhenPayloadIsInvalid() {
            var invalidPayload = new MenuItemDTO.PostRequest("",
                    "",
                    null,
                    false,
                    "",
                    existingRestaurantId
            );

            given()
                    .contentType(ContentType.JSON)
                    .body(invalidPayload)
                    .when()
                    .post()
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("Tests for Listing Menu Items (GET)")
    class ListMenuItemsIntegrationTests {

        @Test
        @DisplayName("Should return a list of menu items and 200 OK")
        void shouldReturnListSuccessfully() {
            // Arrange

            var menuItem1Payload = new MenuItem(
                    "Cheese Burger",
                    "Delicious burger with cheese",
                    BigDecimal.valueOf(25.90),
                    false,
                    "images/cheeseburger.png",
                    existingRestaurantId
            );

            var menuItem2Payload = new MenuItem(
                    "Chocolate Milkshake",
                    "Delicious milkshake with chocolate syrup",
                    BigDecimal.valueOf(14.90),
                    true,
                    "images/milkshake.png",
                    existingRestaurantId
            );

            given().contentType(ContentType.JSON).body(menuItem1Payload).post();
            given().contentType(ContentType.JSON).body(menuItem2Payload).post();

            // Act & Assert
            var response = given()
                    .when()
                    .get();

            response.then()
                    .statusCode(HttpStatus.OK.value())

                    .body("", hasSize(2))

                    .body("[0].name", equalTo("Cheese Burger"))
                    .body("[0].description", equalTo("Delicious burger with cheese"))
                    //.body("price", floatValue());
                    .body("[0].dineInOnly", equalTo(false))
                    .body("[0].imagePath", equalTo("images/cheeseburger.png"))
                    .body("[0].restaurantId", equalTo(existingRestaurantId.toString()))

                    .body("[1].name", equalTo("Chocolate Milkshake"))
                    .body("[1].description", equalTo("Delicious milkshake with chocolate syrup"))
                    //.body("price", floatValue());
                    .body("[1].dineInOnly", equalTo(true))
                    .body("[1].imagePath", equalTo("images/milkshake.png"))
                    .body("[1].restaurantId", equalTo(existingRestaurantId.toString()));
        }

        @Test
        @DisplayName("Should return a specific menu item by ID with 200 OK")
        void shouldReturnMenuItemByIdSuccessfully() {
            // Arrange
            var requestPayload = new MenuItem(
                    "Cheese Burger",
                    "Delicious burger with cheese",
                    BigDecimal.valueOf(25.90),
                    false,
                    "images/cheeseburger.png",
                    existingRestaurantId
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
                    .body("name", equalTo("Cheese Burger"))
                    .body("description", equalTo("Delicious burger with cheese"))
                    //.body("price", floatValue());
                    .body("dineInOnly", equalTo(false))
                    .body("imagePath", equalTo("images/cheeseburger.png"))
                    .body("restaurantId", equalTo(existingRestaurantId.toString()));
        }

        @Test
        @DisplayName("Should return 404 Not Found when ID does not exist")
        void shouldReturn404WhenIdDoesNotExist() {
            var nonExistentId = UUID.randomUUID();

            given()
                    .pathParam("id", nonExistentId)
                    .when()
                    .get("/{id}")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("Should return menu items by restaurant ID with 200 OK")
        void shouldReturnMenuItemByRestaurantIdSuccessfully() {
            // Arrange
            var requestPayload = new MenuItem(
                    "Cheese Burger",
                    "Delicious burger with cheese",
                    BigDecimal.valueOf(25.90),
                    false,
                    "images/cheeseburger.png",
                    existingRestaurantId
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
                    .pathParam("id", existingRestaurantId)
                    .when()
                    .get("/restaurant/{id}")
                    .then()
                    .statusCode(HttpStatus.OK.value())

                    .body("$", hasSize(1))
                    .body("[0].id", equalTo(createdId))
                    .body("[0].name", equalTo("Cheese Burger"))
                    .body("[0].description", equalTo("Delicious burger with cheese"))
                    .body("[0].dineInOnly", equalTo(false))
                    .body("[0].imagePath", equalTo("images/cheeseburger.png"))
                    .body("[0].restaurantId", equalTo(existingRestaurantId.toString()));

        }
        @Test
        @DisplayName("Should return 404 Not Found when Restaurant ID does not exist")
        void shouldReturn404WhenRestaurantIdDoesNotExist() {
            var nonExistentId = UUID.randomUUID();

            given()
                    .pathParam("id", nonExistentId)
                    .when()
                    .get("/restaurant/{id}")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @Nested
    @DisplayName("Tests for Menu Item Update (PUT)")
    class UpdateMenuItemIntegrationTests {

        @Test
        @DisplayName("Should update menu item successfully and return 200 OK")
        void shouldUpdateMenuItemWithSuccess() {
            // Arrange
            var requestPayload = new MenuItem(
                    "Cheese Burger",
                    "Delicious burger with cheese",
                    BigDecimal.valueOf(25.90),
                    false,
                    "images/cheeseburger.png",
                    existingRestaurantId
            );

            String createdId = given()
                    .contentType(ContentType.JSON)
                    .body(requestPayload)
                    .post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id");

            var updatePayload = new MenuItemDTO.PutRequest(
                    "Chocolate Milkshake",
                    "Delicious milkshake with chocolate syrup",
                    BigDecimal.valueOf(14.90),
                    true,
                    "images/milkshake.png"
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
                    .body("name", equalTo("Chocolate Milkshake"))
                    .body("description", equalTo("Delicious milkshake with chocolate syrup"))
                    //.body("price", floatValue());
                    .body("dineInOnly", equalTo(true))
                    .body("imagePath", equalTo("images/milkshake.png"))
                    .body("restaurantId", equalTo(existingRestaurantId.toString()));

            var updatedEntity = menuItemJpaRepository.findById(UUID.fromString(createdId));
            assertTrue(updatedEntity.isPresent());
            equalTo("Chocolate Milkshake").matches(updatedEntity.get().getName());
            equalTo("Delicious milkshake with chocolate syrup").matches(updatedEntity.get().getDescription());
            equalTo("dineInOnly").matches(updatedEntity.get().isDineInOnly());
            equalTo("images/milkshake.png").matches(updatedEntity.get().getImagePath());
        }

        @Test
        @DisplayName("Should return 400 Bad Request when update payload is invalid")
        void shouldReturn400WhenUpdatePayloadIsInvalid() {
            var nonExistentId = UUID.randomUUID();
            var invalidUpdatePayload = new MenuItemDTO.PutRequest("",
                    "",
                    null,
                    false,
                    ""
            );

            given()
                    .contentType(ContentType.JSON)
                    .pathParam("id", nonExistentId)
                    .body(invalidUpdatePayload)
                    .when()
                    .put("/{id}")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("Tests for Menu Item Deletion (DELETE)")
    class DeleteMenuItemIntegrationTests {

        @Test
        @DisplayName("Should delete menu item successfully and return 204 No Content")
        void shouldDeleteMenuItemWithSuccess() {
            // Arrange
            var requestPayload = new MenuItemDTO.PostRequest("Cheese Burger",
                    "Delicious burger with cheese",
                    BigDecimal.valueOf(25.90),
                    false,
                    "images/cheeseburger.png",
                    existingRestaurantId
            );

            String createdId = given()
                    .contentType(ContentType.JSON)
                    .body(requestPayload)
                    .post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id");

            var menuItemId = UUID.fromString(createdId);

            assertTrue(menuItemJpaRepository.existsById(menuItemId));

            // Act & Assert
            given()
                    .pathParam("id", createdId)
                    .when()
                    .delete("/{id}")
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            assertFalse(menuItemJpaRepository.existsById(menuItemId));
        }

        @Test
        @DisplayName("Should return 404 Not Found when trying to delete an unexisting menu item ID")
        void shouldReturn404WhenDeletingNonExistentId() {
            var nonExistentId = UUID.randomUUID();

            given()
                    .pathParam("id", nonExistentId)
                    .when()
                    .delete("/{id}")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }
}
