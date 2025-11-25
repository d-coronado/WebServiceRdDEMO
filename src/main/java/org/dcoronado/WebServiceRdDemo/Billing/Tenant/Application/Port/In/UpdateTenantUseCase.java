package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.In;

import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command.UpdateTenantCommand;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model.Tenant;

public interface UpdateTenantUseCase {
    Tenant updateTennat(UpdateTenantCommand command);
}
