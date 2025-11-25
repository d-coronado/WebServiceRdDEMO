package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.Dto;

import lombok.Builder;

@Builder
public record DbConnectionInfo(
        String urlConexionBd,
        String usuarioBd,
        String passwordBd
) {
}
