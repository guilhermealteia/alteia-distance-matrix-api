package br.com.alteia.common.exceptions.enums;

import br.com.alteia.common.utils.EnumUtils;

public enum SecurityErrorMessages {

    JWT_INVALID_SIGNATURE(getCode(1), "Assinatura inválida"),
    JWT_INVALID_TOKEN(getCode(2), "Token inválido"),
    JWT_EXPIRED(getCode(3), "JWT expirado"),
    JWT_UNSUPPORTED(getCode(4), "JWT não suportado"),
    JWT_EMPTY_CLAIMS(getCode(5), "JWT claims não pode ser vazio");

    private final String code;
    private final String message;

    SecurityErrorMessages(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    private static String getCode(Integer code) {
        return "JWT" + EnumUtils.formatCode(code);
    }
}
