package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain;

import org.dcoronado.WebServiceRdDemo.Shared.Domain.TreeNodeDto;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.ContextoArchivoEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.TipoComprobanteTributarioEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.TipoOperacionArchivoTenantEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class DirectorioTennatTreeBuilderTest {

    /**
     * Construye el árbol completo de directorios para un tenant.
     * Estructura generada:
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

    @DisplayName("buildTenantTree debe construir la estructura correcta del árbol de tenant dado un RNC")
    @Test
    void buildTenantTree_givenRnc_whenBuild_thenStructureIsCorrect() {
        // Given: un RNC de ejemplo
        String rnc = "123456789";

        // When: se construye el árbol de tenant
        TreeNodeDto root = DirectorioTenantTreeBuilder.buildTenantTree(rnc);

        // Then: root y estructura básica
        assertNotNull(root, "root no debe ser nulo");
        assertEquals(1, root.getHijos().size(), "root debe tener un solo hijo (rnc)");
        TreeNodeDto tenantNode = root.getHijos().get(0);
        assertEquals(rnc, tenantNode.getNombre(), "el nombre del nodo tenant debe ser el RNC");

        // Then: el nodo tenant contiene los 4 contextos principales
        Set<String> nombresHijosTenant = tenantNode.getHijos().stream()
                .map(TreeNodeDto::getNombre)
                .collect(Collectors.toSet());

        assertTrue(nombresHijosTenant.contains(ContextoArchivoEnum.CERTIFICADO_DIGITAL.getPathSegment()));
        assertTrue(nombresHijosTenant.contains(ContextoArchivoEnum.COMPROBANTE.getPathSegment()));

        // Then: el nodo comprobantes tiene la jerarquía completa: tipos de operación -> ambientes -> tipos de comprobante
        TreeNodeDto comprobantesNode = tenantNode.getHijos().stream()
                .filter(n -> ContextoArchivoEnum.COMPROBANTE.getPathSegment().equals(n.getNombre()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Nodo comprobantes no encontrado"));

        // cantidad de hijos de comprobantes == cantidad de tipos de operación
        assertEquals(TipoOperacionArchivoTenantEnum.values().length, comprobantesNode.getHijos().size());

        // Para cada tipo de operación, debe haber un hijo por ambiente y dentro de cada ambiente los tipos de comprobante
        for (TreeNodeDto tipoNode : comprobantesNode.getHijos()) {
            // Then: cada tipo de operación contiene todos los ambientes
            assertEquals(AmbienteEnum.values().length, tipoNode.getHijos().size());

            // Then: cada ambiente contiene todos los tipos de comprobante
            TreeNodeDto primerAmbiente = tipoNode.getHijos().get(0);
            assertEquals(TipoComprobanteTributarioEnum.values().length, primerAmbiente.getHijos().size(),
                    "Cada ambiente debe contener todos los tipos de comprobante");
        }
    }
}