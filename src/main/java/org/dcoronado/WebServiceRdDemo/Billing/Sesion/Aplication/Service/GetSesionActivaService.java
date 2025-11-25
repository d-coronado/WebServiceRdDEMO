package org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Command.GetSesionActivaComand;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Port.In.GetSesionActivaUseCase;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Port.Out.SesionRepositoryPort;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Domain.Sesion;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.RNC;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;


/**
 * Servicio de aplicación encargado de obtener la sesión activa de la dgii de un RNC.
 * Verifica que la sesión esté vigente en el momento de la consulta.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GetSesionActivaService implements GetSesionActivaUseCase {

    private final SesionRepositoryPort sesionRepositoryPort;

    /**
     * Obtiene la sesión activa para el RNC indicado en el objeto Sesion.
     * Valida los parámetros de entrada y filtra por sesiones vigentes según UTC.
     *
     * @param comand objeto con los datos necesarios para consultar la sesión activa
     * @return un Optional con la sesión activa si existe, vacío en caso contrario
     */
    @Override
    public Optional<Sesion> getSesionActiva(GetSesionActivaComand comand) {
        log.info("----- Proceso de consulta de sesión activa para RNC: {} | Ambiente: {} ------", comand.rnc(), comand.ambiente());

        log.info("Creando Agregado Sesión con RNC y ambiente proporcionados.");
        final var rncVO = RNC.of(comand.rnc());
        final var ambienteEnum = AmbienteEnum.of(comand.ambiente());
        Sesion sesion = Sesion.iniciar(rncVO, ambienteEnum);

        // Obtener fecha y hora actual en UTC
        LocalDateTime ahoraUtc = LocalDateTime.now(ZoneOffset.UTC);

        log.info("Buscando sesión activa en repositorio.");
        Optional<Sesion> sesionActiva = sesionRepositoryPort.findSesionActiveByRnc(sesion, ahoraUtc);

        /* No se lanza excepción para no interrumpir el flujo en módulos superiores que
           requieren crear una sesión automáticamente cuando no existe una activa.*/
        if (sesionActiva.isPresent()) {
            log.info("Sesión activa encontrada | ID: {}", sesionActiva.get().getId());
        } else {
            log.warn("No existe sesión activa para RNC: {} en ambiente: {}. Se requerirá crear una nueva.",
                    comand.rnc(), comand.ambiente());
        }

        return sesionActiva;
    }

}
