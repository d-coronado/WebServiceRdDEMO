package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain;


import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.ContextoArchivoEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.TipoComprobanteTributarioEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.TipoOperacionArchivoTenantEnum;

public final class RutasDirectoriosTenant {

    public static final String ROOT = "TENANTS-RD"; // Aca cambias deacuerdo a tu contexto

    private RutasDirectoriosTenant() {
    }


    /**
     * Ruta base de el tenant (por RNC).
     */
    private static String baseRnc(String rnc) {
        return String.join("/", ROOT, rnc);
    }

    /**
     * Ruta base para un contexto específico (por ejemplo: comprobante, certificado_digital, otros...).
     */

    private static String baseContexto(String rnc, ContextoArchivoEnum contexto) {
        return String.join("/", baseRnc(rnc), contexto.getPathSegment());
    }

    /**
     * Ruta para certificados digitales.
     */
    public static String getRutaCertificadoDigital(String rnc) {
        return baseContexto(rnc, ContextoArchivoEnum.CERTIFICADO_DIGITAL);
    }

    /**
     * Ruta general para un comprobante o aprobación comercial.
     */
    public static String getRutaArchivoTenant(
            String rnc,
            ContextoArchivoEnum contextoArchivoEnum,
            TipoOperacionArchivoTenantEnum tipoOperacion,
            AmbienteEnum ambiente,
            TipoComprobanteTributarioEnum tipoComprobante
    ) {
        return String.join("/",
                baseContexto(rnc, contextoArchivoEnum),
                tipoOperacion.getPathSegment(),
                ambiente.getPathSegment(),
                tipoComprobante.getPathSegment()
        );
    }

}
