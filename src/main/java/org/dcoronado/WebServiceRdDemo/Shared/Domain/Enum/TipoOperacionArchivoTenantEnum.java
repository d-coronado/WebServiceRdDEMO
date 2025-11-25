package org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoOperacionArchivoTenantEnum {
    EMISION("EMISION"),
    RECEPCION("RECEPCION");

    private final String pathSegment;
}
