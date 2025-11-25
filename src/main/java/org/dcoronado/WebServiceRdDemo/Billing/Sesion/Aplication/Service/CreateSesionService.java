package org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Command.CreateSesionCommand;
import org.dcoronado.WebServiceRdDemo.Dgii.Aplication.Dto.InfoTokenDgii;
import org.dcoronado.WebServiceRdDemo.Dgii.Aplication.Port.Out.DgiiPort;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Port.In.CreateSesionUseCase;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Port.Out.*;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Domain.Sesion;
import org.dcoronado.WebServiceRdDemo.Shared.Contracts.Dto.TenantInfoDto;
import org.dcoronado.WebServiceRdDemo.Shared.Contracts.Port.TenantProvider;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.RNC;
import org.dcoronado.WebServiceRdDemo.Sign.Aplication.Port.In.SignDocumentUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;


/**
 * Servicio de aplicación encargado de crear una nueva sesión.
 * Orquesta la lógica de validación de parámetros, obtención de tenant,
 * generación y firma de semilla, validación con DGII y persistencia de la sesión.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateSesionService implements CreateSesionUseCase {

    private final DgiiPort dgiiPort;
    private final TenantProvider tenantProvider;
    private final SesionRepositoryPort sesionRepositoryPort;
    private final SignDocumentUseCase signDocumentUseCase;

    /**
     * Crea una nueva sesión para el ambiente indicado.
     * Valida los parámetros, el tenant y genera la semilla firmada para DGII.
     *
     * @param command objeto con los datos necesarios para crear la sesión
     * @return la sesión creada y persistida en el repositorio
     * @throws Exception si ocurre algún error al firmar o validar la semilla
     */
    @Transactional
    @Override
    public Sesion crearSesion(CreateSesionCommand command) throws Exception {

        log.info("------- Proceso de creación de SESION DGII para RNC: {} | Ambiente: {} ----------", command.rnc(), command.ambiente());

        log.info("Creando Agregado Sesión con RNC y ambiente proporcionados.");
        final var rncVO = RNC.of(command.rnc());
        final var ambiente = AmbienteEnum.of(command.ambiente());
        Sesion sesion = Sesion.iniciar(rncVO, ambiente);

        log.info("Obteniendo información de tenant para RNC: {}", sesion.getRnc());
        TenantInfoDto tenantInfoDto = tenantProvider.getTenantInfoByRnc(sesion.getRnc().getValor());
        tenantInfoDto.certificadoDigital().puedeFirmar();

        log.info("Validando accesos permitidos para crear una sesion para el tenant.");
        sesion.validarAccesLimitAmbienteTenant(tenantInfoDto.limitAccessAmbiente());

        log.info("Solicitando semilla a DGII para ambiente: {}", sesion.getAmbiente());
        String semilla = dgiiPort.obtenerSemilla(sesion.getAmbiente());

        log.info("Firmando semilla con certificado de tenant.");
        String semillaFirmada = signDocumentUseCase.execute(semilla, tenantInfoDto.certificadoDigital().getRutaAbsolutaCertificado(), tenantInfoDto.certificadoDigital().getClave());

        log.info("Validando semilla firmada con DGII.");
        InfoTokenDgii result = dgiiPort.validarSemilla(sesion.getAmbiente(), semillaFirmada.getBytes(StandardCharsets.UTF_8));

        log.info("Creando Agregado Sesion con informacion del token brindado por DGII.");
        final var sesionwithInfoDgii= sesion.withInfoDgii(result.expedido(), result.expira(),result.token());

        log.info("Persistiendo sesión con InfoTokenDgii en repositorio");
        return sesionRepositoryPort.save(sesionwithInfoDgii);
    }
}
