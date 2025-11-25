package org.dcoronado.WebServiceRdDemo.Billing.Sesion.Aplication.Command;

import lombok.Builder;
import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Assert.notBlank;

@Builder
public record GetSesionActivaComand(
        String rnc,
        String ambiente
) {
    public GetSesionActivaComand {
        notBlank(rnc,"RNC required");
        notBlank(ambiente,"Ambiente required");
    }
}
