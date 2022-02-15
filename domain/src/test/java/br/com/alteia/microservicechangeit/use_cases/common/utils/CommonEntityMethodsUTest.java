package br.com.alteia.microservicechangeit.use_cases.common.utils;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CommonEntityMethodsUTest {

    @Nested
    class treatIdShould {
        @Test
        void treatIdNull() {
            assertDoesNotThrow(() -> CommonEntityMethods.treatUUID(null, ""));
        }

        @Test
        void treatIdAlreadyFilled() {
            assertDoesNotThrow(() -> CommonEntityMethods.treatUUID(UUID.randomUUID().toString(), ""));
        }

    }
}
