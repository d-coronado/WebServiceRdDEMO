package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.Rest.Dto.Request;

import jakarta.validation.constraints.NotBlank;

public record TenantSetupBDRequestDto(
        @NotBlank String rnc,
        @NotBlank String host,
        @NotBlank String port
) {
}
