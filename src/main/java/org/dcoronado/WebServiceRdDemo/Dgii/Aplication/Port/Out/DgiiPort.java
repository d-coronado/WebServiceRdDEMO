package org.dcoronado.WebServiceRdDemo.Dgii.Aplication.Port.Out;

import org.dcoronado.WebServiceRdDemo.Dgii.Aplication.Dto.InfoRecepcionEcfDgii;
import org.dcoronado.WebServiceRdDemo.Dgii.Aplication.Dto.InfoRecepcionRcefDgii;
import org.dcoronado.WebServiceRdDemo.Dgii.Aplication.Dto.InfoTokenDgii;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;

public interface DgiiPort {

    String obtenerSemilla(AmbienteEnum ambiente);

    InfoTokenDgii validarSemilla(AmbienteEnum ambiente, byte[] xmlSemilla);

    InfoRecepcionEcfDgii recepcionECF(AmbienteEnum ambiente, String token, byte[] xmlComprobante);

    InfoRecepcionRcefDgii recepcionResumenECF(AmbienteEnum ambiente, String token, byte[] xmlComprobante);
}
