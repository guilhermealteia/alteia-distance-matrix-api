package br.com.alteia.microservicechangeit.use_cases.common.utils;

import java.util.Objects;
import java.util.UUID;

public class CommonEntityMethods {

    private CommonEntityMethods() {
    }

    public static String treatUUID(String id, String givenId) {
        if (Objects.isNull(id)) {
            if (Objects.isNull(givenId)) {
                id = UUID.randomUUID().toString();
            } else {
                id = givenId;
            }
        }
        return id;
    }
}
