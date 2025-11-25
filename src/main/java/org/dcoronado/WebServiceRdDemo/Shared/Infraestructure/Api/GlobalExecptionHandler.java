package org.dcoronado.WebServiceRdDemo.Shared.Infraestructure.Api;

import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.*;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption.IllegalStateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExecptionHandler {

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<?> handleInvalidArgument(InvalidArgumentException ex) {
        return AbstractApi.error(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex) {
        return AbstractApi.error(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleAlreadyExists(IllegalStateException ex) {
        return AbstractApi.error(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<?> handleInfrastructure(InfrastructureException ex) {
        return AbstractApi.error(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return AbstractApi.error(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
