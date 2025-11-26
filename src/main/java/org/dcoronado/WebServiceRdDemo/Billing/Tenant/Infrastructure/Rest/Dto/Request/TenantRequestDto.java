package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.Rest.Dto.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TenantRequestDto(
        @NotBlank String rnc,
        @NotBlank String razonSocial,
        @NotBlank String direccionFiscal,
        String alias,
        String nombreContacto,
        String telefonoContacto,
        @Schema(description = "Código de ambiente: 1=PRUEBAS, 2=CERTIFICACION, 3=PRODUCCION",
                example = "1",
                allowableValues = {"1", "2", "3"})
        @NotBlank String ambiente
) {
}

