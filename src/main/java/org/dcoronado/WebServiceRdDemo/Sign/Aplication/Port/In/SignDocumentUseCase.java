package org.dcoronado.WebServiceRdDemo.Sign.Aplication.Port.In;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

public interface SignDocumentUseCase {
    // Firmar
    String execute(String documentContent, String certificateIdentifier, String password) throws UnrecoverableEntryException, CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException;

    // Extrae el hash de firma del XML firmado
    String extractHash(String signedXml);
}
