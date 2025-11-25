package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command;

import lombok.Builder;

import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Assert.notBlank;

@Builder
public record CreateTenantCommand(
        String rnc,
        String razonSocial,
        String direccionFiscal,
        String alias,
        String nombreContacto,
        String telefono,
        String ambiente
) {
    public CreateTenantCommand {
        notBlank(rnc,"RNC required");
        notBlank(direccionFiscal,"Direccion Fiscal required");
        notBlank(ambiente,"Ambiente required");
    }
}