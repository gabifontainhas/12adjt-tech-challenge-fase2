package br.com.gabifontainhas.techchallenge.infrastructure.web.dto;

import br.com.gabifontainhas.techchallenge.application.usecase.dto.CreateCustomerCommand;
import br.com.gabifontainhas.techchallenge.application.usecase.dto.UpdateCustomerCommand;
import br.com.gabifontainhas.techchallenge.domain.entity.Customer;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.UUID;


public final class CustomerDTO {
    private CustomerDTO() {
    }

    public record PostRequest(
            @NotBlank
            String email,

            @NotBlank
            String name,

            @NotBlank
            String phoneNumber

    ) {
        public CreateCustomerCommand toCommand() {
            return new CreateCustomerCommand(this.email, this.name, this.phoneNumber);
        }
    }

    public record PutRequest(

            @NotBlank
            String name,

            @NotBlank
            String phoneNumber
    ) {
        public UpdateCustomerCommand toCommand() {
            return new UpdateCustomerCommand(this.name, this.phoneNumber);
        }
    }

    public record Response(
            UUID id,
            String email,
            String name,
            LocalDate lastUpdate,
            String phoneNumber
    ) {
        public Response(Customer c) {
            this(c.getId(), c.getEmail(), c.getName(), c.getLastUpdate(), c.getPhoneNumber());
        }
    }
}
