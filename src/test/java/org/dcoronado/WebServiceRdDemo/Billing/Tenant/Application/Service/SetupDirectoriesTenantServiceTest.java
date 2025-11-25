package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Service;

import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.TenantRepositoryPort;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.SetupDirectoriesPort;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model.Tenant;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.TreeNodeDto;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.RNC;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SetupDirectoriesTenantServiceTest {

    @Mock
    private TenantRepositoryPort tenantRepositoryPort;

    @Mock
    private SetupDirectoriesPort setupDirectoriesPort;

    @Mock
    private Tenant tenantMock;

    @InjectMocks
    private SetupDirectoriesTenantService service;


    @DisplayName("Ejecuta setup directorios correctamente")
    @Test
    void setup_directorios_exitoso() {
        // Given
        RNC rncVO = RNC.of("123456789");
        when(tenantRepositoryPort.findByRnc(rncVO.getValor())).thenReturn(Optional.of(tenantMock));
        when(tenantMock.getRnc()).thenReturn(rncVO);
        when(tenantMock.withSetupDirectories()).thenReturn(tenantMock);

        // When
        service.execute(rncVO.getValor());

        // Then
        verify(tenantRepositoryPort).findByRnc(rncVO.getValor());
        verify(tenantMock).withSetupDirectories();
        verify(setupDirectoriesPort).createDirectory(any(TreeNodeDto.class));
        verify(tenantRepositoryPort).save(tenantMock);
    }
}