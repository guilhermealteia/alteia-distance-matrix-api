package br.com.alteia.microservicechangeit.common.exceptions;

public class InvalidEntityException extends GenericException {
    public InvalidEntityException(String code, String format, Object... values) {
        super(code, format, values);
    }
}
