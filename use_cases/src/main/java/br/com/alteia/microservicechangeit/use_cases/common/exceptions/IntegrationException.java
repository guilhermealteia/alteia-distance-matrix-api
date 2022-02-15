package br.com.alteia.microservicechangeit.use_cases.common.exceptions;

import br.com.alteia.microservicechangeit.common.exceptions.GenericException;

public class IntegrationException extends GenericException {
    public IntegrationException(String code, String format, Object... values) {
        super(code, format, values);
    }
}
