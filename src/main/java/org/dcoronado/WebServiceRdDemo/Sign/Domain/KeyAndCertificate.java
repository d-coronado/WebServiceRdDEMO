package org.dcoronado.WebServiceRdDemo.Sign.Domain;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public record KeyAndCertificate(
        PrivateKey privateKey, X509Certificate certificate
) {
}
