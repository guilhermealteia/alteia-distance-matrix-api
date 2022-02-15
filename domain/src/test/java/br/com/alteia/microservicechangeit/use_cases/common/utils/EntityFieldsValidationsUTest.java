package br.com.alteia.microservicechangeit.use_cases.common.utils;

import br.com.alteia.microservicechangeit.common.exceptions.InvalidEntityException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static br.com.alteia.microservicechangeit.use_cases.common.utils.EntityFieldsValidations.ONLY_LETTERS_PATTERN;
import static br.com.alteia.microservicechangeit.use_cases.common.utils.EntityFieldsValidations.isCpfValid;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EntityFieldsValidationsUTest {

    @Nested
    class validateStandardizedSizedString {
        @Test
        void standardizedSizedStringSucess() {
            String value = "teste field";
            assertDoesNotThrow(() -> EntityFieldsValidations.standardizedSizedString("name", value, 1, 100));
        }

        @Test
        void standardizedSizedStringMinLenghtError() {
            String value = "teste field";
            assertThrows(InvalidEntityException.class, () -> EntityFieldsValidations.standardizedSizedString("name", value, ONLY_LETTERS_PATTERN, 50, 100));
        }

        @Test
        void standardizedSizedStringMaxLenghtError() {
            String value = "teste field";
            assertThrows(InvalidEntityException.class, () ->
                    EntityFieldsValidations.standardizedSizedString(
                            "name", value, ONLY_LETTERS_PATTERN, 1, 5)
            );
        }
    }

    @Nested
    class validateStandardizedPatternedString {
        @Test
        void standardizedSizedStringWithPatternSucess() {
            String value = "teste field";
            assertDoesNotThrow(() -> EntityFieldsValidations.standardizedSizedString("name", value, ONLY_LETTERS_PATTERN, 1, 100));
        }

        @Test
        void standardizedSizedStringWithPatternError() {
            String value = "teste field1";
            assertThrows(InvalidEntityException.class,
                    () -> EntityFieldsValidations.standardizedSizedString(
                            "name", value, ONLY_LETTERS_PATTERN, 1, 100)
            );
        }
    }

    @Nested
    class validateNotNull {
        @Test
        void notNullStringSucess() {
            String value = "teste field";
            assertDoesNotThrow(() -> EntityFieldsValidations.notNull(
                    "name", value)
            );
        }

        @Test
        void notNullStringError() {
            String value = null;
            assertThrows(InvalidEntityException.class,
                    () -> EntityFieldsValidations.notNull(
                            "name", value)
            );
        }
    }

    @Nested
    class validateMinLength {

        @Test
        void minLengthAsExpected() {
            assertDoesNotThrow(() -> EntityFieldsValidations.minValue("value", 100, 1));
        }

        @Test
        void minLengthAsNotExpected() {
            assertThrows(InvalidEntityException.class, () -> EntityFieldsValidations.minValue("value", -100, 1));
        }
    }

    @Nested
    class validateIsCpfValid {
        @ParameterizedTest
        @ValueSource(strings = {"718.531.060-11", "00762197048", "894.618.700-00"})
        void isValidCpf(String cpf) {
            assertDoesNotThrow(() -> isCpfValid(cpf));
        }

        @ParameterizedTest
        @ValueSource(strings = {"71889923922", "0762197048", "00000000000"})
        void isNotValidCpf(String cpf) {
            assertThrows(InvalidEntityException.class, () -> isCpfValid(cpf));
        }

        @Test
        void nullCpf() {
            assertThrows(InvalidEntityException.class, () -> isCpfValid(null));
        }
    }
}