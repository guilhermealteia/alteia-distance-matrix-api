package br.com.alteia.microservicechangeit.adapters.repositories.enums;

import br.com.alteia.microservicechangeit.common.enums.enum_utils.EnumUtils;

public enum DistanceBetweenAdressesRepositoryErrorMessages {

    DISTANCE_BETWEEN_ADRESSES_GENERIC(getCode(1), "Problemas na integração com a Google. Url: %s. Status retornado da Google: %s");

    String code;
    String message;

    DistanceBetweenAdressesRepositoryErrorMessages(String code, String message) {
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
        return "DISTREPO" + EnumUtils.formatCode(code);
    }
}