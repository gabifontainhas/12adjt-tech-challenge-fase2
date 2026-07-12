package br.com.gabifontainhas.techchallenge.infrastructure.web.unit;

import br.com.gabifontainhas.techchallenge.application.exception.EmailAlreadyExistsException;
import br.com.gabifontainhas.techchallenge.application.exception.UserNotFoundException;
import br.com.gabifontainhas.techchallenge.application.usecases.customer.*;
import br.com.gabifontainhas.techchallenge.domain.entities.Customer;
import br.com.gabifontainhas.techchallenge.infrastructure.web.dto.CustomerDTO;
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
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateCustomerUseCase createCustomerUseCase;

    @MockitoBean
    private ListCustomersUseCase listCustomersUseCase;

    @MockitoBean
    private ListCustomerByIdUseCase listCustomerByIdUseCase;

    @MockitoBean
    private DeleteCustomerUseCase deleteCustomerUseCase;

    @MockitoBean
    private UpdateCustomerUseCase updateCustomerUseCase;

    @Nested
    @DisplayName("Tests for Customer Creation (POST /v1/customers)")
    class CreateCustomerTests {

        @Test
        @DisplayName("Should create customer successfully and return 201 Created")
        void shouldCreateCustomerWithSuccess() throws Exception {

            // Arrange
            var request = new CustomerDTO.PostRequest("jimhalpert@dundermifflin.com", "Jim Halpert", "11999999999");
            var createdCustomer = new Customer("jimhalpert@dundermifflin.com", "Jim Halpert", "11999999999");


            when(createCustomerUseCase.create(any())).thenReturn(createdCustomer);

            // Act & Assert
            mockMvc.perform(post("/v1/customers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value("Jim Halpert"))
                    .andExpect(jsonPath("$.email").value("jimhalpert@dundermifflin.com"))
                    .andExpect(jsonPath("$.phoneNumber").value("11999999999"));

        }

        @Test
        @DisplayName("Should return 400 Bad Request when mandatory fields are empty (Validation fails)")
        void shouldReturn400WhenPayloadIsInvalid() throws Exception {
            var invalidRequest = new CustomerDTO.PostRequest("", "Jim Halpert", "11999999999");

            // Act & Assert
            mockMvc.perform(post("/v1/customers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            Mockito.verifyNoInteractions(createCustomerUseCase);
        }


        @Test
        @DisplayName("Should return 422 Unprocessable Content when e-mail already exists")
        void shouldReturn422WhenEmailAlreadyExists() throws Exception {
            // Arrange
            var request = new CustomerDTO.PostRequest("jimhalpert@dundermifflin.com", "Jim Halpert", "11999999999");

            when(createCustomerUseCase.create(any())).thenThrow(new EmailAlreadyExistsException("Email already exists"));

            // Act & Assert
            mockMvc.perform(post("/v1/customers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnprocessableContent());
        }
    }

    @Nested
    @DisplayName("Tests for Listing Customers (GET /v1/customers)")
    class ListCustomersTests {

        @Test
        @DisplayName("Should return list of customers and 200 OK")
        void shouldReturnListOfCustomersWithSuccess() throws Exception {
            // Arrange
            var customer1 = new Customer("jim.halpert@dundermifflin.com", "Jim Halpert", "11999999999");
            var customer2 = new Customer("michael.scott@dundermifflin.com", "Michael Scott", "11888888888");

            when(listCustomersUseCase.getAllCustomers()).thenReturn(List.of(customer1, customer2));

            // Act & Assert
            mockMvc.perform(get("/v1/customers"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))

                    .andExpect(jsonPath("$[0].name").value("Jim Halpert"))
                    .andExpect(jsonPath("$[0].email").value("jim.halpert@dundermifflin.com"))
                    .andExpect(jsonPath("$[0].phoneNumber").value("11999999999"))

                    .andExpect(jsonPath("$[1].name").value("Michael Scott"))
                    .andExpect(jsonPath("$[1].email").value("michael.scott@dundermifflin.com"))
                    .andExpect(jsonPath("$[1].phoneNumber").value("11888888888"));

        }

        @Test
        @DisplayName("Should return a specific customer by ID with 200 OK")
        void shouldReturnCustomerByIdWithSuccess() throws Exception {
            // Arrange
            var customerId = UUID.randomUUID();
            var lastUpdate = LocalDate.now();

            var customer = new Customer(customerId, "jim.halpert@dundermifflin.com", "Jim Halpert", lastUpdate, "11999999999");

            when(listCustomerByIdUseCase.getCustomersById(customerId)).thenReturn(customer);

            // Act & Assert
            mockMvc.perform(get("/v1/customers/{id}", customerId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(customerId.toString()))
                    .andExpect(jsonPath("$.name").value("Jim Halpert"))
                    .andExpect(jsonPath("$.email").value("jim.halpert@dundermifflin.com"))
                    .andExpect(jsonPath("$.phoneNumber").value("11999999999"))
                    .andExpect(jsonPath("$.lastUpdate").value(lastUpdate.toString()));
        }

        @Test
        @DisplayName("Should return 404 Not Found when searching for a non-existent customer ID")
        void shouldReturn404WhenCustomerDoesNotExist() throws Exception {
            // Arrange
            var nonExistentId = UUID.randomUUID();
            when(listCustomerByIdUseCase.getCustomersById(nonExistentId))
                    .thenThrow(new UserNotFoundException("Customer not found"));

            // Act & Assert
            mockMvc.perform(get("/v1/customers/{id}", nonExistentId))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Tests for Customer Deletion (DELETE /v1/customers/{id})")
    class DeleteCustomerTests {

        @Test
        @DisplayName("Should delete customer successfully and return 204 No Content")
        void shouldDeleteCustomerWithSuccess() throws Exception {
            // Arrange
            var customerId = UUID.randomUUID();
            doNothing().when(deleteCustomerUseCase).delete(customerId);

            // Act & Assert
            mockMvc.perform(delete("/v1/customers/{id}", customerId))
                    .andExpect(status().isNoContent());

        }

        @Test
        @DisplayName("Should return 404 Not Found when searching for a non-existent customer ID to delete")
        void shouldReturn404WhenCustomerToDeleteDoesNotExist() throws Exception {
            // Arrange
            var customerId = UUID.randomUUID();
            doThrow(new UserNotFoundException("Customer not found"))
                    .when(deleteCustomerUseCase).delete(customerId);

            // Act & Assert
            mockMvc.perform(delete("/v1/customers/{id}", customerId))
                    .andExpect(status().isNotFound());

        }
    }

    @Nested
    @DisplayName("Tests for Customer Update (PUT /v1/customers/{id})")
    class UpdateCustomerTests {

        @Test
        @DisplayName("Should update customer successfully and return 200 OK")
        void shouldUpdateCustomerWithSuccess() throws Exception {
            // Arrange
            var customerId = UUID.randomUUID();
            var putRequest = new CustomerDTO.PutRequest("James Halpert", "11988888888");
            var updatedCustomer = new Customer(customerId, "jim.halpert@dundermifflin.com", "James Halpert", LocalDate.now(),"11988888888");

            when(updateCustomerUseCase.update(any(), eq(customerId))).thenReturn(updatedCustomer);

            // Act & Assert
            mockMvc.perform(put("/v1/customers/{id}", customerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(putRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(customerId.toString()))
                    .andExpect(jsonPath("$.name").value("James Halpert"))
                    .andExpect(jsonPath("$.phoneNumber").value("11988888888"));

        }

        @Test
        @DisplayName("Should return 400 Bad Request when update payload contains empty/blank fields")
        void shouldReturn400WhenUpdatePayloadIsInvalid() throws Exception {
            // Arrange
            var customerId = UUID.randomUUID();
            var invalidPutRequest = new CustomerDTO.PutRequest("   ", "");

            // Act & Assert
            mockMvc.perform(put("/v1/customers/{id}", customerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidPutRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 404 Not Found when searching for a non-existent customer ID to update")
        void shouldReturn404WhenCustomerToUpdateDoesNotExist() throws Exception {
            // Arrange
            var customerId = UUID.randomUUID();
            var putRequest = new CustomerDTO.PutRequest("James Halpert", "11988888888");

            when(updateCustomerUseCase.update(any(), eq(customerId)))
                    .thenThrow(new UserNotFoundException("Customer not found"));

            // Act & Assert
            mockMvc.perform(put("/v1/customers/{id}", customerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(putRequest)))
                    .andExpect(status().isNotFound());
        }
    }
}