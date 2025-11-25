package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command;

import lombok.Builder;

import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Assert.notBlank;

@Builder
public record SetupBDTenantCommand(
        String rnc,
        String host,
        String puerto
) {
    public SetupBDTenantCommand {
        notBlank(rnc, "RNC required");
        notBlank(host, "Host requerido");
        notBlank(puerto, "Puerto requerido");
    }
}
