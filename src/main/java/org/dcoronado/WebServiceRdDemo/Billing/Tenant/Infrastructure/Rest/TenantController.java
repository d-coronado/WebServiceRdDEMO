package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.Rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Command.*;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.In.*;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Domain.Model.Tenant;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.Rest.Dto.Request.TenantRequestDto;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.Rest.Dto.Request.TenantSetupBDRequestDto;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.Rest.Dto.Response.TenantResponseDto;
import org.dcoronado.WebServiceRdDemo.Shared.Infraestructure.Api.AbstractApi;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Response.CustomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@RestController
@RequestMapping(path = "/api/v2/tenant")
@Tag(name = "Tenant")
@RequiredArgsConstructor
public class TenantController extends AbstractApi {

    private final CreateTenantUseCase createTenantUseCase;
    private final UpdateTenantUseCase updateTenantUseCase;
    private final GetTenantUseCase getTenantUseCase;
    private final UploadCertificadoByTenantUseCase uploadCertificadoUseCase;
    private final FirmarDocumentByTenantUseCase firmarDocumentByTenantUseCase;
    private final SetupDatabaseTenantUseCase setupDatabaseTenantUseCase;
    private final SetupDirectoriesTenantUseCase setupDirectoriesTenantUseCase;
    private final TenantMapperCommand tenantMapperCommand;
    private final TenantDtoTransformer tenantDtoTransformer;

    @Operation(summary = "Crear tenant", description = "Registra un nuevo tenant en el sistema")
    @PostMapping
    public ResponseEntity<CustomResponse> save(@Valid @RequestBody TenantRequestDto request) {
        CreateTenantCommand command = tenantMapperCommand.toCommand(request);
        Tenant result = createTenantUseCase.createTenant(command);
        TenantResponseDto responseDto = tenantDtoTransformer.fromObject(result);
        return success(responseDto, "Tenant Creado Exitosamente");
    }

    @Operation(summary = "Actualizar tenant", description = "Actualiza los datos de un tenant existente por ID")
    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse> update(@PathVariable("id") final Long id, @Valid @RequestBody TenantRequestDto request) {
        UpdateTenantCommand command = tenantMapperCommand.toCommand(id, request);
        Tenant result = updateTenantUseCase.updateTennat(command);
        TenantResponseDto responseDto = tenantDtoTransformer.fromObject(result);
        return success(responseDto, "Tenant Actualizado Exitosamente");
    }

    @Operation(summary = "Obtener tenant por ID", description = "Devuelve los detalles de un tenant")
    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse> getTenantById(@PathVariable("id") final Long id) {
        return getTenantUseCase.findById(id)
                .map(l -> {
                    TenantResponseDto responseDto = tenantDtoTransformer.fromObject(l);
                    return success(responseDto);
                })
                .orElseGet(() -> success("Tenant con id: " + id + " no encontrado"));
    }

    @Operation(summary = "Obtener tenant por RNC", description = "Busca un tenant mediante su RNC")
    @GetMapping("/rnc/{rnc}")
    public ResponseEntity<CustomResponse> getTenantByRnc(@PathVariable("rnc") final String rnc) {
        return getTenantUseCase.finByRnc(rnc)
                .map(l -> {
                    TenantResponseDto responseDto = tenantDtoTransformer.fromObject(l);
                    return success(responseDto);
                })
                .orElseGet(() -> success("Tenant con RNC: " + rnc + " no encontrado"));
    }

    /**
     * Setup de base de datos (solo una vez)
     */
    @Operation(summary = "Configurar base de datos", description = "Crea la base de datos para un nuevo tenant, debe ejecutarse solo una vez por tenant")
    @PostMapping("/setup-database")
    public ResponseEntity<CustomResponse> setupDatabase(@Valid @RequestBody TenantSetupBDRequestDto request) {
        SetupBDTenantCommand command = tenantMapperCommand.toCommand(request);
        setupDatabaseTenantUseCase.execute(command);
        return success("Setup database creado correctamente");
    }


    @Operation(summary = "Configurar directorios", description = "Crea las carpetas necesarias para guardar archivos(xml,.p12,etc) para un tenant")
    @PostMapping("/{rnc}/setup-directories")
    public ResponseEntity<CustomResponse> setupDirectories(@PathVariable String rnc) {
        setupDirectoriesTenantUseCase.execute(rnc);
        return success("Setup directories creado correctamente");
    }

    @Operation(summary = "Subir certificado digital", description = "Permite guardar certificado digital (.p12) para poder firmar documentos")
    @PostMapping("/subir_certificado/{rnc}")
    public ResponseEntity<CustomResponse> uploadCertificadoDigital(
            @PathVariable("rnc") final String rnc, @RequestParam("archivo") final MultipartFile archivo,
            @RequestParam("contrasenia") final String contrasenia
    ) throws IOException {
        final String nombreArchvio = archivo.getOriginalFilename();
        byte[] contenido = archivo.getBytes();
        UploadCertificadoDigitalTenantCommand command = tenantMapperCommand.toCommand(rnc, nombreArchvio, contenido, contrasenia);
        uploadCertificadoUseCase.execute(command);
        return success("Certificado cargado correctamente para el tenant con RNC: " + rnc);
    }

    @Operation(summary = "Firmar documento XML", description = "Firma digitalmente un documento usando el certificado de el tenant")
    @PostMapping("firmar_documento/{rnc}")
    public ResponseEntity<CustomResponse> signDocument(@PathVariable("rnc") final String rnc,
                                                       @RequestParam("archivo") final MultipartFile archivo) throws Exception {
        final String nombreArchvio = archivo.getOriginalFilename();
        byte[] contenido = archivo.getBytes();
        FirmarDocumentoByTenantCommand command = tenantMapperCommand.toCommand(rnc, nombreArchvio, contenido);
        String documentFirmado = firmarDocumentByTenantUseCase.firmarDocumentByTenant(command);
        String documentoBase64 = Base64.getEncoder().encodeToString(documentFirmado.getBytes(StandardCharsets.UTF_8));
        return success(documentoBase64,"Documento Firmado Exitosamente");
    }

}
