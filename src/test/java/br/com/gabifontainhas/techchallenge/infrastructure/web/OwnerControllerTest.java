package br.com.gabifontainhas.techchallenge.infrastructure.web;

import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.usecases.owner.CreateOwnerUseCase;
import br.com.gabifontainhas.techchallenge.application.usecases.owner.DeleteOwnerUseCase;
import br.com.gabifontainhas.techchallenge.application.usecases.owner.ListOwnersUseCase;
import br.com.gabifontainhas.techchallenge.application.usecases.owner.UpdateOwnerUseCase;
import br.com.gabifontainhas.techchallenge.domain.entities.Owner;
import br.com.gabifontainhas.techchallenge.infrastructure.web.dto.OwnerDTO;
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

import java.time.LocalDate;
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
class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateOwnerUseCase createOwnerUseCase;

    @MockitoBean
    private ListOwnersUseCase listOwnersUseCase;

    @MockitoBean
    private DeleteOwnerUseCase deleteOwnerUseCase;

    @MockitoBean
    private UpdateOwnerUseCase updateOwnerUseCase;

    @Nested
    @DisplayName("Tests for Owner Creation (POST /v1/owners)")
    class CreateOwnerTests {

        @Test
        @DisplayName("Should create owner successfully and return 201 Created")
        void shouldCreateOwnerWithSuccess() throws Exception {
            // Arrange
            var request = new OwnerDTO.PostRequest("michael.scott@dundermifflin.com", "Michael Scott", "11999999999");
            var createdOwner = new Owner("michael.scott@dundermifflin.com", "Michael Scott", "11999999999");

            when(createOwnerUseCase.create(any())).thenReturn(createdOwner);

            // Act & Assert
            mockMvc.perform(post("/v1/owners")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value("Michael Scott"))
                    .andExpect(jsonPath("$.email").value("michael.scott@dundermifflin.com"))
                    .andExpect(jsonPath("$.businessPhone").value("11999999999"));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when validation fails due to empty mandatory fields")
        void shouldReturn400WhenPayloadIsInvalid() throws Exception {
            //Arrange
            var invalidRequest = new OwnerDTO.PostRequest("", "", "11999999999");

            // Act & Assert
            mockMvc.perform(post("/v1/owners")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
            Mockito.verifyNoInteractions(createOwnerUseCase);
        }
    }

    @Nested
    @DisplayName("Tests for Listing Owners (GET /v1/owners)")
    class ListOwnersTests {

        @Test
        @DisplayName("Should return list of owners and 200 OK")
        void shouldReturnListOfOwnersWithSuccess() throws Exception {
            // Arrange
            var owner1 = new Owner("jim.halpert@dundermifflin.com", "Jim Halpert", "11999999999");
            var owner2 = new Owner("michael.scott@dundermifflin.com", "Michael Scott", "11888888888");

            when(listOwnersUseCase.getAllOwners()).thenReturn(List.of(owner1, owner2));

            // Act & Assert
            mockMvc.perform(get("/v1/owners"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].name").value("Jim Halpert"))
                    .andExpect(jsonPath("$[0].email").value("jim.halpert@dundermifflin.com"))
                    .andExpect(jsonPath("$[0].businessPhone").value("11999999999"))
                    .andExpect(jsonPath("$[1].name").value("Michael Scott"))
                    .andExpect(jsonPath("$[1].email").value("michael.scott@dundermifflin.com"))
                    .andExpect(jsonPath("$[1].businessPhone").value("11888888888"));
        }

        @Test
        @DisplayName("Should return a specific owner by ID with 200 OK")
        void shouldReturnOwnerByIdWithSuccess() throws Exception {
            // Arrange
            var ownerId = UUID.randomUUID();
            var lastUpdate = LocalDate.now();

            var owner =  new Owner(ownerId,"michael.scott@dundermifflin.com", "Michael Scott", lastUpdate, "11888888888");

            when(listOwnersUseCase.getOwnerById(ownerId)).thenReturn(owner);

            // Act & Assert
            mockMvc.perform(get("/v1/owners/{id}", ownerId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(ownerId.toString()))
                    .andExpect(jsonPath("$.name").value("Michael Scott"))
                    .andExpect(jsonPath("$.email").value("michael.scott@dundermifflin.com"))
                    .andExpect(jsonPath("$.businessPhone").value("11888888888"))
                    .andExpect(jsonPath("$.lastUpdate").value(lastUpdate.toString()));
        }


        @Test
        @DisplayName("Should return 404 Not Found when owner by ID is not registered")
        void shouldReturn404WhenOwnerDoesNotExist() throws Exception {
            // Arrange
            var nonExistentId = UUID.randomUUID();
            when(listOwnersUseCase.getOwnerById(nonExistentId))
                    .thenThrow(new UserNotFoundException("Owner not found"));

            // Act & Assert
            mockMvc.perform(get("/v1/owners/{id}", nonExistentId))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Tests for Owner Deletion (DELETE /v1/owners/{id})")
    class DeleteOwnerTests {

        @Test
        @DisplayName("Should delete owner successfully and return 204 No Content")
        void shouldDeleteOwnerWithSuccess() throws Exception {
            // Arrange
            var ownerId = UUID.randomUUID();
            doNothing().when(deleteOwnerUseCase).delete(ownerId);

            // Act & Assert
            mockMvc.perform(delete("/v1/owners/{id}", ownerId))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Should return 404 Not Found when trying to delete a non-existent owner")
        void shouldReturn404WhenOwnerToDeleteDoesNotExist() throws Exception {
            // Arrange
            var ownerId = UUID.randomUUID();
            doThrow(new UserNotFoundException("Owner not found"))
                    .when(deleteOwnerUseCase).delete(ownerId);

            // Act & Assert
            mockMvc.perform(delete("/v1/owners/{id}", ownerId))
                    .andExpect(status().isNotFound());
        }

    }

    @Nested
    @DisplayName("Tests for Owner Update (PUT /v1/owners/{id})")
    class UpdateOwnerTests {

        @Test
        @DisplayName("Should update owner details successfully and return 200 OK")
        void shouldUpdateOwnerWithSuccess() throws Exception {
            // Arrange
            var ownerId = UUID.randomUUID();
            var lastUpdate = LocalDate.now();
            var putRequest = new OwnerDTO.PutRequest("Michael Gary Scott", "11888888888");

            var updatedOwner = new Owner(ownerId, "michael.scott@dundermifflin.com", "Michael Gary Scott", lastUpdate, "11888888888");

            when(updateOwnerUseCase.update(any(), eq(ownerId))).thenReturn(updatedOwner);

            // Act & Assert
            mockMvc.perform(put("/v1/owners/{id}", ownerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(putRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(ownerId.toString()))
                    .andExpect(jsonPath("$.name").value("Michael Gary Scott"))
                    .andExpect(jsonPath("$.businessPhone").value("11888888888"));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when update payload contains empty fields")
        void shouldReturn400WhenUpdatePayloadIsInvalid() throws Exception {
            // Arrange
            var ownerId = UUID.randomUUID();
            var invalidPutRequest = new OwnerDTO.PutRequest("", "   ");

            // Act & Assert
            mockMvc.perform(put("/v1/owners/{id}", ownerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidPutRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 404 Not Found when trying to update an unexisting owner")
        void shouldReturn404WhenOwnerToUpdateDoesNotExist() throws Exception {
            // Arrange
            var ownerId = UUID.randomUUID();
            var putRequest = new OwnerDTO.PutRequest("Michael Gary Scott", "11888888888");

            when(updateOwnerUseCase.update(any(), eq(ownerId)))
                    .thenThrow(new UserNotFoundException("Owner not found"));

            // Act & Assert
            mockMvc.perform(put("/v1/owners/{id}", ownerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(putRequest)))
                    .andExpect(status().isNotFound());
        }
    }
}