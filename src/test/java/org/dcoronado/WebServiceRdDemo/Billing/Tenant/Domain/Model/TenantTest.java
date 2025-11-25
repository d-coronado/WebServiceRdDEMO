package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model;

import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.ValueObject.CertificadoDigital;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.ValueObject.ConfiguracionBD;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.StatusEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.IllegalStateException;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.RNC;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TenantTest {

    @DisplayName("Crear tenant correctamente usando Value Objects reales")
    @Test
    void create_success_using_real_value_objects() {

        // GIVEN
        RNC rnc = RNC.of("123456789");
        AmbienteEnum ambiente = AmbienteEnum.PRUEBAS;

        // WHEN
        Tenant tenant = Tenant.create(
                rnc,
                "  Mi Empresa  ",
                "  Calle Falsa 123  ",
                "  alias  ",
                "  Juan  ",
                "  809-000-0000  ",
                ambiente
        );

        // THEN
        assertNull(tenant.getId());
        assertEquals(rnc, tenant.getRnc());
        assertEquals("Mi Empresa", tenant.getRazonSocial());
        assertEquals("Calle Falsa 123", tenant.getDireccionFiscal());
        assertEquals("alias", tenant.getAlias());
        assertEquals("Juan", tenant.getNombreContacto());
        assertEquals("809-000-0000", tenant.getTelefonoContacto());
        assertNull(tenant.getConfiguracionBD());
        assertNull(tenant.getCertificadoDigital());
        assertEquals(StatusEnum.PENDING, tenant.getDbSetupStatus());
        assertNull(tenant.getDbSetupAt());
        assertEquals(StatusEnum.PENDING, tenant.getDirectoriesSetupStatus());
        assertNull(tenant.getDirectoriesSetupAt());
        assertTrue(tenant.getIsActive());
    }

    @DisplayName("Actualizar tenant manteniendo el mismo RNC debe funcionar")
    @Test
    void update_success_when_rnc_is_the_same() {

        // GIVEN
        RNC rnc = RNC.of("12345678910");
        AmbienteEnum ambiente = AmbienteEnum.PRUEBAS;
        Tenant original = Tenant.create(rnc, "Empresa", "Direccion", null, null, null, ambiente);

        // WHEN
        Tenant updated = original.update(
                rnc,
                "Empresa Nueva",
                "Direccion Nueva",
                "nuevo alias",
                "Nuevo Nombre",
                "123",
                ambiente
        );

        // THEN
        assertEquals(original.getId(), updated.getId());
        assertEquals(rnc, updated.getRnc());
        assertEquals("Empresa Nueva", updated.getRazonSocial());
        assertEquals("Direccion Nueva", updated.getDireccionFiscal());
        assertEquals("nuevo alias", updated.getAlias());
        assertEquals("Nuevo Nombre", updated.getNombreContacto());
        assertEquals("123", updated.getTelefonoContacto());
        assertNull(updated.getConfiguracionBD());
        assertNull(updated.getCertificadoDigital());
        assertEquals(StatusEnum.PENDING, updated.getDbSetupStatus());
        assertNull(updated.getDbSetupAt());
        assertEquals(StatusEnum.PENDING, updated.getDirectoriesSetupStatus());
        assertNull(updated.getDirectoriesSetupAt());
        assertTrue(updated.getIsActive());
    }

    @DisplayName("No permitir cambiar el RNC cuando el setup de BD ya está completo")
    @Test
    void update_rnc_change_should_fail_when_db_setup_completed() {

        // GIVEN
        RNC rnc = RNC.of("123456789");
        AmbienteEnum ambiente = AmbienteEnum.PRUEBAS;

        ConfiguracionBD configuracionBD = ConfiguracionBD.create(rnc, "localhost", "5432");

        Tenant tenant = Tenant.create(
                rnc,
                "Empresa",
                "Direccion",
                null,
                null,
                null,
                ambiente
        );

        Tenant tenantWithBD = tenant.withConfiguracionBD(configuracionBD);
        RNC nuevoRnc = RNC.of("987654321");

        // WHEN + THEN
        assertThrows(IllegalStateException.class, () ->
                tenantWithBD.update(
                        nuevoRnc,
                        "Empresa",
                        "Direccion",
                        null,
                        null,
                        null,
                        ambiente
                )
        );
    }

    @DisplayName("Configurar BD correctamente y no permitir volver a configurarla")
    @Test
    void withConfiguracionBD_success_and_cannot_add_again() {

        // GIVEN
        RNC rnc = RNC.of("123456789");
        AmbienteEnum ambiente = AmbienteEnum.PRUEBAS;
        Tenant tenant = Tenant.create(rnc, "Empresa", "Dir", null, null, null, ambiente);
        ConfiguracionBD config = ConfiguracionBD.create(rnc, "127.0.0.1", "3306");

        // WHEN
        Tenant withConfig = tenant.withConfiguracionBD(config);

        // THEN
        assertEquals(config, withConfig.getConfiguracionBD());
        assertEquals(StatusEnum.COMPLETED, withConfig.getDbSetupStatus());
        assertNotNull(withConfig.getDbSetupAt());
        assertTrue(withConfig.getDbSetupAt() instanceof LocalDateTime);

        // AND THEN
        assertThrows(IllegalStateException.class, () -> withConfig.withConfiguracionBD(config));
    }

    @DisplayName("Subir certificado digital solo es posible cuando los directorios han sido configurados")
    @Test
    void withCertificadoDigital_success_only_when_directories_setup_completed() {

        // GIVEN
        RNC rnc = RNC.of("123456789");
        AmbienteEnum ambiente = AmbienteEnum.PRUEBAS;
        Tenant tenant = Tenant.create(rnc, "Empresa", "Dir", null, null, null, ambiente);

        byte[] contenido = new byte[]{0x01, 0x02};
        CertificadoDigital certificado = CertificadoDigital.create(
                "mi_certificado.p12",
                contenido,
                "claveSegura",
                "/abs/path/mi_certificado.p12"
        );

        // WHEN + THEN (no directorios)
        assertThrows(IllegalStateException.class, () -> tenant.withCertificadoDigital(certificado));

        // WHEN (configuramos directorios)
        Tenant withDirs = tenant.withSetupDirectories();

        // THEN
        assertEquals(StatusEnum.COMPLETED, withDirs.getDirectoriesSetupStatus());
        assertNotNull(withDirs.getDirectoriesSetupAt());

        // WHEN (agregamos certificado)
        Tenant withCert = withDirs.withCertificadoDigital(certificado);

        // THEN
        assertEquals(certificado, withCert.getCertificadoDigital());

        // AND THEN (certificado funcional)
        withCert.getCertificadoDigital().puedeFirmar();
    }

    @DisplayName("Reconstruir desde la base de datos con todos los campos poblados")
    @Test
    void reconstructFromDatabase_populates_all_fields_correctly() {

        // GIVEN
        Long id = 42L;
        RNC rnc = RNC.reconstructFromDatabase("123456789");
        String razon = "Razon";
        String dir = "Dir";
        String alias = "alias";
        String nombre = "nombre";
        String telefono = "tel";
        CertificadoDigital cert = CertificadoDigital.reconstructFromDatabase("cert.p12", "clave", "/ruta/cert.p12");
        ConfiguracionBD config = ConfiguracionBD.reconstructFromDatabase(
                "nombreBD", "host", "3306", "user", "pwd", "jdbc://host:3306/nombreBD"
        );
        StatusEnum dbStatus = StatusEnum.COMPLETED;
        StatusEnum dirStatus = StatusEnum.PENDING;
        LocalDateTime dbAt = LocalDateTime.now().minusDays(1);
        LocalDateTime dirAt = null;
        AmbienteEnum ambiente = AmbienteEnum.PRUEBAS;
        boolean isActive = false;

        // WHEN
        Tenant tenant = Tenant.reconstructFromDatabase(
                id,
                rnc,
                razon,
                dir,
                alias,
                nombre,
                telefono,
                cert,
                config,
                dbStatus,
                dbAt,
                dirStatus,
                dirAt,
                ambiente,
                isActive
        );

        // THEN
        assertEquals(id, tenant.getId());
        assertEquals(rnc, tenant.getRnc());
        assertEquals(razon, tenant.getRazonSocial());
        assertEquals(dir, tenant.getDireccionFiscal());
        assertEquals(alias, tenant.getAlias());
        assertEquals(nombre, tenant.getNombreContacto());
        assertEquals(telefono, tenant.getTelefonoContacto());
        assertEquals(cert, tenant.getCertificadoDigital());
        assertEquals(config, tenant.getConfiguracionBD());
        assertEquals(dbStatus, tenant.getDbSetupStatus());
        assertEquals(dbAt, tenant.getDbSetupAt());
        assertEquals(dirStatus, tenant.getDirectoriesSetupStatus());
        assertEquals(dirAt, tenant.getDirectoriesSetupAt());
        assertEquals(ambiente, tenant.getAmbiente());
        assertEquals(isActive, tenant.getIsActive());
    }
}
