package org.dcoronado.WebServiceRdDemo.Billing.Sesion.Infraestructure.Dto.Response;

import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;

import java.time.OffsetDateTime;

public record SesionResponseDto(
        Long id,
        String rnc,
        AmbienteEnum ambiente,
        OffsetDateTime expedido,
        OffsetDateTime expira,
        String token
) {
}
