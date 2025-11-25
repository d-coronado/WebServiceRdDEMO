package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.ValueObject.CertificadoDigital;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.ValueObject.ConfiguracionBD;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.StatusEnum;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.IllegalStateException;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.RNC;

import java.time.LocalDateTime;

import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Assert.*;
import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.StatusEnum.COMPLETED;
import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.StatusEnum.PENDING;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Tenant {

    // IDENTIFICACIÓN Y DATOS FISCALES
    private final Long id;
    private final RNC rnc;
    private final String razonSocial;
    private final String direccionFiscal;

    // DATOS DE CONTACTO
    private final String alias;
    private final String nombreContacto;
    private final String telefonoContacto;

    // DATOS DE CERTIFICADO DIGITAL
    private final CertificadoDigital certificadoDigital;

    // CONFIG BD
    private final ConfiguracionBD configuracionBD;

    private final StatusEnum dbSetupStatus;
    private final LocalDateTime dbSetupAt;
    private final StatusEnum directoriesSetupStatus;
    private final LocalDateTime directoriesSetupAt;
    private final AmbienteEnum ambiente;
    private final Boolean isActive;

    public static Tenant create(
            RNC rnc,
            String razonSocial,
            String direccionFiscal,
            String alias,
            String nombreContacto,
            String telefonoContacto,
            AmbienteEnum ambiente
    ) {
        required(rnc, "RNC es requerido");
        notBlank(razonSocial, "Razón social requerida");
        notBlank(direccionFiscal, "Dirección fiscal requerida");
        required(ambiente, "Ambiente requerido");

        return new Tenant(
                null,
                rnc,
                razonSocial.trim(),
                direccionFiscal.trim(),
                trimOrNull(alias),
                trimOrNull(nombreContacto),
                trimOrNull(telefonoContacto),
                null,
                null,
                PENDING,
                null,
                PENDING,
                null,
                ambiente,
                true
        );
    }

    public Tenant update(
            RNC nuevoRnc,
            String nuevaRazonSocial,
            String nuevaDireccionFiscal,
            String nuevoAlias,
            String nuevoNombreContacto,
            String nuevoTelefono,
            AmbienteEnum nuevoAmbiente
    ) {
        canUpdate(nuevoRnc, nuevaRazonSocial, nuevaDireccionFiscal, nuevoAmbiente);

        return new Tenant(
                this.id,
                nuevoRnc,
                nuevaRazonSocial.trim(),
                nuevaDireccionFiscal.trim(),
                trimOrNull(nuevoAlias),
                trimOrNull(nuevoNombreContacto),
                trimOrNull(nuevoTelefono),
                this.certificadoDigital,
                this.configuracionBD,
                this.dbSetupStatus,
                this.dbSetupAt,
                this.directoriesSetupStatus,
                this.directoriesSetupAt,
                nuevoAmbiente,
                this.isActive
        );
    }

    public Tenant withConfiguracionBD(ConfiguracionBD nuevaConfig) {
        if (!canAddConfiguracionBD())
            throw new IllegalStateException("Configuración de BD ya existe o setup completado");

        return new Tenant(
                this.id,
                this.rnc,
                this.razonSocial,
                this.direccionFiscal,
                this.alias,
                this.nombreContacto,
                this.telefonoContacto,
                this.certificadoDigital,
                nuevaConfig,
                COMPLETED,
                LocalDateTime.now(),
                this.directoriesSetupStatus,
                this.directoriesSetupAt,
                this.ambiente,
                this.isActive
        );
    }

    public Tenant withSetupDirectories() {
        if (isSetupDirectoriesCompleted())
            throw new IllegalStateException("La configuración de directorios ya fue completada");

        return new Tenant(
                this.id,
                this.rnc,
                this.razonSocial,
                this.direccionFiscal,
                this.alias,
                this.nombreContacto,
                this.telefonoContacto,
                this.certificadoDigital,
                this.configuracionBD,
                this.dbSetupStatus,
                this.dbSetupAt,
                COMPLETED,
                LocalDateTime.now(),
                this.ambiente,
                this.isActive
        );
    }

    public Tenant withCertificadoDigital(CertificadoDigital certificadoDigital) {
        if (!isSetupDirectoriesCompleted())
            throw new IllegalStateException("La configuración de directorios aun no ha sido completada, no se puede agregar certificado digital");

        return new Tenant(
                this.id,
                this.rnc,
                this.razonSocial,
                this.direccionFiscal,
                this.alias,
                this.nombreContacto,
                this.telefonoContacto,
                certificadoDigital,
                this.configuracionBD,
                this.dbSetupStatus,
                this.dbSetupAt,
                this.directoriesSetupStatus,
                this.directoriesSetupAt,
                this.ambiente,
                this.isActive
        );
    }


    public static Tenant reconstructFromDatabase(
            Long id,
            RNC rnc,
            String razonSocial,
            String direccionFiscal,
            String alias,
            String nombreContacto,
            String telefonoContacto,
            CertificadoDigital certificadoDigital,
            ConfiguracionBD configuracionBD,
            StatusEnum databaseSetupStatus,
            LocalDateTime databaseSetupAt,
            StatusEnum directoriesSetupStatus,
            LocalDateTime directoriesSetupAt,
            AmbienteEnum ambiente,
            boolean isActive
    ) {
        return new Tenant(
                id,
                rnc,
                razonSocial,
                direccionFiscal,
                trimOrNull(alias),
                trimOrNull(nombreContacto),
                trimOrNull(telefonoContacto),
                certificadoDigital,
                configuracionBD,
                databaseSetupStatus,
                databaseSetupAt,
                directoriesSetupStatus,
                directoriesSetupAt,
                ambiente,
                isActive
        );
    }

    private void canUpdate(
            RNC nuevoRnc,
            String nuevaRazonSocial,
            String nuevaDireccionFiscal,
            AmbienteEnum nuevoAmbiente
    ) {
        required(nuevoRnc, "RNC es requerido");
        notBlank(nuevaRazonSocial, "Razón social requerida");
        notBlank(nuevaDireccionFiscal, "Dirección fiscal requerida");
        required(nuevoAmbiente, "Ambiente requerido");

        boolean isChangingRnc = !this.rnc.equals(nuevoRnc);
        boolean anySetupCompleted = isSetupBdCompleted() || isSetupDirectoriesCompleted();

        if (isChangingRnc && anySetupCompleted) {
            throw new IllegalStateException(
                    "No se puede cambiar el RNC porque el tenant ya tiene setup completado. " +
                            "DB: %s, Dir: %s".formatted(dbSetupStatus, directoriesSetupStatus)
            );
        }
    }


    private boolean canAddConfiguracionBD() {
        return this.configuracionBD == null && !isSetupBdCompleted();
    }

    private boolean isSetupBdCompleted() {
        return dbSetupStatus == StatusEnum.COMPLETED;
    }

    public boolean isSetupDirectoriesCompleted() {
        return directoriesSetupStatus == StatusEnum.COMPLETED;
    }

}