package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Service;

import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command.CreateTenantCommand;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.TenantRepositoryPort;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model.Tenant;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.IllegalStateException;
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
class CreateTenantServiceTest {

    @Mock
    private TenantRepositoryPort tenantRepositoryPort;

    @Mock
    private Tenant tenantMock;

    @InjectMocks
    private CreateTenantService service;

    private CreateTenantCommand command;
    private static final String RNC_VALIDO = "123456789";
    private static final String RAZON_SOCIAL = "Empresa Test S.A.";
    private static final String DIRECCION_FISCAL = "Calle Test #123";
    private static final String ALIAS = "EMPRESA_TEST";
    private static final String NOMBRE_CONTACTO = "Juan Pérez";
    private static final String TELEFONO = "809-555-1234";
    private static final String AMBIENTE = "1";

    @BeforeEach
    void setUp() {
        command = new CreateTenantCommand(
                RNC_VALIDO,
                RAZON_SOCIAL,
                DIRECCION_FISCAL,
                ALIAS,
                NOMBRE_CONTACTO,
                TELEFONO,
                AMBIENTE
        );
    }

    @DisplayName("Debe crear tenant correctamente")
    @Test
    void debe_crear_tenant_correctamente() {
        when(tenantRepositoryPort.findByRnc(RNC_VALIDO)).thenReturn(Optional.empty());
        when(tenantRepositoryPort.save(any(Tenant.class))).thenReturn(tenantMock);

        Tenant resultado = service.createTenant(command);

        assertNotNull(resultado);
        verify(tenantRepositoryPort).findByRnc(RNC_VALIDO);
        verify(tenantRepositoryPort).save(any(Tenant.class));
    }

    @DisplayName("Debe lanzar excepción cuando RNC ya existe")
    @Test
    void debe_lanzar_excepcion_cuando_rnc_ya_existe() {
        when(tenantRepositoryPort.findByRnc(RNC_VALIDO)).thenReturn(Optional.of(tenantMock));

        assertThrows(IllegalStateException.class,
                () -> service.createTenant(command));
        verify(tenantRepositoryPort).findByRnc(RNC_VALIDO);
        verify(tenantRepositoryPort, never()).save(any(Tenant.class));
    }

}