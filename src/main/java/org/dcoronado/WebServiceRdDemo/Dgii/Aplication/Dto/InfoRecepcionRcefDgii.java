package org.dcoronado.WebServiceRdDemo.Dgii.Aplication.Dto;

import java.util.List;

public record InfoRecepcionRcefDgii(
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
