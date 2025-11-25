package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command;

import lombok.Builder;

import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Assert.notBlank;

@Builder
public record UploadCertificadoDigitalTenantCommand(
        String rnc,
        String nombreCertificado,
        byte[] certificadoDigitalContenido,
        String claveCertificado
) {
    public UploadCertificadoDigitalTenantCommand {
        notBlank(rnc,"RNC required");
        notBlank(nombreCertificado,"nombreCertificado required");
        notBlank(claveCertificado,"claveCertificado required");
    }
}
