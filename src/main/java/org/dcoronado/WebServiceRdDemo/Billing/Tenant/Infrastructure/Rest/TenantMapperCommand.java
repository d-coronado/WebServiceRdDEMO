package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.Rest;

import lombok.SneakyThrows;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command.*;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.Rest.Dto.Request.TenantRequestDto;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.Rest.Dto.Request.TenantSetupBDRequestDto;
import org.springframework.stereotype.Component;

@Component
public class TenantMapperCommand {

    /**
     * Convierte un request de creación a command.
     */
    public CreateTenantCommand toCommand(TenantRequestDto request) {
        return CreateTenantCommand
                .builder()
                .rnc(request.rnc())
                .razonSocial(request.razonSocial())
                .direccionFiscal(request.direccionFiscal())
                .alias(request.alias())
                .nombreContacto(request.nombreContacto())
                .telefono(request.telefonoContacto())
                .ambiente(request.ambiente())
                .build();
    }

    /**
     * Convierte un request de actualización a command.
     */
    public UpdateTenantCommand toCommand(Long id, TenantRequestDto request) {
        return UpdateTenantCommand.builder()
                .tenantId(id)
                .rnc(request.rnc())
                .razonSocial(request.razonSocial())
                .direccionFiscal(request.direccionFiscal())
                .alias(request.alias())
                .nombreContacto(request.nombreContacto())
                .telefono(request.telefonoContacto())
                .ambiente(request.ambiente())
                .build();
    }

    /**
     * Convierte un request de configuración de BD a command.
     */
    public SetupBDTenantCommand toCommand(TenantSetupBDRequestDto request) {
        return SetupBDTenantCommand.builder()
                .rnc(request.rnc())
                .host(request.host())
                .puerto(request.port())
                .build();
    }

    /**
     * Convierte un archivo y contraseña a command de certificado.
     */
    @SneakyThrows
    public UploadCertificadoDigitalTenantCommand toCommand(String rnc, String nombreArchivo, byte[] archivo, String contrasenia) {
        return UploadCertificadoDigitalTenantCommand.builder()
                .rnc(rnc)
                .nombreCertificado(nombreArchivo)
                .certificadoDigitalContenido(archivo)
                .claveCertificado(contrasenia)
                .build();
    }


    @SneakyThrows
    public FirmarDocumentoByTenantCommand toCommand(String rnc, String nombreArchivo, byte[] archivo) {
        return FirmarDocumentoByTenantCommand.builder()
                .rnc(rnc)
                .nombreDocumento(nombreArchivo)
                .documento(archivo)
                .build();
    }

}
