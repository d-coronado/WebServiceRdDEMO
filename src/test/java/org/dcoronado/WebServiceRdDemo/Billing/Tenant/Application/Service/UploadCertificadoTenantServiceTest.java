package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Service;

import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command.UploadCertificadoDigitalTenantCommand;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.TenantRepositoryPort;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model.Tenant;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.ValueObject.CertificadoDigital;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.IllegalStateException;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.NotFoundException;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.SaveFilePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadCertificadoTenantServiceTest {

    @Mock
    private TenantRepositoryPort tenantRepositoryPort;

    @Mock
    private SaveFilePort saveFilePort;

    @Mock
    private Tenant tenantMock;

    @InjectMocks
    private UploadCertificadoTenantService service;

    private UploadCertificadoDigitalTenantCommand command;
    private static final String RNC_VALIDO = "123456789";
    private static final String NOMBRE_CERTIFICADO = "certificado.p12";
    private static final byte[] CONTENIDO_CERTIFICADO = "contenido".getBytes();
    private static final String CLAVE = "password123";
    private static final String RUTA_BASE = "/files";

    @BeforeEach
    void setUp() {
        command = new UploadCertificadoDigitalTenantCommand(
                RNC_VALIDO,
                NOMBRE_CERTIFICADO,
                CONTENIDO_CERTIFICADO,
                CLAVE
        );
    }

    @DisplayName("Debe cargar el certificado correctamente cuando los datos son válidos")
    @Test
    void debe_cargar_certificado_correctamente() throws IOException {
        when(tenantRepositoryPort.findByRnc(RNC_VALIDO)).thenReturn(Optional.of(tenantMock));
        when(tenantMock.isSetupDirectoriesCompleted()).thenReturn(true);
        when(saveFilePort.getBasePath()).thenReturn(RUTA_BASE);
        when(tenantMock.withCertificadoDigital(any(CertificadoDigital.class))).thenReturn(tenantMock);
        when(tenantMock.getCertificadoDigital()).thenReturn(mock(CertificadoDigital.class));
        when(tenantMock.getCertificadoDigital().getRutaAbsolutaCertificado()).thenReturn("/ruta/cert.p12");

        service.execute(command);

        verify(tenantRepositoryPort).findByRnc(RNC_VALIDO);
        verify(saveFilePort).save(anyString(), eq(CONTENIDO_CERTIFICADO));
        verify(tenantRepositoryPort).save(tenantMock);
    }


    @DisplayName("Debe lanzar excepción cuando el tenant no existe")
    @Test
    void debe_lanzar_excepcion_cuando_tenant_no_existe() {
        when(tenantRepositoryPort.findByRnc(RNC_VALIDO)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.execute(command));
        verify(tenantRepositoryPort).findByRnc(RNC_VALIDO);
        verifyNoInteractions(saveFilePort);
    }

    @DisplayName("Debe lanzar excepción cuando los directorios no están configurados")
    @Test
    void debe_lanzar_excepcion_cuando_directorios_no_estan_configurados() {
        when(tenantRepositoryPort.findByRnc(RNC_VALIDO)).thenReturn(Optional.of(tenantMock));
        when(tenantMock.isSetupDirectoriesCompleted()).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> service.execute(command));
        verify(tenantRepositoryPort).findByRnc(RNC_VALIDO);
        verifyNoInteractions(saveFilePort);
        verify(tenantRepositoryPort, never()).save(any());
    }

}