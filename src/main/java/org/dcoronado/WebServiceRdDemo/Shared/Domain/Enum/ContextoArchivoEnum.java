package org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ContextoArchivoEnum {
    CERTIFICADO_DIGITAL("CERTIFICADO_DIGITAL"),
    COMPROBANTE("COMPROBANTES");

    private final String pathSegment;
}
