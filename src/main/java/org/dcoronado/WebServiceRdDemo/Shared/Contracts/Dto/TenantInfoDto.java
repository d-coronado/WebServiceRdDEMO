package org.dcoronado.WebServiceRdDemo.Shared.Contracts.Dto;

import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.ValueObject.CertificadoDigital;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;

public record TenantInfoDto(
        String rnc,
        CertificadoDigital certificadoDigital,
        AmbienteEnum limitAccessAmbiente,
        String razonSocial,
        String direccionFiscal
) {
}
