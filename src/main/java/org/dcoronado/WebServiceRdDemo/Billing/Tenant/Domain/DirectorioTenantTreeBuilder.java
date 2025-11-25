package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain;

import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.ContextoArchivoEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.TipoComprobanteTributarioEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.TipoOperacionArchivoTenantEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.TreeNodeDto;

import static org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.RutasDirectoriosTenant.*;
import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Assert.required;

/**
 * Constructor de árbol de directorios para tenants DGII.
 * Genera la estructura de carpetas basada en RNC, ambientes y tipos de comprobantes.
 */
public final class DirectorioTenantTreeBuilder {


    private DirectorioTenantTreeBuilder() {
    }

    /**
     * Construye el árbol completo de directorios para un tenant.
     * Estructura generada:
     * Esta estructua se armo con la documentacion de DGII : Ambientes,Procesos(emision de comprobante,recepcion de comprobantes)
     * y tipos de comprobantes. Esto puede reestructurarse segun tu necesiadad
     * Ejemplo grafico
     * - TENANTS-RD/
     * ├── rnc-tenant/ (ejemplo: 123456789)
     * │   ├── certificado_digital/
     * │   ├── comprobantes/
     * │   │   ├── recepcion/
     * │   │   └── emision/
     * │   │       ├── pruebas/
     * │   │       ├── certificacion/
     * │   │       └── produccion/
     * │   │           ├── factura_credito_fiscal/
     * │   │           ├── factura_consumo/
     * │   │           ├── nota_credito/
     * │   │           ├── nota_debito/
     * │
     */
    public static TreeNodeDto buildTenantTree(String rnc) {
        required(rnc, "rnc required");

        TreeNodeDto root = new TreeNodeDto(ROOT);
        root.agregarHijo(construirNodoTenant(rnc));
        return root;
    }


    private static TreeNodeDto construirNodoTenant(String rnc) {
        TreeNodeDto tenant = new TreeNodeDto(rnc);
        tenant.agregarHijo(new TreeNodeDto(ContextoArchivoEnum.CERTIFICADO_DIGITAL.getPathSegment()));
        tenant.agregarHijo(construirNodoComprobantes());
        return tenant;
    }

    private static TreeNodeDto construirNodoComprobantes() {
        TreeNodeDto comprobantes = new TreeNodeDto(ContextoArchivoEnum.COMPROBANTE.getPathSegment());

        // emision, recepcion
        for (TipoOperacionArchivoTenantEnum tipo : TipoOperacionArchivoTenantEnum.values()) {
            comprobantes.agregarHijo(construirNodoTipoArchivo(tipo));
        }

        return comprobantes;
    }


    private static TreeNodeDto construirNodoTipoArchivo(TipoOperacionArchivoTenantEnum tipoArchivo) {
        TreeNodeDto nodoTipo = new TreeNodeDto(tipoArchivo.getPathSegment());

        for (AmbienteEnum ambiente : AmbienteEnum.values()) {
            nodoTipo.agregarHijo(construirNodoAmbiente(ambiente));
        }

        return nodoTipo;
    }

    private static TreeNodeDto construirNodoAmbiente(AmbienteEnum ambiente) {
        TreeNodeDto nodoAmbiente = new TreeNodeDto(ambiente.getPathSegment());

        for (TipoComprobanteTributarioEnum tipoComprobante : TipoComprobanteTributarioEnum.values()) {
            nodoAmbiente.agregarHijo(construirNodoComprobante(tipoComprobante));
        }
        return nodoAmbiente;
    }

    private static TreeNodeDto construirNodoComprobante(TipoComprobanteTributarioEnum tipoComprobante) {
        return new TreeNodeDto(tipoComprobante.getPathSegment());
    }

}