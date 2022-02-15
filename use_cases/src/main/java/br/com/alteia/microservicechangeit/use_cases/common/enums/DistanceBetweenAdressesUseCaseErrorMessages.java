package br.com.alteia.microservicechangeit.use_cases.common.enums;

import br.com.alteia.microservicechangeit.common.enums.enum_utils.EnumUtils;

public enum DistanceBetweenAdressesUseCaseErrorMessages {

    DISTANCE_BETWEEN_ADRESSES_USE_CASE_ERROR_MESSAGES_NOT_FOUND(getCode(1), "A distância entre o CEP origem: (%s) e CEP destino: (%s) não foi encontrada"),
    DISTANCE_BETWEEN_ADRESSES_USE_CASE_ERROR_MESSAGES_INVALID_ORIGIN(getCode(2), "CEP origem: (%s) não foi encontrado"),
    DISTANCE_BETWEEN_ADRESSES_USE_CASE_ERROR_MESSAGES_INVALID_DESTINATION(getCode(3), "CEP destino: (%s) não foi encontrado"),
    DISTANCE_BETWEEN_ADRESSES_USE_CASE_ERROR_MESSAGES_GENERIC(getCode(4), "Problemas na integração com a google. Status: %s, Mensagem: %s");

    String code;
    String message;

    DistanceBetweenAdressesUseCaseErrorMessages(String code, String message) {
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
        return "DISTNC" + EnumUtils.formatCode(code);
    }
}