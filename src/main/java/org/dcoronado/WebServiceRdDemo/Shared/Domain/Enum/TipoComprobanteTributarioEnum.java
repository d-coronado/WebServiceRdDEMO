package org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum TipoComprobanteTributarioEnum {
    FACTURA_CREDITO_FISCAL(31, "Factura de Crédito Fiscal Electrónica", "FACTURA_CREDITO_FISCAL"),
    FACTURA_CONSUMO(32, "Factura de Consumo Electrónica", "FACTURA_CONSUMO"),
    NOTA_DEBITO(33, "Nota de Débito Electrónica", "NOTA_DEBITO"),
    NOTA_CREDITO(34, "Nota de Crédito Electrónica", "NOTA_CREDITO"),
    COMPRAS(41, "Compras Electrónico", "COMPRAS"),
    GASTOS_MENORES(43, "Gastos Menores Electrónico", "GASTOS_MENORES"),
    REGIMENES_ESPECIALES(44, "Regímenes Especiales Electrónico", "REGIMENES_ESPECIALES"),
    GUBERNAMENTAL(45, "Gubernamental Electrónico", "GUBERNAMENTAL"),
    COMPROBANTE_EXPORTACION(46, "Comprobante de Exportaciones Electrónico", "COMPROBANTE_EXPORTACION"),
    COMPROBANTE_PAGO_EXTERIOR(47, "Comprobante para Pagos al Exterior Electrónico", "COMPROBANTE_PAGO_EXTERIOR");


    private final Integer valor;
    private final String descripcion;
    private final String pathSegment;

}
