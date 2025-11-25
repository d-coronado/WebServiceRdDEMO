package org.dcoronado.WebServiceRdDemo.Sign.Infraestructure;

import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.NotFoundException;
import org.dcoronado.WebServiceRdDemo.Sign.Aplication.Port.Out.CertificateLoaderPort;
import org.dcoronado.WebServiceRdDemo.Sign.Domain.KeyAndCertificate;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Component
public class FileSystemCertificateLoaderAdapter implements CertificateLoaderPort {

    @Override
    public KeyAndCertificate loadCertificate(String certificateIdentifier, String password) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableEntryException {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(certificateIdentifier)) {
            ks.load(fis, password.toCharArray());
        }

        String alias = ks.aliases().nextElement();

        KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) ks.getEntry(
                alias,
                new KeyStore.PasswordProtection(password.toCharArray())
        );

        if (entry == null) throw new NotFoundException("No se encontró el certificado en el KeyStore");

        return new KeyAndCertificate(entry.getPrivateKey(), (X509Certificate) entry.getCertificate());
    }
}
