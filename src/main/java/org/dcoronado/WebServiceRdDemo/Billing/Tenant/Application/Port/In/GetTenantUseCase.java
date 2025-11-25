package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.In;

import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model.Tenant;

import java.util.Optional;

public interface GetTenantUseCase {
    Optional<Tenant> findById(Long id);

    Optional<Tenant> finByRnc(String rnc);
}
