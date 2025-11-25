package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.Persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface SpringDataTenantRepository extends JpaRepository<TenantEntity, Long> {
    @Query("SELECT l FROM TenantEntity l WHERE l.rnc = :rncTenant")
    Optional<TenantEntity> findByRnc(@Param("rncTenant") String rnc);
}
