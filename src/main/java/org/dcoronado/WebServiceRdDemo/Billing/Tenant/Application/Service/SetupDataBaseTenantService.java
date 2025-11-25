package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command.SetupBDTenantCommand;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.In.SetupDatabaseTenantUseCase;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.DatabaseManagerPort;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.Dto.DbConnectionInfo;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.TenantRepositoryPort;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.ScriptDataBaseExecutorPort;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model.Tenant;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.ValueObject.ConfiguracionBD;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.RNC;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.InfrastructureException;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.InvalidArgumentException;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Servicio responsable de realizar el setup de base de datos para el tenant.
 * Este proceso incluye la validación de los datos de entrada, creación de la base de datos,
 * usuario, asignación de privilegios, ejecución de scripts de estructura y actualización del
 * estado del setup en el tenant.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SetupDataBaseTenantService implements SetupDatabaseTenantUseCase {

    private final TenantRepositoryPort tenantRepositoryPort;
    private final DatabaseManagerPort databaseManagerPort;
    private final ScriptDataBaseExecutorPort scriptExecutorPort;


    /**
     * Ejecuta el proceso completo de configuración de la base de datos para el tenant indicado.
     *
     * @param command Objeto que contiene los datos necesarios para configurar la base de datos.
     * @throws InvalidArgumentException si el setup ya fue completado previamente o los datos son inválidos.
     * @throws InfrastructureException  si ocurre un error durante la creación o configuración de la base de datos.
     * @throws NotFoundException        si no se encuentra el tenant asociada al RNC indicado.
     */
    @Transactional
    @Override
    public void execute(SetupBDTenantCommand command) {

        log.info("------Proceso de setupBD para tenant --------");

        log.info("Generando V.O. RNC");
        final var rncVO = RNC.of(command.rnc());

        log.info("Generando V.O configuraciónBD");
        final var configuracionBDVO = ConfiguracionBD.create(rncVO,command.host(), command.puerto());

        // Busca el tenant
        log.info("Buscando tenant en el repositorio");
        final Tenant tenantRepository = tenantRepositoryPort.findByRnc(rncVO.getValor())
                .orElseThrow(() -> new NotFoundException("Tenant not found"));

        log.info("Generando instancia de tenant con datos configuración BD");
        final Tenant tenantConConfiguracionBD = tenantRepository.withConfiguracionBD(configuracionBDVO);

        try {

            // Crear base de datos
            log.info("Creando base de datos '{}'", tenantConConfiguracionBD.getConfiguracionBD().getNombreBD());
            databaseManagerPort.createDatabase(tenantConConfiguracionBD.getConfiguracionBD().getNombreBD());

            // Crear usuario
            log.info("Creando usuario '{}'", tenantConConfiguracionBD.getConfiguracionBD().getUsuario());
            databaseManagerPort.createUser(tenantConConfiguracionBD.getConfiguracionBD().getUsuario(), tenantConConfiguracionBD.getConfiguracionBD().getPassword(), tenantConConfiguracionBD.getConfiguracionBD().getHostBD());

            // Otorgar privilegios
            log.info("Otorgando privilegios");
            databaseManagerPort.grantPrivileges(tenantConConfiguracionBD.getConfiguracionBD().getNombreBD(), tenantConConfiguracionBD.getConfiguracionBD().getUsuario(), tenantConConfiguracionBD.getConfiguracionBD().getHostBD());

            // Ejecutar script de estructura
            log.info("Ejecutando script de estructura de BD");
            DbConnectionInfo dbConnectionInfo = DbConnectionInfo.builder()
                    .urlConexionBd(tenantConConfiguracionBD.getConfiguracionBD().getUrlConexion())
                    .usuarioBd(tenantConConfiguracionBD.getConfiguracionBD().getUsuario())
                    .passwordBd(tenantConConfiguracionBD.getConfiguracionBD().getPassword())
                    .build();
            scriptExecutorPort.executeScript(dbConnectionInfo);

            log.info("Guardando cambios");
            tenantRepositoryPort.save(tenantConConfiguracionBD);

        } catch (Exception e) {
            log.error("[E] Error durante setup de BD para RNC {}: {}", tenantConConfiguracionBD.getRnc(), e.getMessage(), e);
            databaseManagerPort.dropUserIfExists(tenantConConfiguracionBD.getConfiguracionBD().getUsuario(), tenantConConfiguracionBD.getConfiguracionBD().getHostBD());
            log.info("Rollback realizado: usuario eliminado '{}@{}'", tenantConConfiguracionBD.getConfiguracionBD().getUsuario(), tenantConConfiguracionBD.getConfiguracionBD().getHostBD());
            throw new InfrastructureException("Error durante el proceso de setup de base de datos ", e);
        }
    }
}
