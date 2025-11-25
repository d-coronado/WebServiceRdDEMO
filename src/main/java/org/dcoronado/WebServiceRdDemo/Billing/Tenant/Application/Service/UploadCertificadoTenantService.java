package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command.UploadCertificadoDigitalTenantCommand;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.In.UploadCertificadoByTenantUseCase;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.TenantRepositoryPort;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.ValueObject.CertificadoDigital;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.IllegalStateException;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.ValueObject.RNC;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.SaveFilePort;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.RutasDirectoriosTenant.getRutaCertificadoDigital;


/**
 * Servicio de aplicación encargado de cargar y registrar
 * un certificado digital asociado a un tenant.
 * Se encarga de validar los datos de entrada, verificar la existencia de el tenant,
 * guardar el archivo del certificado en la ruta correspondiente
 * y actualizar la información de el tenant con los datos del certificado.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UploadCertificadoTenantService implements UploadCertificadoByTenantUseCase {

    private final TenantRepositoryPort tenantRepositoryPort;
    private final SaveFilePort saveFilePort;


    /**
     * Carga un certificado digital y lo asocia a el tenant correspondiente.
     * @param command Objeto que contiene los datos necesarios para cargar el certificado digital.
     * @throws IOException       si ocurre un error al guardar el archivo
     * @throws NotFoundException si no se encuentra el tenant con el RNC indicado
     */
    @Transactional
    @Override
    public void execute(UploadCertificadoDigitalTenantCommand command) throws IOException {
        log.info("----- Proceso de carga de certificado digital para RNC: {} ------", command.rnc());

        log.info("Validando RNC");
        final var rncVO = RNC.of(command.rnc());

        log.info("Buscando tenant en el repositorio con RNC {}", rncVO);
        final var tenantRepository = tenantRepositoryPort.findByRnc(rncVO.getValor())
                .orElseThrow(() -> new NotFoundException("Tenant con RNC " + rncVO.getValor() + " no encontrado"));

        if(!tenantRepository.isSetupDirectoriesCompleted())
            throw new IllegalStateException("el tenant con RNC " + rncVO.getValor() + " no ha completado la configuración de directorios.");

        final String rutaBase = saveFilePort.getBasePath();
        final String rutaCompletaCertificado = String.join("/", rutaBase + getRutaCertificadoDigital(rncVO.getValor()), command.nombreCertificado());

        final var certifcadoVO = CertificadoDigital.create(command.nombreCertificado(),command.certificadoDigitalContenido(), command.claveCertificado(), rutaCompletaCertificado);

        final var tenantWhithCertificado = tenantRepository.withCertificadoDigital(certifcadoVO);

        log.info("Guardando archivo del certificado en el sistema de archivos.");
        saveFilePort.save(tenantWhithCertificado.getCertificadoDigital().getRutaAbsolutaCertificado(), certifcadoVO.getContenidoCertificado());

        log.info("Guardando datos del certificado en el repositorio de tenant.");
        tenantRepositoryPort.save(tenantWhithCertificado);
    }
}
