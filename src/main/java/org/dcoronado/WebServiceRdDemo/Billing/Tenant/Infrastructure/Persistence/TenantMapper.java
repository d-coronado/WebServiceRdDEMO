package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.Persistence;

import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model.Tenant;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.ValueObject.CertificadoDigital;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.ValueObject.ConfiguracionBD;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.RNC;
import org.springframework.stereotype.Component;

@Component
public class TenantMapper {

    public TenantEntity toEntity(Tenant model) {
        if (model == null) return null;

        var certificado = model.getCertificadoDigital();
        var configuracionBD = model.getConfiguracionBD();

        return TenantEntity.builder()
                .id(model.getId())
                .rnc(model.getRnc().getValor())
                .razonSocial(model.getRazonSocial())
                .direccionFiscal(model.getDireccionFiscal())
                .alias(model.getAlias())
                .nombreContacto(model.getNombreContacto())
                .telefonoContacto(model.getTelefonoContacto())
                // Certificado digital
                .rutaCertificado(certificado != null ? certificado.getRutaAbsolutaCertificado() : null)
                .nombreCertificado(certificado != null ? certificado.getNombreCertificado() : null)
                .claveCertificado(certificado != null ? certificado.getClave() : null)
                // Configuración BD
                .hostBd(configuracionBD != null ? configuracionBD.getHostBD() : null)
                .puertoBd(configuracionBD != null ? configuracionBD.getPuertoBD() : null)
                .urlConexionBd(configuracionBD != null ? configuracionBD.getUrlConexion() : null)
                .nombreBd(configuracionBD != null ? configuracionBD.getNombreBD() : null)
                .usuarioBd(configuracionBD != null ? configuracionBD.getUsuario() : null)
                .passwordBd(configuracionBD != null ? configuracionBD.getPassword() : null)
                .ambiente(model.getAmbiente())
                .databaseSetupStatus(model.getDbSetupStatus())
                .databaseSetupAt(model.getDbSetupAt())
                .directoriesSetupStatus(model.getDirectoriesSetupStatus())
                .directoriesSetupAt(model.getDirectoriesSetupAt())
                .isActive(model.getIsActive())
                .build();
    }

    public Tenant toDomain(TenantEntity entity) {
        if (entity == null) return null;

        RNC rnc = null;
        if (entity.getRnc() != null && !entity.getRnc().isBlank()) {
            rnc = RNC.reconstructFromDatabase(entity.getRnc());
        }

        CertificadoDigital certificadoDigital = null;
        if (entity.getNombreCertificado() != null ||
                entity.getClaveCertificado() != null ||
                entity.getRutaCertificado() != null) {

            certificadoDigital = CertificadoDigital.reconstructFromDatabase(
                    entity.getNombreCertificado(),
                    entity.getClaveCertificado(),
                    entity.getRutaCertificado()
            );
        }

        ConfiguracionBD configuracionBD = null;
        if (entity.getNombreBd() != null ||
                entity.getHostBd() != null ||
                entity.getPuertoBd() != null ||
                entity.getUsuarioBd() != null ||
                entity.getPasswordBd() != null ||
                entity.getUrlConexionBd() != null) {

            configuracionBD = ConfiguracionBD.reconstructFromDatabase(
                    entity.getNombreBd(),
                    entity.getHostBd(),
                    entity.getPuertoBd(),
                    entity.getUsuarioBd(),
                    entity.getPasswordBd(),
                    entity.getUrlConexionBd()
            );
        }

        return Tenant.reconstructFromDatabase(
                entity.getId(),
                rnc,
                entity.getRazonSocial(),
                entity.getDireccionFiscal(),
                entity.getAlias(),
                entity.getNombreContacto(),
                entity.getTelefonoContacto(),
                certificadoDigital,
                configuracionBD,
                entity.getDatabaseSetupStatus(),
                entity.getDatabaseSetupAt(),
                entity.getDirectoriesSetupStatus(),
                entity.getDirectoriesSetupAt(),
                entity.getAmbiente(),
                entity.getIsActive()
        );
    }

}
