package org.dcoronado.WebServiceRdDemo.Shared.Domain.Util;

import java.math.BigDecimal;

public class StringToValUtil {

    public static Integer toInteger(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        return Integer.valueOf(input.trim());
    }

    public static Float toFloat(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        return Float.valueOf(input.trim());
    }

    public static BigDecimal toBigDecimal(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        return new BigDecimal(input.trim());
    }

    public static Double toDouble(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        return Double.valueOf(input.trim());
    }

    public static Boolean toBoolean(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        return Boolean.valueOf(input.trim());
    }

    public static String rellenarConCeros(final String texto, final int largo) {
        return String.format("%0".concat(String.valueOf(largo)).concat("d"), Integer.parseInt(texto));
    }
}
