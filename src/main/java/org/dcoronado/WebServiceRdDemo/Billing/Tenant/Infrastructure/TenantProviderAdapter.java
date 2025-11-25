package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure;

import lombok.RequiredArgsConstructor;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.In.GetTenantUseCase;
import org.dcoronado.WebServiceRdDemo.Shared.Contracts.Dto.TenantInfoDto;
import org.dcoronado.WebServiceRdDemo.Shared.Contracts.Port.TenantProvider;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.NotFoundException;
import org.springframework.stereotype.Component;

import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Assert.notBlank;

@Component
@RequiredArgsConstructor
public class TenantProviderAdapter implements TenantProvider {

    private final GetTenantUseCase getTenantUseCase;

    @Override
    public TenantInfoDto getTenantInfoByRnc(String rnc) {
        notBlank(rnc, "Rnc Requerido");

        return getTenantUseCase.finByRnc(rnc)
                .map(tenant -> new TenantInfoDto(
                        tenant.getRnc().getValor(),
                        tenant.getCertificadoDigital(),
                        tenant.getAmbiente(),
                        tenant.getRazonSocial(),
                        tenant.getDireccionFiscal()
                ))
                .orElseThrow(() -> new NotFoundException("Tenant con RNC " + rnc + " no encontrado"));
    }
}
