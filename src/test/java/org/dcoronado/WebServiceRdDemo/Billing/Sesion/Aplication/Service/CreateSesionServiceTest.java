package org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Service;

import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.ValueObject.CertificadoDigital;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Command.CreateSesionCommand;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Port.Out.SesionRepositoryPort;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Domain.Sesion;
import org.dcoronado.WebServiceRdDemo.Dgii.Aplication.Dto.InfoTokenDgii;
import org.dcoronado.WebServiceRdDemo.Dgii.Aplication.Port.Out.DgiiPort;
import org.dcoronado.WebServiceRdDemo.Shared.Contracts.Dto.TenantInfoDto;
import org.dcoronado.WebServiceRdDemo.Shared.Contracts.Port.TenantProvider;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.InvalidArgumentException;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.RNC;
import org.dcoronado.WebServiceRdDemo.Sign.Aplication.Port.In.SignDocumentUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateSesionService - Tests de Caso de Uso")
class CreateSesionServiceTest {

    @Mock
    private DgiiPort dgiiPort;

    @Mock
    private TenantProvider tenantProvider;

    @Mock
    private SesionRepositoryPort sesionRepositoryPort;

    @Mock
    private SignDocumentUseCase signDocumentUseCase;

    @InjectMocks
    private CreateSesionService createSesionService;

    private CreateSesionCommand command;
    private TenantInfoDto tenantInfoDto;
    private InfoTokenDgii infoTokenDgii;

    @BeforeEach
    void setUp() {
        command = new CreateSesionCommand("123456789", "1");
        var rnc = RNC.of(command.rnc());
        var ambiente = AmbienteEnum.of(command.ambiente());
        var certificadoDigital = CertificadoDigital.create(
                "certificado.p12",
                new byte[]{0x00, 0x01},
                "password123",
                "/ruta/certificado.p12"
        );
        tenantInfoDto = new TenantInfoDto(
                rnc.getValor(),
                certificadoDigital,
                ambiente,
                "Empresa XYZ",
                "Calle Falsa 123"
        );
        infoTokenDgii = new InfoTokenDgii(
                "token-dgii-abc123",
                "2024-01-15T10:00:00Z",
                "2024-01-15T11:00:00Z"
        );
    }

    @Test
    @DisplayName("Debe crear sesión exitosamente con todos los pasos correctos")
    void debeCrearSesionExitosamente() throws Exception {
        // Given
        String semilla = "<Semilla>abc123</Semilla>";
        String semillaFirmada = "<Semilla Firmada>abc123</Semilla>";

        when(tenantProvider.getTenantInfoByRnc(anyString()))
                .thenReturn(tenantInfoDto);
        when(dgiiPort.obtenerSemilla(any(AmbienteEnum.class)))
                .thenReturn(semilla);
        when(signDocumentUseCase.execute(anyString(), anyString(), anyString()))
                .thenReturn(semillaFirmada);
        when(dgiiPort.validarSemilla(any(AmbienteEnum.class), any(byte[].class)))
                .thenReturn(infoTokenDgii);
        when(sesionRepositoryPort.save(any(Sesion.class)))
                .thenAnswer(invocation -> {
                    Sesion sesion = invocation.getArgument(0);
                    return Sesion.reconstruir(
                            1L,
                            sesion.getRnc(),
                            sesion.getAmbiente(),
                            sesion.getExpedido(),
                            sesion.getExpira(),
                            sesion.getToken()
                    );
                });

        // When
        Sesion sesionCreada = createSesionService.crearSesion(command);

        // Then
        assertNotNull(sesionCreada);
        assertEquals("123456789", sesionCreada.getRnc().getValor());
        assertEquals(AmbienteEnum.PRUEBAS, sesionCreada.getAmbiente());
        assertEquals("token-dgii-abc123", sesionCreada.getToken());
        assertNotNull(sesionCreada.getExpedido());
        assertNotNull(sesionCreada.getExpira());

        verify(tenantProvider).getTenantInfoByRnc("123456789");
        verify(dgiiPort).obtenerSemilla(AmbienteEnum.PRUEBAS);
        verify(signDocumentUseCase).execute(semilla, "/ruta/certificado.p12", "password123");
        verify(dgiiPort).validarSemilla(eq(AmbienteEnum.PRUEBAS), any(byte[].class));
        verify(sesionRepositoryPort).save(any(Sesion.class));
    }


    @Test
    @DisplayName("Debe lanzar excepción cuando intenta acceder a producción con tenant tiene acceso a ambiente diferente de produccion")
    void debeLanzarExcepcionCuandoTenantNoTieneAccesoAProduccion() {
        // Given
        CreateSesionCommand commandProduccion = new CreateSesionCommand("123456789", "3");

        when(tenantProvider.getTenantInfoByRnc(anyString()))
                .thenReturn(tenantInfoDto);

        // When & Then
        InvalidArgumentException exception = assertThrows(
                InvalidArgumentException.class,
                () -> createSesionService.crearSesion(commandProduccion)
        );

        assertTrue(exception.getMessage().contains("tenant encontrado no cuenta acceso a entornos productivos"));
        verify(tenantProvider).getTenantInfoByRnc("123456789");
        verify(dgiiPort, never()).obtenerSemilla(any());
    }

    @Test
    @DisplayName("Debe persistir sesión con información correcta de DGII")
    void debePersistirSesionConInformacionCorrectaDeDgii() throws Exception {
        // Given
        String semilla = "<Semilla>abc123</Semilla>";
        String semillaFirmada = "<Semilla Firmada>abc123</Semilla>";
        ArgumentCaptor<Sesion> sesionCaptor = ArgumentCaptor.forClass(Sesion.class);

        when(tenantProvider.getTenantInfoByRnc(anyString()))
                .thenReturn(tenantInfoDto);
        when(dgiiPort.obtenerSemilla(any(AmbienteEnum.class)))
                .thenReturn(semilla);
        when(signDocumentUseCase.execute(anyString(), anyString(), anyString()))
                .thenReturn(semillaFirmada);
        when(dgiiPort.validarSemilla(any(AmbienteEnum.class), any(byte[].class)))
                .thenReturn(infoTokenDgii);
        when(sesionRepositoryPort.save(any(Sesion.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        createSesionService.crearSesion(command);

        // Then
        verify(sesionRepositoryPort).save(sesionCaptor.capture());
        Sesion sesionGuardada = sesionCaptor.getValue();

        assertEquals("123456789", sesionGuardada.getRnc().getValor());
        assertEquals(AmbienteEnum.PRUEBAS, sesionGuardada.getAmbiente());
        assertEquals("token-dgii-abc123", sesionGuardada.getToken());
        assertNotNull(sesionGuardada.getExpedido());
        assertNotNull(sesionGuardada.getExpira());
    }
}