package org.dcoronado.WebServiceRdDemo.Billing.Sesion.Infraestructure.Persistence;

import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Domain.Sesion;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.RNC;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;


@Component
public class SesionMapper {
    public SesionEntity toEntity(Sesion sesion) {
        if (sesion == null) return null;
        SesionEntity sesionEntity = new SesionEntity();
        sesionEntity.setId(sesion.getId());
        sesionEntity.setTenantRnc(sesion.getRnc().getValor());
        sesionEntity.setAmbiente(sesion.getAmbiente());
        sesionEntity.setTokenDgii(sesion.getToken());
        sesionEntity.setFechaTokenExpedidoDgii(sesion.getExpedido().toLocalDateTime()); // Convertimos UTC a LocalDateDime
        sesionEntity.setFechaTokenExpiraDgii(sesion.getExpira().toLocalDateTime());
        return sesionEntity;
    }

    public Sesion toDomain(SesionEntity sesionEntity) {
        if (sesionEntity == null) return null;
        return Sesion.reconstruir(
                sesionEntity.getId(),
                RNC.reconstructFromDatabase(sesionEntity.getTenantRnc()),
                sesionEntity.getAmbiente(),
                sesionEntity.getFechaTokenExpedidoDgii() != null ? sesionEntity.getFechaTokenExpedidoDgii().atOffset(ZoneOffset.UTC) : null,
                sesionEntity.getFechaTokenExpiraDgii() != null ? sesionEntity.getFechaTokenExpiraDgii().atOffset(ZoneOffset.UTC) : null,
                sesionEntity.getTokenDgii()
        );
    }
}
