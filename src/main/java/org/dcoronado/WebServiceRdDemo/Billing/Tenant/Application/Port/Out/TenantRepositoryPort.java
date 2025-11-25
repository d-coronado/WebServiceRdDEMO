package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out;

import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model.Tenant;

import java.util.Optional;

public interface TenantRepositoryPort {
    Tenant save(Tenant tenant);

    Optional<Tenant> findById(Long id);

    Optional<Tenant> findByRnc(String rnc);
}
