package org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.InvalidArgumentException;

import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Assert.notBlank;
import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Assert.trimOrNull;

@Getter
@EqualsAndHashCode
public class RNC {

    private final String valor;

    private RNC(String valor) {
        this.valor = trimOrNull(valor);
    }

    public static RNC of(String valor) {
        notBlank(valor, "RNC no puede estar vacío");
        validarFormato(valor);
        return new RNC(valor);
    }

    public static RNC reconstructFromDatabase(String valor) {
        return new RNC(valor);
    }

    private static void validarFormato(String rnc) {
        String rncLimpio = rnc.replaceAll("[^0-9]", "");
        if (rncLimpio.length() != 9 && rncLimpio.length() != 11) {
            throw new InvalidArgumentException(
                    "RNC debe tener 9 o 11 dígitos, recibido: " + rnc
            );
        }
    }

    @Override
    public String toString() {
        return valor;
    }
}
