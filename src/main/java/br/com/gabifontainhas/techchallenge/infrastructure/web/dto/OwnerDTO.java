package br.com.gabifontainhas.techchallenge.infrastructure.web.dto;

import br.com.gabifontainhas.techchallenge.application.usecases.dto.CreateOwnerCommand;
import br.com.gabifontainhas.techchallenge.application.usecases.dto.UpdateOwnerCommand;
import br.com.gabifontainhas.techchallenge.domain.entities.Owner;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.UUID;

public final class OwnerDTO {
    public record PostRequest(

            @NotBlank
            String email,

            @NotBlank
            String name,

            @NotBlank
            String businessPhone
    ) {
        public CreateOwnerCommand toCommand() {
            return new CreateOwnerCommand(this.name, this.email, this.businessPhone);
        }
    }

    public record PutRequest(

            @NotBlank
            String name,

            @NotBlank
            String businessPhone
    ) {

        public UpdateOwnerCommand toCommand() {
            return new UpdateOwnerCommand(this.name, this.businessPhone);
        }
    }

    public record Response(
            UUID id,
            String email,
            String name,
            LocalDate lastUpdate,
            String businessPhone
    ) {
        public Response(Owner o) {
            this(o.getId(), o.getEmail(), o.getName(), o.getLastUpdate(), o.getBusinessPhone());
        }
    }

}
