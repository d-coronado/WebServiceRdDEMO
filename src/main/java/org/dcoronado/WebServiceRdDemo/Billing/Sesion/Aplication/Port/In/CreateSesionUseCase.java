package org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Port.In;

import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Command.CreateSesionCommand;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Domain.Sesion;

public interface CreateSesionUseCase {
    Sesion crearSesion(CreateSesionCommand command) throws Exception;
}
