package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.Rest.Dto.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record TenantSetupBDRequestDto(

        @Schema(description = "RNC del tenant que será configurado", example = "123456789")
        @NotBlank String rnc,

        @Schema(description = "Host del motor de base de datos.En ambiente local usar: 127.0.0.1 En Docker usar: nombre del servicio definido en docker-compose (ej.: mariadb-bd)",
                example = "mariadb-bd"
        )
        @NotBlank String host,

        @Schema(description = "Puerto del motor de base de datos", example = "3306")
        @NotBlank String port
) {
}
