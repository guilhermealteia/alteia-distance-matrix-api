package br.com.alteia.microservicechangeit.use_cases.common.exceptions;

import br.com.alteia.microservicechangeit.common.exceptions.GenericException;

public class InvalidRequestException extends GenericException {
    public InvalidRequestException(String code, String format, Object... values) {
        super(code, format, values);
    }
}
