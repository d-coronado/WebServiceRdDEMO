package org.dcoronado.WebServiceRdDemo.Dgii.Infraestructura.Dto;

import java.util.List;

public record InfoRecepcionComprobanteResumenDgiiResponse(
        int codigo,
        String estado,
        List<Mensaje> mensajes,
        String encf,
        boolean secuenciaUtilizada
) {
    public record Mensaje(
            String codigo,
            String valor
    ) {
    }
}
