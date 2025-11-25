package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.Persistence;

import lombok.RequiredArgsConstructor;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.TenantRepositoryPort;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model.Tenant;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaTenantRepositoryAdapter implements TenantRepositoryPort {

    private final SpringDataTenantRepository springDataTenantRepository;
    private final TenantMapper tenantMapper;

    @Override
    public Tenant save(Tenant tenant) {
        TenantEntity tenantEntity = tenantMapper.toEntity(tenant);
        final TenantEntity savedTenant = springDataTenantRepository.save(tenantEntity);
        return tenantMapper.toDomain(savedTenant);
    }


    @Override
    public Optional<Tenant> findById(Long id) {
        return springDataTenantRepository.findById(id)
                .map(tenantMapper::toDomain);
    }

    @Override
    public Optional<Tenant> findByRnc(String rnc) {
        return springDataTenantRepository.findByRnc(rnc)
                .map(tenantMapper::toDomain);
    }

}
