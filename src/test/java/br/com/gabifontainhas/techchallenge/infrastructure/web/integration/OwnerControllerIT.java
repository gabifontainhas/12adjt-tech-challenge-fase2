package br.com.gabifontainhas.techchallenge.infrastructure.web.integration;

import br.com.gabifontainhas.techchallenge.infrastructure.persistance.OwnerJpaRepository;
import br.com.gabifontainhas.techchallenge.infrastructure.web.dto.OwnerDTO;
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
public class OwnerControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private OwnerJpaRepository ownerJpaRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/v1/owners";

        ownerJpaRepository.deleteAll();
    }

    @Nested
    @DisplayName("Tests for Owner Registration (POST)")
    class RegisterOwnerIntegrationTests {

        @Test
        @DisplayName("Should register a owner successfully and return 201 Created with persisted data")
        void shouldRegisterOwnerWithSuccess() {
            var requestPayload = new OwnerDTO.PostRequest(
                    "jim.halpert@dundermifflin.com",
                    "Jim Halpert",
                    "11999999999"
            );

            given()
                    .contentType(ContentType.JSON)
                    .body(requestPayload)
                    .when()
                    .post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("id", notNullValue())
                    .body("name", equalTo("Jim Halpert"))
                    .body("email", equalTo("jim.halpert@dundermifflin.com"))
                    .body("businessPhone", equalTo("11999999999"));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when payload violates validation constraints")
        void shouldReturn400WhenPayloadIsInvalid() {
            var invalidPayload = new OwnerDTO.PostRequest(
                    "",
                    "Jim Halpert",
                    "11999999999"
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
    @DisplayName("Tests for Listing Owners (GET)")
    class ListOwnersIntegrationTests {

        @Test
        @DisplayName("Should return a list of owners and 200 OK")
        void shouldReturnListSuccessfully() {
            // Arrange
            var owner1Payload = new OwnerDTO.PostRequest("jim.halpert@dundermifflin.com", "Jim Halpert", "11999999999");
            var owner2Payload = new OwnerDTO.PostRequest("michael.scott@dundermifflin.com", "Michael Scott", "11888888888");

            given().contentType(ContentType.JSON).body(owner1Payload).post();
            given().contentType(ContentType.JSON).body(owner2Payload).post();

            // Act & Assert
            var response = given()
                    .when()
                    .get();

            response.then()
                    .statusCode(HttpStatus.OK.value())

                    .body("", hasSize(2))

                    .body("[0].name", equalTo("Jim Halpert"))
                    .body("[0].email", equalTo("jim.halpert@dundermifflin.com"))
                    .body("[0].businessPhone", equalTo("11999999999"))

                    .body("[1].name", equalTo("Michael Scott"))
                    .body("[1].email", equalTo("michael.scott@dundermifflin.com"))
                    .body("[1].businessPhone", equalTo("11888888888"));


        }

        @Test
        @DisplayName("Should return a specific owner by ID with 200 OK")
        void shouldReturnOwnerByIdSuccessfully() {
            // Arrange
            var requestPayload = new OwnerDTO.PostRequest("jim.halpert@dundermifflin.com", "Jim Halpert", "11999999999");

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
                    .body("name", equalTo("Jim Halpert"))
                    .body("email", equalTo("jim.halpert@dundermifflin.com"))
                    .body("businessPhone", equalTo("11999999999"));
        }

        @Test
        @DisplayName("Should return 404 Not Found when owner ID does not exist")
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
    @DisplayName("Tests for Owner Update (PUT)")
    class UpdateOwnerIntegrationTests {

        @Test
        @DisplayName("Should update owner successfully and return 200 OK")
        void shouldUpdateOwnerWithSuccess() {
            // Arrange
            var requestPayload = new OwnerDTO.PostRequest(
                    "jim.halpert@dundermifflin.com",
                    "Jim Halpert",
                    "11999999999"
            );

            String createdId = given()
                    .contentType(ContentType.JSON)
                    .body(requestPayload)
                    .post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id");

            var updatePayload = new OwnerDTO.PutRequest("James Halpert", "11977777777");

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
                    .body("name", equalTo("James Halpert"))
                    .body("businessPhone", equalTo("11977777777"))
                    .body("email", equalTo("jim.halpert@dundermifflin.com"));

            var updatedEntity = ownerJpaRepository.findById(UUID.fromString(createdId));
            assertTrue(updatedEntity.isPresent());
            equalTo("James Halpert").matches(updatedEntity.get().getName());
            equalTo("11977777777").matches(updatedEntity.get().getBusinessPhone());
        }

        @Test
        @DisplayName("Should return 400 Bad Request when update payload is invalid")
        void shouldReturn400WhenUpdatePayloadIsInvalid() {
            var nonExistentId = UUID.randomUUID();
            var invalidUpdatePayload = new OwnerDTO.PutRequest("", "   ");

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
    @DisplayName("Tests for Owner Deletion (DELETE)")
    class DeleteOwnerIntegrationTests {

        @Test
        @DisplayName("Should delete owner successfully and return 204 No Content")
        void shouldDeleteOwnerWithSuccess() {
            // Arrange
            var requestPayload = new OwnerDTO.PostRequest("jim.halpert@dundermifflin.com", "Jim Halpert", "11999999999");

            String createdId = given()
                    .contentType(ContentType.JSON)
                    .body(requestPayload)
                    .post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id");

            var ownerId = UUID.fromString(createdId);

            assertTrue(ownerJpaRepository.existsById(ownerId));

            // Act & Assert
            given()
                    .pathParam("id", createdId)
                    .when()
                    .delete("/{id}")
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            assertFalse(ownerJpaRepository.existsById(ownerId));
        }

        @Test
        @DisplayName("Should return 404 Not Found when trying to delete an unexisting owner ID")
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
