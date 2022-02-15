package br.com.alteia.common.exceptions;

public class CircuitException extends RuntimeException {

    private final String code;

    public CircuitException(String code, String format, Object... values) {
        super(String.format(format, values));
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}