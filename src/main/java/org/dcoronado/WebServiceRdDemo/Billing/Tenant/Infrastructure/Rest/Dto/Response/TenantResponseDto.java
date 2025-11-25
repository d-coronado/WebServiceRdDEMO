package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.Rest.Dto.Response;

import lombok.Builder;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.StatusEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;

@Builder
public record TenantResponseDto(
        Long id,
        String rncEmpresa,
        String razonSocial,
        String direccionFiscal,
        String alias,
        String nombreContacto,
        String telefonoContacto,
        AmbienteEnum ambiente,
        StatusEnum setupBdStatus,
        StatusEnum setupDirectoriesStatus,
        Boolean isActive
) {
}
