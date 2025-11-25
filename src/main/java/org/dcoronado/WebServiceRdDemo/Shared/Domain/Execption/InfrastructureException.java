package org.dcoronado.WebServiceRdDemo.Shared.Domain.Execption;

public class InfrastructureException extends DomainException {

    public InfrastructureException(String message) {
        super(message);
    }

    public InfrastructureException(String message, Throwable cause) {
        super(message, cause);
    }
}
