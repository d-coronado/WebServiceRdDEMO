package org.dcoronado.WebServiceRdDemo.Shared.Infraestructure.Api;

import org.dcoronado.WebServiceRdDemo.Shared.Domain.Response.CustomResponse;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public abstract class AbstractApi {

    private static final Logger log = LoggerFactory.getLogger(AbstractApi.class);

    public static ResponseEntity<CustomResponse> success(Object data) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CustomResponse(
                        HttpStatus.OK.value(),
                        data,
                        "OK",
                        LocalDateTime.now()
                ));
    }

    public static ResponseEntity<CustomResponse> success(Object data, String message) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CustomResponse(
                        HttpStatus.OK.value(),
                        data,
                        message,
                        LocalDateTime.now()
                ));
    }

    public static ResponseEntity<ErrorResponse> error(Exception ex, HttpStatus status) {
        log.error("Ocurrió una excepción: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                ex.getCause() != null ? ex.getCause().toString() : "",
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(errorResponse);
    }
}
