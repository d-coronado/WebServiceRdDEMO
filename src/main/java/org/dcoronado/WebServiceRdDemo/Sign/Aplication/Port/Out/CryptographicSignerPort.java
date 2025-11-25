package org.dcoronado.WebServiceRdDemo.Sign.Aplication.Port.Out;


import org.dcoronado.WebServiceRdDemo.Sign.Domain.KeyAndCertificate;

public interface CryptographicSignerPort {
    String signDocument(String documentContent, KeyAndCertificate keyAndCertificate);
    String extractHash(String signedXml);
}
