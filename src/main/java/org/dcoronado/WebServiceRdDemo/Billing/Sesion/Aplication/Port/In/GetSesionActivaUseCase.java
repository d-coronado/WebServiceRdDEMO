package org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Port.In;

import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Command.GetSesionActivaComand;
import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Domain.Sesion;

import java.util.Optional;

public interface GetSesionActivaUseCase {
    Optional<Sesion> getSesionActiva(GetSesionActivaComand comand);
}
