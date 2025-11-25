package org.dcoronado.WebServiceRdDemo.Billing.Sesion.Infraestructure;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Command.CreateSesionCommand;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Command.GetSesionActivaComand;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Infraestructure.Dto.Request.CreateSesionRequestDto;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Infraestructure.Dto.Request.GetSesionActiveRequestDto;
import org.springframework.stereotype.Component;

@Component
public class SesionMapperCommand {

    public CreateSesionCommand toCommand(CreateSesionRequestDto request) {
        return new CreateSesionCommand(
                request.rnc(),
                request.ambiente()
        );
    }

    public GetSesionActivaComand toCommand(GetSesionActiveRequestDto request) {
        return new GetSesionActivaComand(
                request.rnc(),
                request.ambiente()
        );
    }
}
