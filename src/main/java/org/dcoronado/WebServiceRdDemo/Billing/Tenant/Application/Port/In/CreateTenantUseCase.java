package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.In;

import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command.CreateTenantCommand;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model.Tenant;

public interface CreateTenantUseCase {
    Tenant createTenant(CreateTenantCommand command);
}
