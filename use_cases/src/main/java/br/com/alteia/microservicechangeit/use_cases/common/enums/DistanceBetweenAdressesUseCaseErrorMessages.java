package br.com.alteia.microservicechangeit.use_cases.common.enums;

import br.com.alteia.microservicechangeit.common.enums.enum_utils.EnumUtils;

public enum DistanceBetweenAdressesUseCaseErrorMessages {

    DISTANCE_BETWEEN_ADRESSES_NOT_FOUND(getCode(1), "A distância entre o CEP origem: (%s) e CEP destino: (%s) não foi encontrada");

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
        return "DISTUC" + EnumUtils.formatCode(code);
    }
}