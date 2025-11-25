package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.In;

import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command.UploadCertificadoDigitalTenantCommand;

import java.io.IOException;

public interface UploadCertificadoByTenantUseCase {
    void execute(UploadCertificadoDigitalTenantCommand command) throws IOException;
}
