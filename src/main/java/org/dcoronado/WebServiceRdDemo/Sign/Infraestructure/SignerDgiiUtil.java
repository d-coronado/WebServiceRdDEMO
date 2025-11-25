package org.dcoronado.WebServiceRdDemo.Sign.Infraestructure;

import oracle.xml.parser.v2.DOMParser;
import oracle.xml.parser.v2.XMLDocument;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.InfrastructureException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utilidad para firmar XMLs según el estándar XMLDSig requerido por la DGII.
 * Se utiliza la librería de Oracle para el parseo del XML (DOMParser) debido a que el codigo base de firma es brindado por la misma DGII.
 * De hecho se quizo utilizar la librería estándar de Java para el parseo (javax.xml.parsers) pero la DGII por alguna razon no reconoce las firmas generadas con dicha librería.
 */
public class SignerDgiiUtil {

    private static final String METODO_FIRMADO = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";

    public static DOMSignContext sign(InputStream archivoXML, KeyStore.PrivateKeyEntry keyEntry) throws InfrastructureException {
        try {
            // Creamos un DOM XMLSignatureFactory
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

            Reference ref = fac.newReference(
                    "",
                    fac.newDigestMethod(DigestMethod.SHA256, null),
                    Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)),
                    null,
                    null
            );

            SignedInfo si = fac.newSignedInfo(
                    fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                    fac.newSignatureMethod(METODO_FIRMADO, null),
                    Collections.singletonList(ref)
            );

            // Obtenemos el certificado desde la KeyEntry
            X509Certificate cert = (X509Certificate) keyEntry.getCertificate();
            KeyInfoFactory kinfoFactory = fac.getKeyInfoFactory();
            List<Serializable> x509Content = new ArrayList<>();
            x509Content.add(cert);
            X509Data x509d = kinfoFactory.newX509Data(x509Content);
            KeyInfo kinfo = kinfoFactory.newKeyInfo(Collections.singletonList(x509d));

            // Parseamos el XML usando DOMParser de Oracle
            DOMParser parser = new DOMParser();
            parser.setPreserveWhitespace(false);
            parser.parse(archivoXML);
            XMLDocument xml = parser.getDocument();
            Element xmlRoot = xml.getDocumentElement();

            // Creamos el contexto de firma
            DOMSignContext dsc = new DOMSignContext(keyEntry.getPrivateKey(), xmlRoot);

            // Creamos la firma
            XMLSignature signature = fac.newXMLSignature(si, kinfo);

            // Firmamos
            signature.sign(dsc);

            return dsc;

        } catch (Exception e) {
            throw new InfrastructureException("OCURRIO UN ERROR DURANTE LA FIRMA: " + e.toString(), e);
        }
    }

    /**
     * Firma un XML pasando la clave privada y el certificado directamente.
     */
    public static DOMSignContext sign(InputStream archivoXML, PrivateKey privateKey, X509Certificate certificate) throws InfrastructureException {
        try {
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

            Reference ref = fac.newReference(
                    "",
                    fac.newDigestMethod(DigestMethod.SHA256, null),
                    Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)),
                    null,
                    null
            );

            SignedInfo si = fac.newSignedInfo(
                    fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                    fac.newSignatureMethod(METODO_FIRMADO, null),
                    Collections.singletonList(ref)
            );

            // KeyInfo con el certificado pasado
            KeyInfoFactory kinfoFactory = fac.getKeyInfoFactory();
            List<Serializable> x509Content = new ArrayList<>();
            x509Content.add(certificate);
            X509Data x509d = kinfoFactory.newX509Data(x509Content);
            KeyInfo kinfo = kinfoFactory.newKeyInfo(Collections.singletonList(x509d));

            // Parseamos el XML usando DOMParser de Oracle
            DOMParser parser = new DOMParser();
            parser.setPreserveWhitespace(false);
            parser.parse(archivoXML);
            XMLDocument xml = parser.getDocument();
            Element xmlRoot = xml.getDocumentElement();

            // Contexto de firma usando la clave privada
            DOMSignContext dsc = new DOMSignContext(privateKey, xmlRoot);

            XMLSignature signature = fac.newXMLSignature(si, kinfo);
            signature.sign(dsc);

            return dsc;

        } catch (Exception e) {
            throw new InfrastructureException("OCURRIO UN ERROR DURANTE LA FIRMA: " + e.toString(), e);
        }
    }


    public static String convertirStringAXML(final DOMSignContext dsc) throws ParserConfigurationException, TransformerException {
        Element signedXmlRoot = (Element) dsc.getParent();
        Document newXmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        newXmlDoc.appendChild(newXmlDoc.importNode(signedXmlRoot, true));

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        StringWriter buffer = new StringWriter();
        transformer.transform(new DOMSource(newXmlDoc), new StreamResult(buffer));

        return buffer.toString();
    }

    public static InputStream stringToInputStream(String xml) {
        return new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Extrae el valor de <SignatureValue> del XML firmado (XMLDSig).
     * Devuelve el contenido (normalmente Base64).
     * Lanza InfrastructureException si no se encuentra o hay error de parsing.
     */
    public static String extractSignatureValue(String signedXml) throws InfrastructureException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder builder = dbf.newDocumentBuilder();

            ByteArrayInputStream is = new ByteArrayInputStream(signedXml.getBytes(StandardCharsets.UTF_8));
            Document doc = builder.parse(is);

            // Namespace XMLDSig
            final String XMLDSIG_NS = "http://www.w3.org/2000/09/xmldsig#";

            // Buscamos el elemento SignatureValue
            NodeList nl = doc.getElementsByTagNameNS(XMLDSIG_NS, "SignatureValue");
            if (nl == null || nl.getLength() == 0) {
                throw new InfrastructureException("No se encontró <SignatureValue> en el XML firmado.");
            }

            String signatureValue = nl.item(0).getTextContent();
            if (signatureValue == null || signatureValue.trim().isEmpty()) {
                throw new InfrastructureException("El elemento <SignatureValue> está vacío.");
            }

            return signatureValue.trim();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new InfrastructureException("Error parseando XML firmado para extraer SignatureValue: " + e.getMessage(), e);
        }
    }


}
