package org.dcoronado.WebServiceRdDemo.Billing.Sesion.Infraestructure.Dto.Request;

import jakarta.validation.constraints.NotBlank;

public record CreateSesionRequestDto(
        @NotBlank String rnc,
        @NotBlank String ambiente
) {
}
