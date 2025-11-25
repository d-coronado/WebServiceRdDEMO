package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command.CreateTenantCommand;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.In.CreateTenantUseCase;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.*;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model.Tenant;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.RNC;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.IllegalStateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTenantService implements CreateTenantUseCase {

    private final TenantRepositoryPort tenantRepositoryPort;

    /**
     * Crea un nuevo tenant
     *
     * @param command objeto con los datos requeridos para crear el tenant
     * @return el tenant creado y persistido
     * @throws IllegalStateException si ya existe un tenant con el mismo RNC en el repositorio
     */
    @Transactional
    @Override
    public Tenant createTenant(CreateTenantCommand command) {

        log.info("----------INICIO - Creando tenant con RNC: {} ---------------", command.rnc());

        log.info("Validando RNC y Ambiente");
        final RNC rncValue = RNC.of(command.rnc());
        final AmbienteEnum ambienteEnum = AmbienteEnum.of(command.ambiente());

        log.info("Construyendo nueva instancia de Tenant");

        Tenant tenant = Tenant.create(
                rncValue,
                command.razonSocial(),
                command.direccionFiscal(),
                command.alias(),
                command.nombreContacto(),
                command.telefono(),
                ambienteEnum
        );

        log.info("Verificando registro duplicado en el repositorio");
        tenantRepositoryPort.findByRnc(tenant.getRnc().getValor())
                .ifPresent(l -> {
                    throw new IllegalStateException(
                            "Tenant con RNC %s ya existe".formatted(tenant.getRnc().getValor()));
                });


        log.info("Guardando cambios en el repositorio");
        return tenantRepositoryPort.save(tenant);
    }

}
