package org.dcoronado.WebServiceRdDemo.Dgii.Infraestructura.Http;

import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;
import org.springframework.stereotype.Component;

import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Assert.required;

@Component
public class DgiiEnvironments {

    // === Bases específicas por tipo ===
    private static final String BASE_COMPROBANTE = "https://ecf.dgii.gov.do";
    private static final String BASE_COMPROBANTE_RESUMEN = "https://fc.dgii.gov.do";

    public static final String GENERAR_SEMILLA = "/autenticacion/api/autenticacion/semilla";
    public static final String VALIDAR_SEMILLA = "/autenticacion/api/autenticacion/validarsemilla";
    public static final String ENVIA_COMPROBANTE = "/recepcion/api/facturaselectronicas";
    public static final String ENVIA_COMPROBANTE_RESUMEN = "/recepcionfc/api/recepcion/ecf";

    public static final String SERVICE_NOT_FOUND = "SERVICIO CON ERROR ";
    public static final String BEARER_TOKEN_FORMAT = "bearer ";

    private DgiiEnvironments() {}

    // Devuelve la parte del path según el ambiente
    private String getAmbientePath(AmbienteEnum ambiente) {
        required(ambiente.getUrlPathDgii() != null,"El ambiente no tiene una URL configurada: " + ambiente.name());
        return ambiente.getUrlPathDgii();
    }

    private String buildEndpoint(String base, AmbienteEnum ambiente, String endpoint) {
        return base + "/" + getAmbientePath(ambiente) + endpoint;
    }

    public String getGenerarSemillaEndpoint(AmbienteEnum ambiente) {
        return BASE_COMPROBANTE + "/" + getAmbientePath(ambiente) + GENERAR_SEMILLA;
    }

    public String getValidarSemillaEndpoint(AmbienteEnum ambiente) {
        return BASE_COMPROBANTE + "/" + getAmbientePath(ambiente) + VALIDAR_SEMILLA;
    }

    public String getEnviaComprobanteEndpoint(AmbienteEnum ambiente) {
        return buildEndpoint(BASE_COMPROBANTE, ambiente, ENVIA_COMPROBANTE);
    }

    public String getEnviaComprobanteResumenEndpoint(AmbienteEnum ambiente) {
        return buildEndpoint(BASE_COMPROBANTE_RESUMEN, ambiente, ENVIA_COMPROBANTE_RESUMEN);
    }
}
