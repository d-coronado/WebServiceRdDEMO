package org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
