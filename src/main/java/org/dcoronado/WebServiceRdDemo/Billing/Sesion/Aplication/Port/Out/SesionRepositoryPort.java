package org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Port.Out;

import org.dcoronado.WebServiceRdDemo.Billing.Sesion.Domain.Sesion;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SesionRepositoryPort {
    Sesion save(Sesion sesion);

    Optional<Sesion> findSesionActiveByRnc(Sesion sesion, LocalDateTime ahora);
}
