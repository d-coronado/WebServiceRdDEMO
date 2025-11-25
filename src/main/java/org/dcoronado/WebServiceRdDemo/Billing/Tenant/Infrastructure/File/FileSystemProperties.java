package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.File;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Component
@Validated
public class FileSystemProperties {
    @Value("${FILESYSTEM_PATH}")
    @NotBlank(message = "La ruta base del sistema de archivos no puede estar vacía")
    private String basepath;
}