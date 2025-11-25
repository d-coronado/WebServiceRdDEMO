package org.dcoronado.WebServiceRdDemo.Dgii.Infraestructura.Http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcoronado.WebServiceRdDemo.Dgii.Infraestructura.Dto.InfoRecepcionComprobanteDgiiResponse;
import org.dcoronado.WebServiceRdDemo.Dgii.Infraestructura.Dto.InfoRecepcionComprobanteResumenDgiiResponse;
import org.dcoronado.WebServiceRdDemo.Dgii.Infraestructura.Dto.InfoSesionDgiiResponseDTO;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.InfrastructureException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.dcoronado.WebServiceRdDemo.Dgii.Infraestructura.Http.DgiiEnvironments.BEARER_TOKEN_FORMAT;

@Component
@RequiredArgsConstructor
@Slf4j
public class DgiiApi {

    private final RestClient.Builder restClientBuilder;
    private final DgiiEnvironments dgiiEnvironments;

    public String obtenerSemilla(AmbienteEnum ambiente) {

        RestClient restClient = restClientBuilder.build();
        try {
            return restClient.get()
                    .uri(dgiiEnvironments.getGenerarSemillaEndpoint(ambiente))
                    .header("Accept", "*/*")
                    .retrieve()
                    .body(String.class);

        }catch (Exception e){
            log.error("Error obteniendo semilla DGII", e);
            throw new RuntimeException("DGII request failed", e);
        }
    }

    public InfoSesionDgiiResponseDTO validarSemilla(AmbienteEnum ambiente, byte[] xmlSemillaFirmada) {
        RestClient restClient = restClientBuilder.build();

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder
                .part("xml", new ByteArrayResource(xmlSemillaFirmada){
                    @Override
                    public String getFilename() {
                        return "semillaFirmada.xml";
                    }})
                .contentType(MediaType.TEXT_XML);

        try {
            return restClient.post()
                    .uri(dgiiEnvironments.getValidarSemillaEndpoint(ambiente))
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(bodyBuilder.build())
                    .retrieve()
                    .body(InfoSesionDgiiResponseDTO.class);

        } catch (Exception e) {
            log.error("Error enviando comprobante DGII", e);
            throw new RuntimeException("DGII request failed", e);
        }
    }

    public InfoRecepcionComprobanteDgiiResponse recepcionComprobanteDgii(AmbienteEnum ambiente, String token, byte[] xmlComprobante){

        RestClient restClient = restClientBuilder
                .requestInterceptor((request, body, execution) ->
                        execution.execute(request, body))
                .build();

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("xml", new ByteArrayResource(xmlComprobante) {
                    @Override
                    public String getFilename() {
                        return "comprobante.xml";
                    }
                })
                .contentType(MediaType.TEXT_XML);

        try {
            return restClient.post()
                    .uri(dgiiEnvironments.getEnviaComprobanteEndpoint(ambiente))
                    .header("Authorization", BEARER_TOKEN_FORMAT.concat(token))
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(bodyBuilder.build())
                    .retrieve()
                    .body(InfoRecepcionComprobanteDgiiResponse.class);

        }  catch (HttpClientErrorException e) {
            // 4xx con body JSON válido → parsear igual
            try {
                String json = e.getResponseBodyAsString();
                return new ObjectMapper().readValue(json, InfoRecepcionComprobanteDgiiResponse.class);
            } catch (Exception parseError) {
                throw new InfrastructureException("Respuesta DGII inválida (no es JSON válido)", parseError);
            }
        } catch (Exception e) {
            log.error("Error enviando comprobante DGII", e);
            // Errores reales: timeout, red, etc.
            throw new InfrastructureException("No se pudo comunicar con DGII", e);
        }
    }

    public InfoRecepcionComprobanteResumenDgiiResponse recepcionComprobanteResumenDgii(AmbienteEnum ambiente, String token, byte[] xmlComprobanteResumen){
        RestClient restClient = restClientBuilder
                .requestInterceptor((request, body, execution) ->
                        execution.execute(request, body))
                .build();

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder
                .part("xml", new ByteArrayResource(xmlComprobanteResumen) {
                    @Override
                    public String getFilename() {
                        return "comprobante-resumen.xml";
                    }
                })
                .contentType(MediaType.TEXT_XML);

        try {
            return restClient.post()
                    .uri(dgiiEnvironments.getEnviaComprobanteResumenEndpoint(ambiente))
                    .header("Authorization", BEARER_TOKEN_FORMAT.concat(token))
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(bodyBuilder.build())
                    .retrieve()
                    .body(InfoRecepcionComprobanteResumenDgiiResponse.class);

        } catch (HttpClientErrorException e) {
            // 4xx con body JSON válido → parsear igual
            try {
                String json = e.getResponseBodyAsString();
                return new ObjectMapper().readValue(json, InfoRecepcionComprobanteResumenDgiiResponse.class);
            } catch (Exception parseError) {
                throw new InfrastructureException("Respuesta DGII inválida (no es JSON válido)", parseError);
            }
        } catch (Exception e) {
            log.error("Error enviando comprobante DGII", e);
            // Errores reales: timeout, red, etc.
            throw new InfrastructureException("No se pudo comunicar con DGII", e);
        }
    }


}
