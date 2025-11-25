package org.dcoronado.WebServiceRdDemo.Billing.Sesion.Domain;

import lombok.Getter;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.InvalidArgumentException;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.RNC;

import java.time.OffsetDateTime;

import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Assert.*;
import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Util.FechaUtil.parseUtcStringToOffsetDateTime;

@Getter
public class Sesion {

    private final Long id;
    private final RNC rnc;
    private final AmbienteEnum ambiente;
    private final OffsetDateTime expedido; // SIEMPRE EN UTC
    private final OffsetDateTime expira; // SIEMPRE EN UTC
    private final String token;

    private Sesion(Long id, RNC rnc, AmbienteEnum ambiente, OffsetDateTime expedido, OffsetDateTime expira, String token) {
        this.id = id;
        this.rnc = rnc;
        this.ambiente = ambiente;
        this.expedido = expedido;
        this.expira = expira;
        this.token = token;
    }

    public static Sesion iniciar(RNC rnc, AmbienteEnum ambiente) {
        required(rnc, "rnc requerido");
        required(ambiente, "Ambiente requerido");
        return new Sesion(null, rnc, ambiente, null, null, null);
    }

    public Sesion withInfoDgii(String expedido, String expira, String token) {
        required(expedido, "Fecha expedido requerida");
        required(expira, "Fecha expira requerida");
        notBlank(token, "Token requerido");
        return new Sesion(this.id, this.rnc, this.ambiente, parseUtcStringToOffsetDateTime(expedido), parseUtcStringToOffsetDateTime(expira), token);
    }

    public static Sesion reconstruir(Long id, RNC rnc, AmbienteEnum ambiente, OffsetDateTime expedido,
                                     OffsetDateTime expira, String token) {
        return new Sesion(id, rnc, ambiente, expedido, expira, token);
    }

    public void validarAccesLimitAmbienteTenant(AmbienteEnum ambienteTenant) {
        required(ambienteTenant, "tenant encontrado no cuenta con un ambiente valido");
        if (this.ambiente == AmbienteEnum.PRODUCCION && !ambienteTenant.equals(AmbienteEnum.PRODUCCION)) {
            throw new InvalidArgumentException(
                    String.format("tenant encontrado no cuenta acceso a entornos productivos. Tenant actual: %s",
                            ambienteTenant.getPathSegment())
            );
        }
    }

}
