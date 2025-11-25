package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command.UpdateTenantCommand;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.In.UpdateTenantUseCase;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.*;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model.Tenant;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.RNC;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case responsable ÚNICAMENTE de actualizar datos editables de un tenant.
 * Responsabilidades:
 * - Actualizar campos de negocio (razón social, dirección, contacto, etc.)
 * - Validar que el tenant existe
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateTenantService implements UpdateTenantUseCase {

    private final TenantRepositoryPort tenantRepositoryPort;

    /**
     * Actualiza los datos editables de un tenant existente.
     *
     * @param command objeto que contiene los datos para actualizar el tenant
     * @return el tenant actualizado y persistido
     * @throws NotFoundException si no existe un tenant con el ID indicado
     */
    @Transactional
    @Override
    public Tenant updateTennat(UpdateTenantCommand command) {

        log.info("-------Proceso de actualización de tenant------");

        log.info("Validando RNC Y AMBIENTE");
        final var rncValue = RNC.of(command.rnc());
        final var ambienteEnum = AmbienteEnum.of(command.ambiente());

        log.info("Buscando tenant existente en el repositorio");
        Tenant tenantRepository = tenantRepositoryPort.findById(command.tenantId())
                .orElseThrow(() -> new NotFoundException("Tenant con ID " + command.tenantId() + " no encontrado"));

        log.info("Creando agregado Tenant con datos actualizados");
        Tenant tenantActualizada = tenantRepository.update(
                rncValue,
                command.razonSocial(),
                command.direccionFiscal(),
                command.alias(),
                command.nombreContacto(),
                command.telefono(),
                ambienteEnum
        );

        log.info("Guardando tenant con datos actualizados en el repositorio");
        return tenantRepositoryPort.save(tenantActualizada);

    }

}
