package org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.InvalidArgumentException;

import java.util.stream.Stream;

import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Assert.notBlank;

@AllArgsConstructor
@Getter
public enum AmbienteEnum {
    PRUEBAS("1", "PRUEBAS", "testecf"),
    CERTIFICACION("2", "CERTIFICACION", "testecf"), // luego "certecf"
    PRODUCCION("3", "PRODUCCION", "testecf");       // luego "ecf"

    private final String codigo;
    private final String pathSegment;
    private final String urlPathDgii;

    public static AmbienteEnum of(String codigo) {
        notBlank(codigo, "Código de ambiente es requerido.");
        return Stream.of(values())
                .filter(a -> a.codigo.equals(codigo.trim()))
                .findFirst()
                .orElseThrow(()-> new InvalidArgumentException("Código de ambiente inválido: " + codigo));
    }

}
