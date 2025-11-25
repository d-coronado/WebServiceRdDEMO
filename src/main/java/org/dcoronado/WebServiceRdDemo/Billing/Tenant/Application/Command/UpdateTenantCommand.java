package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command;

import lombok.Builder;

import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Assert.notBlank;
import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Assert.required;

@Builder
public record UpdateTenantCommand(
        Long tenantId,
        String rnc,
        String razonSocial,
        String direccionFiscal,
        String alias,
        String nombreContacto,
        String telefono,
        String ambiente
) {

    public UpdateTenantCommand {
        required(tenantId,"Tenant ID required");
        notBlank(rnc,"RNC required");
        notBlank(direccionFiscal,"Direccion Fiscal required");
        notBlank(ambiente,"Ambiente required");
    }
}
