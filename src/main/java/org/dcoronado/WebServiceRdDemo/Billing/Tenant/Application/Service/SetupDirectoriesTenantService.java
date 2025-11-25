package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.In.SetupDirectoriesTenantUseCase;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.TenantRepositoryPort;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.SetupDirectoriesPort;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.RNC;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.NotFoundException;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.TreeNodeDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.DirectorioTenantTreeBuilder.buildTenantTree;

/**
 * Servicio encargado de preparar y crear la estructura de directorios asociada a un tenant.
 * Este proceso incluye la validación del RNC, verificación del estado del setup,
 * construcción del árbol de directorios y persistencia del cambio en el repositorio.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SetupDirectoriesTenantService implements SetupDirectoriesTenantUseCase {

    private final TenantRepositoryPort tenantRepositoryPort;
    private final SetupDirectoriesPort setupDirectoriesPort;

    /**
     * Ejecuta el proceso de creación y configuración de directorios
     * para un tenant asociada al RNC proporcionado.
     * @param rnc RNC de el tenant para la cual se realizará el setup de directorios. No debe ser nulo ni vacío.
     * @throws NotFoundException si no se encuentra un tenant asociada al RNC indicado.
     */
    @Transactional
    @Override
    public void execute(final String rnc) {
        log.info("-----Proceso de setup DIRECTORIES para tenant con RNC {}-------", rnc);

        final var rncVO = RNC.of(rnc);

        // Busca el tenant
        log.info("Buscando tenant por RNC {}", rncVO);
        final var tenantRepository = tenantRepositoryPort.findByRnc(rncVO.getValor())
                .orElseThrow(() -> new NotFoundException("Tenant not found"));

        log.info("Creando tenant con datos de setup de directorios para RNC {}", rncVO);
       final var tenantConDatosSetupDirectories = tenantRepository.withSetupDirectories();

        log.info("Armando arbol de directorios para tenant con RNC {}", rncVO);
        TreeNodeDto directoryTreeTenant = buildTenantTree(tenantConDatosSetupDirectories.getRnc().getValor());

        // Crear estructura de directorios
        log.info("Persistiendo en storage arbol de directorios para tenant con RNC {}", rncVO);
        setupDirectoriesPort.createDirectory(directoryTreeTenant);

        log.info("Guardando cambios en el repositorio");
        tenantRepositoryPort.save(tenantConDatosSetupDirectories);
    }
}
