package org.dcoronado.WebServiceRdDemo.Sign.Aplication.Port.Out;

import org.dcoronado.WebServiceRdDemo.Sign.Domain.KeyAndCertificate;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

public interface CertificateLoaderPort {

    KeyAndCertificate loadCertificate(String certificateIdentifier, String password) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableEntryException;
}
