package org.dcoronado.WebServiceRdDemo.Billing.Sesion.Infraestructure.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Command.CreateSesionCommand;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Command.GetSesionActivaComand;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Port.In.CreateSesionUseCase;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Port.In.GetSesionActivaUseCase;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Domain.Sesion;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Infraestructure.Dto.Request.CreateSesionRequestDto;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Infraestructure.Dto.Request.GetSesionActiveRequestDto;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Infraestructure.Dto.Response.SesionResponseDto;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Infraestructure.Dto.Transformer.SesionDtoTransformer;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Infraestructure.SesionMapperCommand;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.NotFoundException;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Response.CustomResponse;
import org.dcoronado.WebServiceRdDemo.Shared.Infraestructure.Api.AbstractApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v2/sesion")
@Tag(name = "Sesion")
public class SesionController extends AbstractApi {

    private final CreateSesionUseCase sesionUseCase;
    private final GetSesionActivaUseCase getSesionActivaUseCase;
    private final SesionDtoTransformer sesionTransformer;
    private final SesionMapperCommand sesionMapperCommand;

    @Operation(summary = "Crear sesion", description = "Crea una nueva sesión con datos de el tenant para poder obtener un token y consumir los servicios de DGII")
    @PostMapping("/crear")
    public ResponseEntity<CustomResponse> crearSesion(@Valid @RequestBody CreateSesionRequestDto sesionRequestDto) throws Exception {
        CreateSesionCommand command = sesionMapperCommand.toCommand(sesionRequestDto);
        Sesion result = sesionUseCase.crearSesion(command);
        SesionResponseDto responseDto = sesionTransformer.fromObject(result);
        return success(responseDto);
    }

    @Operation(summary = "Obtener Sesion", description = "Obtiene una sesion activa de el tenant para poder obtener un token y consumir los servicios de DGII")
    @GetMapping("/obtener_activa")
    public ResponseEntity<CustomResponse> getSesionActiva(@Valid @RequestBody GetSesionActiveRequestDto getSesionActiveRequestDto) {
        GetSesionActivaComand comand = sesionMapperCommand.toCommand(getSesionActiveRequestDto);
        return getSesionActivaUseCase.getSesionActiva(comand)
                .map(s -> {
                    SesionResponseDto responseDto = sesionTransformer.fromObject(s);
                    return success(responseDto);
                })
                .orElseGet(() -> {
                    LocalDateTime ahoraUtc = LocalDateTime.now(ZoneOffset.UTC);
                    String msg = "No hay sesiones activas para la fecha / hora actual (UTC): " + ahoraUtc;
                    throw new NotFoundException(msg);
                });

    }

}