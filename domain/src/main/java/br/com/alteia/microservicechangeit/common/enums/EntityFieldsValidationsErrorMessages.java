package br.com.alteia.microservicechangeit.common.enums;

import br.com.alteia.microservicechangeit.common.enums.enum_utils.EnumUtils;

public enum EntityFieldsValidationsErrorMessages {

    PATTERN_MISMATCH(getCode(1), "O campo %s deve atender ao padrão: %s"),
    FIELD_LENGTH(getCode(2), "O tamanho do campo %s deve ser entre %s e %s"),
    FIELD_NOT_EMPTY(getCode(3), "O campo %s não pode ser vazio"),
    FIELD_MIN_LENGTH(getCode(4), "O campo %s não pode um valor menor a %s"),
    INVALID_DATE(getCode(5), "Formato de data inválido. Deve corresponder a: %s"),
    INVALID_CPF(getCode(6), "Formato de cpf inválido para o cpf informado: %s");

    private final String code;
    private final String message;

    EntityFieldsValidationsErrorMessages(String code, String message) {
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
        return "FLDVLDTNS" + EnumUtils.formatCode(code);
    }
}
