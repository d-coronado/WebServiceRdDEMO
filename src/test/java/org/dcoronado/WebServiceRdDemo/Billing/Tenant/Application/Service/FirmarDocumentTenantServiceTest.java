package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Service;

import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command.FirmarDocumentoByTenantCommand;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.TenantRepositoryPort;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model.Tenant;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.ValueObject.CertificadoDigital;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.IllegalStateException;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.InvalidArgumentException;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.NotFoundException;
import org.dcoronado.WebServiceRdDemo.Sign.Aplication.Port.In.SignDocumentUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FirmarDocumentTenantServiceTest {

    @Mock
    private TenantRepositoryPort tenantRepositoryPort;

    @Mock
    private SignDocumentUseCase signDocumentUseCase;

    @Mock
    private Tenant tenantMock;

    @Mock
    private CertificadoDigital certificadoDigitalMock;

    @InjectMocks
    private FirmarDocumentTenantService service;

    private FirmarDocumentoByTenantCommand command;
    private static final String RNC_VALIDO = "123456789";
    private static final String NOMBRE_DOCUMENTO = "documento.xml";
    private static final String CONTENIDO_XML = "<?xml version=\"1.0\"?><documento>test</documento>";
    private static final byte[] DOCUMENTO_BYTES = CONTENIDO_XML.getBytes();
    private static final String RUTA_CERTIFICADO = "/ruta/certificado.p12";
    private static final String CLAVE_CERTIFICADO = "password123";
    private static final String DOCUMENTO_FIRMADO = "<?xml version=\"1.0\"?><documento firmado=\"true\">test</documento>";

    @BeforeEach
    void setUp() {
        command = new FirmarDocumentoByTenantCommand(
                RNC_VALIDO,
                NOMBRE_DOCUMENTO,
                DOCUMENTO_BYTES
        );
    }

    @DisplayName("Debe firmar documento correctamente")
    @Test
    void debe_firmar_documento_correctamente() throws Exception {
        when(tenantRepositoryPort.findByRnc(RNC_VALIDO)).thenReturn(Optional.of(tenantMock));
        when(tenantMock.getCertificadoDigital()).thenReturn(certificadoDigitalMock);
        doNothing().when(certificadoDigitalMock).puedeFirmar();
        when(certificadoDigitalMock.getRutaAbsolutaCertificado()).thenReturn(RUTA_CERTIFICADO);
        when(certificadoDigitalMock.getClave()).thenReturn(CLAVE_CERTIFICADO);
        when(signDocumentUseCase.execute(CONTENIDO_XML, RUTA_CERTIFICADO, CLAVE_CERTIFICADO))
                .thenReturn(DOCUMENTO_FIRMADO);

        String resultado = service.firmarDocumentByTenant(command);

        assertEquals(DOCUMENTO_FIRMADO, resultado);
        verify(tenantRepositoryPort).findByRnc(RNC_VALIDO);
        verify(certificadoDigitalMock).puedeFirmar();
        verify(signDocumentUseCase).execute(CONTENIDO_XML, RUTA_CERTIFICADO, CLAVE_CERTIFICADO);
    }


    @DisplayName("Debe lanzar excepción cuando extensión no es XML")
    @Test
    void debe_lanzar_excepcion_cuando_extension_no_es_xml() {
        var commandInvalido = new FirmarDocumentoByTenantCommand(
                RNC_VALIDO,
                "documento.pdf",
                DOCUMENTO_BYTES
        );

        assertThrows(InvalidArgumentException.class,
                () -> service.firmarDocumentByTenant(commandInvalido));
        verifyNoInteractions(tenantRepositoryPort);
        verifyNoInteractions(signDocumentUseCase);
    }

    @DisplayName("Debe lanzar excepción cuando documento está vacío")
    @Test
    void debe_lanzar_excepcion_cuando_documento_esta_vacio() {
        var commandInvalido = new FirmarDocumentoByTenantCommand(
                RNC_VALIDO,
                NOMBRE_DOCUMENTO,
                new byte[0]
        );

        assertThrows(InvalidArgumentException.class,
                () -> service.firmarDocumentByTenant(commandInvalido));
        verifyNoInteractions(tenantRepositoryPort);
        verifyNoInteractions(signDocumentUseCase);
    }

    @DisplayName("Debe lanzar excepción cuando tenant no existe")
    @Test
    void debe_lanzar_excepcion_cuando_tenant_no_existe() {
        when(tenantRepositoryPort.findByRnc(RNC_VALIDO)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.firmarDocumentByTenant(command));
        verify(tenantRepositoryPort).findByRnc(RNC_VALIDO);
        verifyNoInteractions(signDocumentUseCase);
    }

    @DisplayName("Debe lanzar excepción cuando certificado no puede firmar")
    @Test
    void debe_lanzar_excepcion_cuando_certificado_no_puede_firmar() {
        when(tenantRepositoryPort.findByRnc(RNC_VALIDO)).thenReturn(Optional.of(tenantMock));
        when(tenantMock.getCertificadoDigital()).thenReturn(certificadoDigitalMock);
        doThrow(new IllegalStateException("Certificado no configurado correctamente"))
                .when(certificadoDigitalMock).puedeFirmar();

        assertThrows(IllegalStateException.class,
                () -> service.firmarDocumentByTenant(command));
        verify(tenantRepositoryPort).findByRnc(RNC_VALIDO);
        verify(certificadoDigitalMock).puedeFirmar();
        verifyNoInteractions(signDocumentUseCase);
    }
}