package org.dcoronado.WebServiceRdDemo.Billing.Sesion.Domain;

import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.InvalidArgumentException;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.RNC;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class SesionTest {

    @Test
    @DisplayName("Debe iniciar sesión correctamente con RNC y ambiente válidos")
    void debeIniciarSesionCorrectamente() {
        // Given
        RNC rnc = RNC.of("123456789");
        AmbienteEnum ambiente = AmbienteEnum.PRUEBAS;

        // When
        Sesion sesion = Sesion.iniciar(rnc, ambiente);

        // Then
        assertNotNull(sesion);
        assertNull(sesion.getId());
        assertEquals(rnc, sesion.getRnc());
        assertEquals(ambiente, sesion.getAmbiente());
        assertNull(sesion.getExpedido());
        assertNull(sesion.getExpira());
        assertNull(sesion.getToken());
    }

    @Test
    @DisplayName("Debe agregar información de DGII correctamente")
    void debeAgregarInfoDgiiCorrectamente() {
        // Given
        RNC rnc = RNC.of("123456789");
        AmbienteEnum ambiente = AmbienteEnum.PRUEBAS;
        Sesion sesion = Sesion.iniciar(rnc, ambiente);

        String expedido = "2024-01-15T10:30:00Z";
        String expira = "2024-01-15T11:30:00Z";
        String token = "abc123token";

        // When
        Sesion sesionConToken = sesion.withInfoDgii(expedido, expira, token);

        // Then
        assertNotNull(sesionConToken);
        assertEquals(rnc, sesionConToken.getRnc());
        assertEquals(ambiente, sesionConToken.getAmbiente());
        assertNotNull(sesionConToken.getExpedido());
        assertNotNull(sesionConToken.getExpira());
        assertEquals(token, sesionConToken.getToken());
    }

    @Test
    @DisplayName("Debe lanzar excepción al agregar info DGII con fecha expedido nula")
    void debeLanzarExcepcionCuandoExpedidoEsNulo() {
        // Given
        Sesion sesion = Sesion.iniciar(RNC.of("123456789"), AmbienteEnum.PRUEBAS);
        String expedido = null;
        String expira = "2024-01-15T11:30:00Z";
        String token = "abc123token";

        // When & Then
        InvalidArgumentException exception = assertThrows(
            InvalidArgumentException.class,
            () -> sesion.withInfoDgii(expedido, expira, token)
        );
        assertEquals("Fecha expedido requerida", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción al agregar info DGII con fecha expira nula")
    void debeLanzarExcepcionCuandoExpiraEsNulo() {
        // Given
        Sesion sesion = Sesion.iniciar(RNC.of("123456789"), AmbienteEnum.PRUEBAS);
        String expedido = "2024-01-15T10:30:00Z";
        String expira = null;
        String token = "abc123token";

        // When & Then
        InvalidArgumentException exception = assertThrows(
            InvalidArgumentException.class,
            () -> sesion.withInfoDgii(expedido, expira, token)
        );
        assertEquals("Fecha expira requerida", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción al agregar info DGII con token vacío")
    void debeLanzarExcepcionCuandoTokenEstaVacio() {
        // Given
        Sesion sesion = Sesion.iniciar(RNC.of("123456789"), AmbienteEnum.PRUEBAS);
        String expedido = "2024-01-15T10:30:00Z";
        String expira = "2024-01-15T11:30:00Z";
        String token = "";

        // When & Then
        InvalidArgumentException exception = assertThrows(
            InvalidArgumentException.class,
            () -> sesion.withInfoDgii(expedido, expira, token)
        );
        assertEquals("Token requerido", exception.getMessage());
    }

    @Test
    @DisplayName("Debe reconstruir sesión desde base de datos correctamente")
    void debeReconstruirSesionDesdeBaseDatos() {
        // Given
        Long id = 1L;
        RNC rnc = RNC.of("123456789");
        AmbienteEnum ambiente = AmbienteEnum.PRODUCCION;
        OffsetDateTime expedido = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime expira = OffsetDateTime.now(ZoneOffset.UTC).plusHours(1);
        String token = "token123";

        // When
        Sesion sesion = Sesion.reconstruir(id, rnc, ambiente, expedido, expira, token);

        // Then
        assertNotNull(sesion);
        assertEquals(id, sesion.getId());
        assertEquals(rnc, sesion.getRnc());
        assertEquals(ambiente, sesion.getAmbiente());
        assertEquals(expedido, sesion.getExpedido());
        assertEquals(expira, sesion.getExpira());
        assertEquals(token, sesion.getToken());
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar crear una sesion Dgii en producción con tenant sin acceso a producción")
    void debeLanzarExcepcionAlAccederProduccionConTenantPruebas() {
        // Given
        Sesion sesion = Sesion.iniciar(RNC.of("123456789"), AmbienteEnum.PRODUCCION);
        AmbienteEnum ambienteTenant = AmbienteEnum.PRUEBAS;

        // When & Then
        InvalidArgumentException exception = assertThrows(
            InvalidArgumentException.class,
            () -> sesion.validarAccesLimitAmbienteTenant(ambienteTenant)
        );
        assertTrue(exception.getMessage().contains("tenant encontrado no cuenta acceso a entornos productivos"));
        assertTrue(exception.getMessage().contains("PRUEBAS"));
    }

}