package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.Rest;

import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model.Tenant;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.Rest.Dto.Response.TenantResponseDto;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Dto.Transformer.DtoTransformer;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.InvalidArgumentException;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class TenantDtoTransformer extends DtoTransformer<TenantResponseDto, Tenant> {

    @Override
    public TenantResponseDto fromObject(Tenant tenant) {
        if (isNull(tenant)) throw new InvalidArgumentException("Tenant no puede ser null");

        return TenantResponseDto.builder()
                .id(tenant.getId())
                .rncEmpresa(tenant.getRnc().getValor())
                .razonSocial(tenant.getRazonSocial())
                .direccionFiscal(tenant.getDireccionFiscal())
                .alias(tenant.getAlias())
                .nombreContacto(tenant.getNombreContacto())
                .telefonoContacto(tenant.getTelefonoContacto())
                .ambiente(tenant.getAmbiente())
                .setupBdStatus(tenant.getDbSetupStatus())
                .setupDirectoriesStatus(tenant.getDirectoriesSetupStatus())
                .isActive(tenant.getIsActive())
                .build();
    }
}
