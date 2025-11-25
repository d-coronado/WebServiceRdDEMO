package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.ValueObject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.DocumentFile;

import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Assert.notBlank;
import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Assert.trimOrNull;

@Getter
@EqualsAndHashCode
public class CertificadoDigital {

    private final String nombreCertificado;
    private final byte[] contenidoCertificado;
    private final String clave;
    private final String rutaAbsolutaCertificado;

    private CertificadoDigital(String nombreCertificado,byte[] contenidoCertificado, String clave, String rutaAbsolutaCertificado) {
        this.nombreCertificado = trimOrNull(nombreCertificado);
        this.contenidoCertificado = contenidoCertificado;
        this.clave = trimOrNull(clave);
        this.rutaAbsolutaCertificado = trimOrNull(rutaAbsolutaCertificado);
    }

    public static CertificadoDigital create(String nombreCertificado, byte[] contenidoCertificado, String clave, String rutaAbsolutaCertificado) {
        notBlank(nombreCertificado, "Nombre del certificado requerido");
        notBlank(clave, "Clave del certificado requerida");
        notBlank(rutaAbsolutaCertificado, "Ruta del certificado requerida");

        final var file = DocumentFile.of(nombreCertificado, contenidoCertificado);
        file.validateExtension("p12");

        return new CertificadoDigital(file.getNombre(),contenidoCertificado, clave, rutaAbsolutaCertificado);
    }

    public static CertificadoDigital reconstructFromDatabase(String nombreCertificado, String clave, String rutaCertificado) {
        return new CertificadoDigital(nombreCertificado,null, clave, rutaCertificado);
    }

    /**
     * Verifica que el certificado esté listo para firmar.
     * Lanza IllegalStateException si no se puede firmar por estado interno del VO.
     */
    public void puedeFirmar() {
        if (this.rutaAbsolutaCertificado == null || this.rutaAbsolutaCertificado.isBlank() || this.clave == null || this.clave.isBlank()) {
            throw new IllegalStateException("El certificado no está completo para firmar");
        }
    }
}
