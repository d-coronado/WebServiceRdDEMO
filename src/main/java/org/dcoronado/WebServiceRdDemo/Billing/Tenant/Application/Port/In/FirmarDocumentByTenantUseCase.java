package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.In;

import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command.FirmarDocumentoByTenantCommand;

public interface FirmarDocumentByTenantUseCase {
    String firmarDocumentByTenant(FirmarDocumentoByTenantCommand command) throws Exception;
}
