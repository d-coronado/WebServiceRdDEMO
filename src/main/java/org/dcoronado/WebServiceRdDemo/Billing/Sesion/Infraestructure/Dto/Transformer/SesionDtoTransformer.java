package org.dcoronado.WebServiceRdDemo.Billing.Sesion.Infraestructure.Dto.Transformer;

import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Domain.Sesion;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Infraestructure.Dto.Response.SesionResponseDto;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Dto.Transformer.DtoTransformer;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.InvalidArgumentException;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class SesionDtoTransformer extends DtoTransformer<SesionResponseDto, Sesion> {

    @Override
    public SesionResponseDto fromObject(Sesion sesion) {
        if (isNull(sesion)) throw new InvalidArgumentException("Tenant no puede ser null");
        return new SesionResponseDto(
                sesion.getId(),
                sesion.getRnc().getValor(),
                sesion.getAmbiente(),
                sesion.getExpedido(),
                sesion.getExpira(),
                sesion.getToken()
        );
    }
}