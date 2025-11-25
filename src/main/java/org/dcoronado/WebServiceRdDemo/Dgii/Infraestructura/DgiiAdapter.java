package org.dcoronado.WebServiceRdDemo.Dgii.Infraestructura;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcoronado.WebServiceRdDemo.Dgii.Aplication.Dto.InfoRecepcionEcfDgii;
import org.dcoronado.WebServiceRdDemo.Dgii.Aplication.Dto.InfoRecepcionRcefDgii;
import org.dcoronado.WebServiceRdDemo.Dgii.Aplication.Dto.InfoTokenDgii;
import org.dcoronado.WebServiceRdDemo.Dgii.Aplication.Port.Out.DgiiPort;
import org.dcoronado.WebServiceRdDemo.Dgii.Infraestructura.Dto.InfoRecepcionComprobanteDgiiResponse;
import org.dcoronado.WebServiceRdDemo.Dgii.Infraestructura.Dto.InfoRecepcionComprobanteResumenDgiiResponse;
import org.dcoronado.WebServiceRdDemo.Dgii.Infraestructura.Dto.InfoSesionDgiiResponseDTO;
import org.dcoronado.WebServiceRdDemo.Dgii.Infraestructura.Http.DgiiApi;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DgiiAdapter implements DgiiPort {

    private final DgiiApi dgiiApi;

    @Override
    public String obtenerSemilla(AmbienteEnum ambiente) {
        return dgiiApi.obtenerSemilla(ambiente);
    }

    @Override
    public InfoTokenDgii validarSemilla(AmbienteEnum ambiente, byte[] xmlSemillaFirmada) {
        InfoSesionDgiiResponseDTO infoSesionDgiiResponseDTO =  dgiiApi.validarSemilla(ambiente, xmlSemillaFirmada);
        return new InfoTokenDgii(
                infoSesionDgiiResponseDTO.token(),
                infoSesionDgiiResponseDTO.expedido(),
                infoSesionDgiiResponseDTO.expira()
        );
    }

    @Override
    public InfoRecepcionEcfDgii recepcionECF(AmbienteEnum ambiente, String token, byte[] xmlComprobante) {
        InfoRecepcionComprobanteDgiiResponse infoRecepcionComprobanteDgiiResponse = dgiiApi.recepcionComprobanteDgii(ambiente, token, xmlComprobante);
        return new InfoRecepcionEcfDgii(
                infoRecepcionComprobanteDgiiResponse.trackId(),
                infoRecepcionComprobanteDgiiResponse.error(),
                infoRecepcionComprobanteDgiiResponse.mensaje()
        );
    }

    @Override
    public InfoRecepcionRcefDgii recepcionResumenECF(AmbienteEnum ambiente, String token, byte[] xmlComprobanteResumen) {
        InfoRecepcionComprobanteResumenDgiiResponse response = dgiiApi.recepcionComprobanteResumenDgii(ambiente, token, xmlComprobanteResumen);

        // Si el response es null, devolvemos un objeto vacío por defecto
        if (response == null)
            return new InfoRecepcionRcefDgii(0, null, List.of(), null, false);

        // Mapear mensajesDgii de forma null-safe
        List<InfoRecepcionRcefDgii.Mensaje> mensajes = response.mensajes() == null
                ? List.of()
                : response.mensajes().stream()
                .map(m -> new InfoRecepcionRcefDgii.Mensaje(m.codigo(), m.valor()))
                .toList();

        // Construir objeto interno directamente
        return new InfoRecepcionRcefDgii(
                response.codigo(),
                response.estado(),
                mensajes,
                response.encf(),
                response.secuenciaUtilizada()
        );
    }
}
