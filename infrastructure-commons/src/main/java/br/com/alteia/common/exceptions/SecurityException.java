package br.com.alteia.common.exceptions;

public class SecurityException extends RuntimeException {

    private final String code;

    public SecurityException(String code, String format, Object... values) {
        super(String.format(format, values));
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}