package org.dcoronado.WebServiceRdDemo.Shared.Infraestructure.Util;

public class FuncionesGenericasUtil {
    public static boolean isLinux() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("nux") || os.contains("nix") || os.contains("aix");
    }
}
