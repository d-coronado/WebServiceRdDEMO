package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Service;

import lombok.RequiredArgsConstructor;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.In.GetTenantUseCase;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.TenantRepositoryPort;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model.Tenant;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetTenantService implements GetTenantUseCase {

    private final TenantRepositoryPort tenantRepositoryPort;

    @Override
    public Optional<Tenant> findById(Long id) {
        return tenantRepositoryPort.findById(id);
    }

    @Override
    public Optional<Tenant> finByRnc(String rnc) {
        return tenantRepositoryPort.findByRnc(rnc);
    }
}
