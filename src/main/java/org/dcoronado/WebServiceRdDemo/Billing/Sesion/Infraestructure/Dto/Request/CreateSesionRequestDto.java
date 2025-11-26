package org.dcoronado.WebServiceRdDemo.Billing.Sesion.Infraestructure.Dto.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateSesionRequestDto(
        @NotBlank String rnc,
        @Schema(description = "Código de ambiente: 1=PRUEBAS, 2=CERTIFICACION, 3=PRODUCCION",
                example = "1",
                allowableValues = {"1", "2", "3"})
        @NotBlank String ambiente
) {
}
