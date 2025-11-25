package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain;

import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.ContextoArchivoEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.TipoComprobanteTributarioEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.TipoOperacionArchivoTenantEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RutasDirectoriosTenantTest {

    private static final String Rnc = "123456789";

    @DisplayName("getRutaCertificadoDigital debe construir la ruta correcta dado un Rnc")
    @Test
    void getRutaCertificadoDigital_givenRnc_whenGetRuta_thenReturnsCorrectPath() {
        // Given: un Rnc válido
        String rnc = Rnc;

        // When: se obtiene la ruta del certificado digital
        String ruta = RutasDirectoriosTenant.getRutaCertificadoDigital(rnc);

        // Then: la ruta debe tener la estructura correcta
        String expected = "TENANTS-RD/123456789/CERTIFICADO_DIGITAL";
        assertEquals(expected, ruta);
    }


    @DisplayName("getRutaArchivoTenant debe construir la ruta completa dado todos los parámetros para comprobante")
    @Test
    void getRutaArchivoTenant_givenAllParameters_whenGetRuta_thenReturnsCorrectPathForComprobante() {
        // Given: todos los parámetros necesarios para un comprobante
        String rnc = Rnc;
        ContextoArchivoEnum contexto = ContextoArchivoEnum.COMPROBANTE;
        TipoOperacionArchivoTenantEnum tipoOperacion = TipoOperacionArchivoTenantEnum.EMISION;
        AmbienteEnum ambiente = AmbienteEnum.PRODUCCION;
        TipoComprobanteTributarioEnum tipoComprobante = TipoComprobanteTributarioEnum.FACTURA_CREDITO_FISCAL;

        // When: se obtiene la ruta completa del archivo de tenant
        String ruta = RutasDirectoriosTenant.getRutaArchivoTenant(
                rnc,
                contexto,
                tipoOperacion,
                ambiente,
                tipoComprobante
        );

        // Then: la ruta debe tener la estructura completa con minúsculas donde corresponda
        String expected = "TENANTS-RD/123456789/COMPROBANTES/EMISION/PRODUCCION/FACTURA_CREDITO_FISCAL";
        assertEquals(expected, ruta);
    }

}