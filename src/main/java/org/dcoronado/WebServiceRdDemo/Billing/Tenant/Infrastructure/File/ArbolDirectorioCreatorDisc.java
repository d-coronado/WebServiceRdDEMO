package org.dcoronado.WebServiceRdDemo.Billing.Tenant.Infrastructure.File;

import lombok.extern.slf4j.Slf4j;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.InfrastructureException;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.TreeNodeDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;


@Slf4j
public class ArbolDirectorioCreatorDisc {

    private final Path basePath;

    public ArbolDirectorioCreatorDisc(Path basePath) {
        this.basePath = Objects.requireNonNull(basePath, "BasePath required");
    }

    public void crearEstructuraDisc(TreeNodeDto raiz) {
        crearRecursivamente(basePath, raiz);
    }

    private void crearRecursivamente(Path padre, TreeNodeDto nodo) {
        Path actual = padre.resolve(nodo.getNombre());
        crearDirectorio(actual);
        // Recursión para los hijos
        nodo.getHijos().forEach(hijo -> crearRecursivamente(actual, hijo));
    }

    private void crearDirectorio(Path path) {
        try {
            if (Files.notExists(path)) {
                Files.createDirectories(path);
                log.info("Directorio creado: {}", path);
            } else {
                log.debug("Directorio ya existe: {}", path);
            }
        } catch (IOException e) {
            log.error("Error al crear el directorio: {}", path, e);
            throw new InfrastructureException("No se pudo crear el directorio: " + path, e);
        }
    }

}
