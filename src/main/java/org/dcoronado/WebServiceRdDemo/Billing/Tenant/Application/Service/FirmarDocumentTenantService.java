package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command.FirmarDocumentoByTenantCommand;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.In.FirmarDocumentByTenantUseCase;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.TenantRepositoryPort;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model.Tenant;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.RNC;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.NotFoundException;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.DocumentFile;
import org.dcoronado.WebServiceRdDemo.Sign.Aplication.Port.In.SignDocumentUseCase;
import org.springframework.stereotype.Service;

/**
 * Servicio de aplicación encargado de firmar documentos electrónicos asociados a un tenant.
 * Se encarga de validar el RNC y el archivo recibido, verificar la existencia de el tenant,
 * comprobar que el tenant tenga los datos necesarios para la firma,
 * y delegar la firma al proveedor externo.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FirmarDocumentTenantService implements FirmarDocumentByTenantUseCase {

    private final TenantRepositoryPort tenantRepositoryPort;
    private final SignDocumentUseCase signDocumentUseCase;

    /**
     * Firma un documento electrónico asociado a un tenant.
     *
     * @param command objeto que contiene los datos necesarios para firmar el documento
     * @return documento firmado en formato texto
     * @throws Exception         si ocurre un error durante el proceso de firma
     * @throws NotFoundException si no se encuentra el tenant con el RNC indicado
     */
    @Override
    public String firmarDocumentByTenant(FirmarDocumentoByTenantCommand command) throws Exception {

        log.info("----- Proceso de firma de documento para tenant con RNC {} -------", command.rnc());

        log.info("Validando RNC");
        RNC rncValue = RNC.of(command.rnc());

        log.info("Validando archivo de documento");
        DocumentFile file = DocumentFile.of(command.nombreDocumento(), command.documento());
        file.validateExtension("xml");

        log.info("Buscando tenant por RNC {}", rncValue);
        Tenant tenantSaved = tenantRepositoryPort.findByRnc(rncValue.getValor())
                .orElseThrow(() -> new NotFoundException("Tenant con rnc: " + rncValue + " not found"));

        log.info("Buscando que el tenant tiene datos necesarios para firma");
        tenantSaved.getCertificadoDigital().puedeFirmar();

        log.info("Convirtiendo archivo a String");
        String documentoString = file.asText();

        log.info("Firmando documento");
        return signDocumentUseCase.execute(documentoString, tenantSaved.getCertificadoDigital().getRutaAbsolutaCertificado(), tenantSaved.getCertificadoDigital().getClave());
    }
}
