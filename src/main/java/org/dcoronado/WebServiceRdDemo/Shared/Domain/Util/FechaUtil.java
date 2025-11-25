package org.dcoronado.WebServiceRdDemo.Shared.Domain.Util;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public class FechaUtil {

    private static final String FORMATO_FECHA = "dd-MM-yyyy";
    private static final Pattern REGEX_FECHA = Pattern.compile(
            "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$"
    );

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(FORMATO_FECHA);

    /**
     * Verifica si la fecha tiene formato correcto dd-MM-yyyy.
     * Retorna true si es válida, false si no.
     */
    public static boolean tieneFormatoFechaValido(final String fecha) {
        if (isNull(fecha)) return false;
        return REGEX_FECHA.matcher(fecha).matches();
    }


    public static boolean isBefore(String fecha1, String fecha2) {
        LocalDate f1 = LocalDate.parse(fecha1, FORMATTER);
        LocalDate f2 = LocalDate.parse(fecha2, FORMATTER);
        return f1.isBefore(f2);
    }

    public static boolean isAfter(String fecha1, String fecha2) {
        LocalDate f1 = LocalDate.parse(fecha1, FORMATTER);
        LocalDate f2 = LocalDate.parse(fecha2, FORMATTER);
        return f1.isAfter(f2);
    }

    public static boolean isEqual(String fecha1, String fecha2) {
        LocalDate f1 = LocalDate.parse(fecha1, FORMATTER);
        LocalDate f2 = LocalDate.parse(fecha2, FORMATTER);
        return f1.isEqual(f2);
    }

    public static LocalDate toDate(final String fechaString) {
        DateTimeFormatter parseador = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(fechaString, parseador);
    }

    public static String toTexto(Date fecha) {
        if (fecha == null) return null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(fecha);
    }

    public static LocalDateTime formatearString(final String fechaZonaHorarioString) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(fechaZonaHorarioString);
        return offsetDateTime.toLocalDateTime();
    }

    /**
     * Genera la fecha y hora actual como texto en formato dd-MM-yyyy HH:mm:ss para la zona horaria de Lima.
     *
     * @return Fecha y hora actual como cadena (formato corto).
     */
    public static String obtenerFechaHoraActualComoTextoCorto() {
        ZoneId zoneId = ZoneId.of("America/Lima");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return zonedDateTime.format(formatter);
    }

    /**
     * Genera la fecha y hora actual como texto en formato MySQL (yyyy-MM-dd HH:mm:ss) para la zona horaria de Lima.
     *
     * @return Fecha y hora actual como cadena en formato MySQL.
     */
    public static String obtenerFechaHoraActualComoTextoMySQL() {
        ZoneId zoneId = ZoneId.of("America/Lima");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return zonedDateTime.format(formateador);
    }

    /**
     * Convierte una fecha en formato dd-MM-yyyy a formato MySQL yyyy-MM-dd.
     *
     * @param fechaOriginal Fecha como cadena en formato dd-MM-yyyy.
     * @return Fecha formateada como cadena en formato yyyy-MM-dd.
     */
    public static String convertirFechaAFormatoMySQL(final String fechaOriginal) {
        DateTimeFormatter formatoOriginal = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate fecha = LocalDate.parse(fechaOriginal, formatoOriginal);
        DateTimeFormatter formatoNuevo = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return fecha.format(formatoNuevo);
    }

    public static OffsetDateTime parseUtcStringToOffsetDateTime(final String fechaIso) {
        return OffsetDateTime.parse(fechaIso)
                .withOffsetSameInstant(ZoneOffset.UTC);
    }

}
