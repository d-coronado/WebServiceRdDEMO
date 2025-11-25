package org.dcoronado.WebServiceRdDemo.Sign.Infraestructure;

import org.dcoronado.WebServiceRdDemo.Sign.Aplication.Port.Out.CertificateValidatorPort;
import org.dcoronado.WebServiceRdDemo.Sign.Domain.KeyAndCertificate;
import org.springframework.stereotype.Component;

import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;

@Component
public class X509CertificateValidatorAdapter implements CertificateValidatorPort {

    @Override
    public boolean isValidCertificate(KeyAndCertificate keyAndCertificate) throws CertificateNotYetValidException, CertificateExpiredException {
        var certificate = keyAndCertificate.certificate();

        // Verificar que no haya expirado
        certificate.checkValidity();

        // Verificar que tenga capacidad de firma digital
        var keyUsage = certificate.getKeyUsage();

        // digitalSignature bit
        return keyUsage != null && keyUsage[0];
    }
}
