package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Service;

import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command.UpdateTenantCommand;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.TenantRepositoryPort;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model.Tenant;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.NotFoundException;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.RNC;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateTenantServiceTest {

    @Mock
    private TenantRepositoryPort tenantRepositoryPort;

    @Mock
    private Tenant tenantMock;

    @InjectMocks
    private UpdateTenantService service;

    private UpdateTenantCommand command;
    private static final Long TENANT_ID = 1L;
    private static final String RNC_VALIDO = "123456789";
    private static final String RAZON_SOCIAL = "Empresa Actualizada S.A.";
    private static final String DIRECCION_FISCAL = "Calle Actualizada #456";
    private static final String ALIAS = "EMPRESA_ACTUALIZADA";
    private static final String NOMBRE_CONTACTO = "María García";
    private static final String TELEFONO = "809-555-5678";
    private static final String AMBIENTE = "3";

    @BeforeEach
    void setUp() {
        command = new UpdateTenantCommand(
                TENANT_ID,
                RNC_VALIDO,
                RAZON_SOCIAL,
                DIRECCION_FISCAL,
                ALIAS,
                NOMBRE_CONTACTO,
                TELEFONO,
                AMBIENTE
        );
    }

    @DisplayName("Debe actualizar tenant correctamente")
    @Test
    void debe_actualizar_tenant_correctamente() {
        // Given
        when(tenantRepositoryPort.findById(TENANT_ID)).thenReturn(Optional.of(tenantMock));
        when(tenantMock.update(
                any(RNC.class),
                eq(RAZON_SOCIAL),
                eq(DIRECCION_FISCAL),
                eq(ALIAS),
                eq(NOMBRE_CONTACTO),
                eq(TELEFONO),
                any(AmbienteEnum.class)
        )).thenReturn(tenantMock);
        when(tenantRepositoryPort.save(tenantMock)).thenReturn(tenantMock);

        // When
        Tenant resultado = service.updateTennat(command);

        // Then
        assertNotNull(resultado);
        verify(tenantRepositoryPort).findById(TENANT_ID);
        verify(tenantMock).update(
                any(RNC.class),
                eq(RAZON_SOCIAL),
                eq(DIRECCION_FISCAL),
                eq(ALIAS),
                eq(NOMBRE_CONTACTO),
                eq(TELEFONO),
                any(AmbienteEnum.class)
        );
        verify(tenantRepositoryPort).save(tenantMock);
    }

    @DisplayName("Debe lanzar excepción cuando tenant no existe")
    @Test
    void debe_lanzar_excepcion_cuando_tenant_no_existe() {
        // Given
        when(tenantRepositoryPort.findById(TENANT_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class,
                () -> service.updateTennat(command));
        verify(tenantRepositoryPort).findById(TENANT_ID);
        verify(tenantRepositoryPort, never()).save(any(Tenant.class));
    }
}