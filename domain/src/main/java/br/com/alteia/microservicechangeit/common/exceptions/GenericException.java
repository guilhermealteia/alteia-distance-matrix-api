package br.com.alteia.microservicechangeit.common.exceptions;

public abstract class GenericException extends RuntimeException {

    private final String code;

    protected GenericException(String code, String format, Object... values) {
        super(String.format(format, values));
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
