package org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Service;

import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Command.GetSesionActivaComand;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Port.Out.SesionRepositoryPort;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Domain.Sesion;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.RNC;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetSesionActivaServiceTest {

    @Mock
    private SesionRepositoryPort sesionRepositoryPort;

    @InjectMocks
    private GetSesionActivaService getSesionActivaService;

    private GetSesionActivaComand command;
    private Sesion sesionActiva;

    @BeforeEach
    void setUp() {
        command = new GetSesionActivaComand("123456789", "1");

        // Crear sesión activa del repositorio
        sesionActiva = Sesion.reconstruir(
                1L,
                RNC.of("123456789"),
                AmbienteEnum.PRUEBAS,
                OffsetDateTime.now(ZoneOffset.UTC).minusMinutes(30), // Expedido hace 30 min
                OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(30),  // Expira en 30 min
                "token-activo-abc123"
        );
    }

    @Test
    @DisplayName("Debe retornar sesión activa cuando existe en el repositorio")
    void debeRetornarSesionActivaCuandoExiste() {
        // Given
        when(sesionRepositoryPort.findSesionActiveByRnc(any(Sesion.class), any(LocalDateTime.class)))
                .thenReturn(Optional.of(sesionActiva));

        // When
        Optional<Sesion> resultado = getSesionActivaService.getSesionActiva(command);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("123456789", resultado.get().getRnc().getValor());
        assertEquals(AmbienteEnum.PRUEBAS, resultado.get().getAmbiente());
        assertEquals("token-activo-abc123", resultado.get().getToken());

        verify(sesionRepositoryPort).findSesionActiveByRnc(any(Sesion.class), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Debe retornar Optional vacío cuando no existe sesión activa")
    void debeRetornarOptionalVacioCuandoNoExisteSesionActiva() {
        // Given
        when(sesionRepositoryPort.findSesionActiveByRnc(any(Sesion.class), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        // When
        Optional<Sesion> resultado = getSesionActivaService.getSesionActiva(command);

        // Then
        assertFalse(resultado.isPresent());
        verify(sesionRepositoryPort).findSesionActiveByRnc(any(Sesion.class), any(LocalDateTime.class));
    }


    @Test
    @DisplayName("Debe consultar con fecha UTC actual")
    void debeConsultarConFechaUtcActual() {
        // Given
        ArgumentCaptor<LocalDateTime> fechaCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        LocalDateTime antes = LocalDateTime.now(ZoneOffset.UTC);

        when(sesionRepositoryPort.findSesionActiveByRnc(any(Sesion.class), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        // When
        getSesionActivaService.getSesionActiva(command);
        LocalDateTime despues = LocalDateTime.now(ZoneOffset.UTC);

        // Then
        verify(sesionRepositoryPort).findSesionActiveByRnc(any(Sesion.class), fechaCaptor.capture());

        LocalDateTime fechaConsulta = fechaCaptor.getValue();
        assertTrue(fechaConsulta.isAfter(antes.minusSeconds(1)));
        assertTrue(fechaConsulta.isBefore(despues.plusSeconds(1)));
    }


    @Test
    @DisplayName("No debe lanzar excepción cuando no encuentra sesión activa")
    void noDebeLanzarExcepcionCuandoNoEncuentraSesion() {
        // Given
        when(sesionRepositoryPort.findSesionActiveByRnc(any(Sesion.class), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        // When & Then
        assertDoesNotThrow(() -> getSesionActivaService.getSesionActiva(command));
    }
}