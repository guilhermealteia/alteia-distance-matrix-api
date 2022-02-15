package br.com.alteia.microservicechangeit.use_cases.common.exceptions;

import br.com.alteia.microservicechangeit.common.exceptions.GenericException;

public class NotFoundException extends GenericException {
    public NotFoundException(String code, String format, Object... values) {
        super(code, String.format(format, values));
    }
}
