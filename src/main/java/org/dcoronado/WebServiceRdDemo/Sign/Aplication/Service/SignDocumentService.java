package org.dcoronado.WebServiceRdDemo.Sign.Aplication.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.InvalidArgumentException;
import org.dcoronado.WebServiceRdDemo.Sign.Aplication.Port.In.SignDocumentUseCase;
import org.dcoronado.WebServiceRdDemo.Sign.Aplication.Port.Out.CertificateLoaderPort;
import org.dcoronado.WebServiceRdDemo.Sign.Aplication.Port.Out.CertificateValidatorPort;
import org.dcoronado.WebServiceRdDemo.Sign.Aplication.Port.Out.CryptographicSignerPort;
import org.dcoronado.WebServiceRdDemo.Sign.Domain.KeyAndCertificate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

/**
 * Servicio encargado de la firma criptográfica de documentos.
 * Orquesta la carga, validación y uso del certificado digital.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SignDocumentService implements SignDocumentUseCase {

    private final CertificateLoaderPort certificateLoaderPort;
    private final CertificateValidatorPort certificateValidatorPort;
    private final CryptographicSignerPort cryptographicSignerPort;

    /**
     * Firma un documento XML usando el certificado indicado.
     *
     * @param documentContent       contenido del documento a firmar
     * @param certificateIdentifier identificador o ruta del certificado
     * @param password              clave del certificado
     * @return documento firmado en formato XML
     */
    @Override
    public String execute(String documentContent, String certificateIdentifier, String password) throws UnrecoverableEntryException, CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException {

        log.info("INICIO - Proceso de firma digital");

        log.debug("[1] Cargando certificado desde origen.");
        KeyAndCertificate certificate = certificateLoaderPort.loadCertificate(certificateIdentifier, password);

        log.info("[2] Validando validez del certificado.");
        if (!certificateValidatorPort.isValidCertificate(certificate))
            throw new InvalidArgumentException("Certificate is not valid");

        log.info("[3] Firmando documento digitalmente...");
        return cryptographicSignerPort.signDocument(documentContent, certificate);

    }

    @Override
    public String extractHash(String signedXml) {
        return cryptographicSignerPort.extractHash(signedXml);
    }

}
