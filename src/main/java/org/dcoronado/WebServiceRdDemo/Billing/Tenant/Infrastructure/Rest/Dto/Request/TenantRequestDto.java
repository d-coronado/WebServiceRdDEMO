package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.Rest.Dto.Request;

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
        @NotBlank String ambiente
) {
}

