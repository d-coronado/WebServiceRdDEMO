package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.File;

import lombok.RequiredArgsConstructor;
import org.dcoronado.WebServiceRdDemo.Billing.Tenant.Application.Port.Out.SetupDirectoriesPort;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.TreeNodeDto;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.dcoronado.WebServiceRdDemo.Shared.Domain.Assert.required;

@Component
@RequiredArgsConstructor
public class ArbolDirectoryDiscAdapter implements SetupDirectoriesPort {

    private final FileSystemProperties fileSystemProperties;

    @Override
    public void createDirectory(TreeNodeDto estructura) {

        String basePathString = fileSystemProperties.getBasepath();
        required(basePathString, "Base path no puede ser null");

        Path basePath = Paths.get(basePathString);
        ArbolDirectorioCreatorDisc creador = new ArbolDirectorioCreatorDisc(basePath);
        creador.crearEstructuraDisc(estructura);
    }
}
