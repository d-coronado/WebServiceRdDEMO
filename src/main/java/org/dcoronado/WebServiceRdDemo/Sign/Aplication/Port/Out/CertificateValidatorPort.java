package org.dcoronado.WebServiceRdDemo.Sign.Aplication.Port.Out;


import org.dcoronado.WebServiceRdDemo.Sign.Domain.KeyAndCertificate;

import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;

public interface CertificateValidatorPort {
    boolean isValidCertificate(KeyAndCertificate keyAndCertificate) throws CertificateNotYetValidException, CertificateExpiredException;
}
