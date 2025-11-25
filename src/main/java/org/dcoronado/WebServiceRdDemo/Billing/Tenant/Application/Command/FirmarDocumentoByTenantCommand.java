package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command;

import lombok.Builder;

import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Assert.notBlank;

@Builder
public record FirmarDocumentoByTenantCommand(
        String rnc,
        String nombreDocumento,
        byte[] documento
) {
    public FirmarDocumentoByTenantCommand {
        notBlank(rnc, "RNC required");
        notBlank(nombreDocumento, "nombreDocumento required");
    }
}
