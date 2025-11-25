package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.In;

import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command.SetupBDTenantCommand;

public interface SetupDatabaseTenantUseCase {
    void execute(SetupBDTenantCommand command);
}
