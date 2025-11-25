package org.dcoronado.WebServiceRdDemo.Shared.Infraestructure.Util;

import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.InfrastructureException;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileSiytemDiscUtil {

    private FileSiytemDiscUtil() {
    }

    public static void store(String path, byte[] content) {
        try {
            Path target = Paths.get(path);
            Files.createDirectories(target.getParent());
            Files.write(target, content);
        } catch (IOException e) {
            throw new InfrastructureException("Error guardando archivo", e);
        }
    }

    public static byte[] readBytes(String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            throw new InfrastructureException("Error leyendo archivo", e);
        }
    }

    public static void deleteFile(String path) {
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            throw new InfrastructureException("Error eliminando archivo", e);
        }
    }

    public static String readFileFromResources(String fileName) {
        try (InputStream inputStream = FileSiytemDiscUtil.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new NotFoundException("No se encontró el archivo: " + fileName);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new InfrastructureException("Ocurrio un error al leer el file", e);
        }
    }
}
