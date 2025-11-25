package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Service;

import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command.SetupBDTenantCommand;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.DatabaseManagerPort;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.Dto.DbConnectionInfo;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.TenantRepositoryPort;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.ScriptDataBaseExecutorPort;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model.Tenant;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.ValueObject.ConfiguracionBD;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.InfrastructureException;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.InvalidArgumentException;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SetupDataBaseTenantServiceTest {

    @Mock
    private TenantRepositoryPort tenantRepositoryPort;

    @Mock
    private DatabaseManagerPort databaseManagerPort;

    @Mock
    private ScriptDataBaseExecutorPort scriptExecutorPort;

    @Mock
    private Tenant tenantMock;

    @Mock
    private ConfiguracionBD configuracionBDMock;

    @InjectMocks
    private SetupDataBaseTenantService service;

    private SetupBDTenantCommand command;
    private static final String RNC_VALIDO = "123456789";
    private static final String HOST = "localhost";
    private static final String PUERTO = "3306";
    private static final String NOMBRE_BD = "fe_rd_V2_123456789";
    private static final String USUARIO = "fe_rd_user_V2_123456789";
    private static final String PASSWORD = "password123";
    private static final String URL_CONEXION = "jdbc:mysql://localhost:3306/fe_rd_V2_123456789";

    @BeforeEach
    void setUp() {
        command = new SetupBDTenantCommand(RNC_VALIDO, HOST, PUERTO);
    }

    @DisplayName("Ejecuta setupBD correctamente")
    @Test
    void setup_bd_exitoso() throws Exception {
        // Given
        when(tenantRepositoryPort.findByRnc(RNC_VALIDO)).thenReturn(Optional.of(tenantMock));
        when(tenantMock.withConfiguracionBD(any(ConfiguracionBD.class))).thenReturn(tenantMock);
        when(tenantMock.getConfiguracionBD()).thenReturn(configuracionBDMock);
        when(configuracionBDMock.getNombreBD()).thenReturn(NOMBRE_BD);
        when(configuracionBDMock.getUsuario()).thenReturn(USUARIO);
        when(configuracionBDMock.getPassword()).thenReturn(PASSWORD);
        when(configuracionBDMock.getHostBD()).thenReturn(HOST);
        when(configuracionBDMock.getUrlConexion()).thenReturn(URL_CONEXION);

        doNothing().when(databaseManagerPort).createDatabase(NOMBRE_BD);
        doNothing().when(databaseManagerPort).createUser(USUARIO, PASSWORD, HOST);
        doNothing().when(databaseManagerPort).grantPrivileges(NOMBRE_BD, USUARIO, HOST);
        doNothing().when(scriptExecutorPort).executeScript(any(DbConnectionInfo.class));

        // When
        service.execute(command);

        // Then
        verify(tenantRepositoryPort).findByRnc(RNC_VALIDO);
        verify(databaseManagerPort).createDatabase(NOMBRE_BD);
        verify(databaseManagerPort).createUser(USUARIO, PASSWORD, HOST);
        verify(databaseManagerPort).grantPrivileges(NOMBRE_BD, USUARIO, HOST);
        verify(scriptExecutorPort).executeScript(any(DbConnectionInfo.class));
        verify(tenantRepositoryPort).save(tenantMock);
        verify(databaseManagerPort, never()).dropUserIfExists(anyString(), anyString());
    }

    @DisplayName("Lanza InvalidArgumentException si host es vacío")
    @Test
    void host_vacio_lanza_excepcion() {
        // When & Then
        assertThrows(InvalidArgumentException.class,
                () -> new SetupBDTenantCommand(RNC_VALIDO, "", PUERTO));

        verifyNoInteractions(tenantRepositoryPort);
        verifyNoInteractions(databaseManagerPort);
        verifyNoInteractions(scriptExecutorPort);
    }

    @DisplayName("Lanza InvalidArgumentException si puerto es vacío")
    @Test
    void puerto_vacio_lanza_excepcion() {
        // When & Then
        assertThrows(InvalidArgumentException.class,
                () -> new SetupBDTenantCommand(RNC_VALIDO, HOST, ""));

        verifyNoInteractions(tenantRepositoryPort);
        verifyNoInteractions(databaseManagerPort);
        verifyNoInteractions(scriptExecutorPort);
    }

    @DisplayName("Lanza NotFoundException si el tenant no existe")
    @Test
    void tenant_no_encontrado_lanza_excepcion() {
        // Given
        when(tenantRepositoryPort.findByRnc(RNC_VALIDO)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> service.execute(command));
        verify(tenantRepositoryPort).findByRnc(RNC_VALIDO);
        verifyNoInteractions(databaseManagerPort);
        verifyNoInteractions(scriptExecutorPort);
    }

    @DisplayName("Lanza InfrastructureException y realiza rollback si hay error al crear BD")
    @Test
    void error_crear_bd_realiza_rollback() {
        // Given
        when(tenantRepositoryPort.findByRnc(RNC_VALIDO)).thenReturn(Optional.of(tenantMock));
        when(tenantMock.withConfiguracionBD(any(ConfiguracionBD.class))).thenReturn(tenantMock);
        when(tenantMock.getConfiguracionBD()).thenReturn(configuracionBDMock);
        when(configuracionBDMock.getNombreBD()).thenReturn(NOMBRE_BD);
        when(configuracionBDMock.getUsuario()).thenReturn(USUARIO);
        when(configuracionBDMock.getHostBD()).thenReturn(HOST);

        doThrow(new RuntimeException("Error al crear BD")).when(databaseManagerPort).createDatabase(NOMBRE_BD);

        // When & Then
        assertThrows(InfrastructureException.class, () -> service.execute(command));
        verify(databaseManagerPort).createDatabase(NOMBRE_BD);
        verify(databaseManagerPort).dropUserIfExists(USUARIO, HOST);
        verify(tenantRepositoryPort, never()).save(any());
    }

    @DisplayName("Lanza InfrastructureException y realiza rollback si hay error al crear usuarioBD")
    @Test
    void error_crear_usuario_realiza_rollback() {
        // Given
        when(tenantRepositoryPort.findByRnc(RNC_VALIDO)).thenReturn(Optional.of(tenantMock));
        when(tenantMock.withConfiguracionBD(any(ConfiguracionBD.class))).thenReturn(tenantMock);
        when(tenantMock.getConfiguracionBD()).thenReturn(configuracionBDMock);
        when(configuracionBDMock.getNombreBD()).thenReturn(NOMBRE_BD);
        when(configuracionBDMock.getUsuario()).thenReturn(USUARIO);
        when(configuracionBDMock.getPassword()).thenReturn(PASSWORD);
        when(configuracionBDMock.getHostBD()).thenReturn(HOST);

        doNothing().when(databaseManagerPort).createDatabase(NOMBRE_BD);
        doThrow(new RuntimeException("Error al crear usuario")).when(databaseManagerPort).createUser(USUARIO, PASSWORD, HOST);

        // When & Then
        assertThrows(InfrastructureException.class, () -> service.execute(command));
        verify(databaseManagerPort).createDatabase(NOMBRE_BD);
        verify(databaseManagerPort).createUser(USUARIO, PASSWORD, HOST);
        verify(databaseManagerPort).dropUserIfExists(USUARIO, HOST);
        verify(tenantRepositoryPort, never()).save(any());
    }

    @DisplayName("Lanza InfrastructureException y realiza rollback si hay error al otorgar privilegios")
    @Test
    void error_otorgar_privilegios_realiza_rollback() {
        // Given
        when(tenantRepositoryPort.findByRnc(RNC_VALIDO)).thenReturn(Optional.of(tenantMock));
        when(tenantMock.withConfiguracionBD(any(ConfiguracionBD.class))).thenReturn(tenantMock);
        when(tenantMock.getConfiguracionBD()).thenReturn(configuracionBDMock);
        when(configuracionBDMock.getNombreBD()).thenReturn(NOMBRE_BD);
        when(configuracionBDMock.getUsuario()).thenReturn(USUARIO);
        when(configuracionBDMock.getPassword()).thenReturn(PASSWORD);
        when(configuracionBDMock.getHostBD()).thenReturn(HOST);

        doNothing().when(databaseManagerPort).createDatabase(NOMBRE_BD);
        doNothing().when(databaseManagerPort).createUser(USUARIO, PASSWORD, HOST);
        doThrow(new RuntimeException("Error al otorgar privilegios")).when(databaseManagerPort).grantPrivileges(NOMBRE_BD, USUARIO, HOST);

        // When & Then
        assertThrows(InfrastructureException.class, () -> service.execute(command));
        verify(databaseManagerPort).grantPrivileges(NOMBRE_BD, USUARIO, HOST);
        verify(databaseManagerPort).dropUserIfExists(USUARIO, HOST);
        verify(tenantRepositoryPort, never()).save(any());
    }

    @DisplayName("Lanza InfrastructureException y realiza rollback si hay error al ejecutar script")
    @Test
    void error_ejecutar_script_realiza_rollback() throws Exception {
        // Given
        when(tenantRepositoryPort.findByRnc(RNC_VALIDO)).thenReturn(Optional.of(tenantMock));
        when(tenantMock.withConfiguracionBD(any(ConfiguracionBD.class))).thenReturn(tenantMock);
        when(tenantMock.getConfiguracionBD()).thenReturn(configuracionBDMock);
        when(configuracionBDMock.getNombreBD()).thenReturn(NOMBRE_BD);
        when(configuracionBDMock.getUsuario()).thenReturn(USUARIO);
        when(configuracionBDMock.getPassword()).thenReturn(PASSWORD);
        when(configuracionBDMock.getHostBD()).thenReturn(HOST);
        when(configuracionBDMock.getUrlConexion()).thenReturn(URL_CONEXION);

        doNothing().when(databaseManagerPort).createDatabase(NOMBRE_BD);
        doNothing().when(databaseManagerPort).createUser(USUARIO, PASSWORD, HOST);
        doNothing().when(databaseManagerPort).grantPrivileges(NOMBRE_BD, USUARIO, HOST);
        doThrow(new RuntimeException("Error al ejecutar script")).when(scriptExecutorPort).executeScript(any(DbConnectionInfo.class));

        // When & Then
        assertThrows(InfrastructureException.class, () -> service.execute(command));
        verify(scriptExecutorPort).executeScript(any(DbConnectionInfo.class));
        verify(databaseManagerPort).dropUserIfExists(USUARIO, HOST);
        verify(tenantRepositoryPort, never()).save(any());
    }

    @DisplayName("Verifica DbConnectionInfo correcta después de setup exitoso")
    @Test
    void verifica_db_connection_info_correcta() throws Exception {
        // Given
        ArgumentCaptor<DbConnectionInfo> dbConnectionInfoCaptor = ArgumentCaptor.forClass(DbConnectionInfo.class);

        when(tenantRepositoryPort.findByRnc(RNC_VALIDO)).thenReturn(Optional.of(tenantMock));
        when(tenantMock.withConfiguracionBD(any(ConfiguracionBD.class))).thenReturn(tenantMock);
        when(tenantMock.getConfiguracionBD()).thenReturn(configuracionBDMock);
        when(configuracionBDMock.getNombreBD()).thenReturn(NOMBRE_BD);
        when(configuracionBDMock.getUsuario()).thenReturn(USUARIO);
        when(configuracionBDMock.getPassword()).thenReturn(PASSWORD);
        when(configuracionBDMock.getHostBD()).thenReturn(HOST);
        when(configuracionBDMock.getUrlConexion()).thenReturn(URL_CONEXION);

        doNothing().when(scriptExecutorPort).executeScript(dbConnectionInfoCaptor.capture());

        // When
        service.execute(command);

        // Then
        DbConnectionInfo capturedInfo = dbConnectionInfoCaptor.getValue();
        assertNotNull(capturedInfo);
        assertEquals(URL_CONEXION, capturedInfo.urlConexionBd());
        assertEquals(USUARIO, capturedInfo.usuarioBd());
        assertEquals(PASSWORD, capturedInfo.passwordBd());
    }
}