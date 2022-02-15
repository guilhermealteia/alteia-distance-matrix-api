package br.com.alteia.common.tests;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.LowerCase;

import java.lang.instrument.IllegalClassFormatException;

import static br.com.alteia.common.tests.TestUtils.fromObjectToJson;
import static br.com.alteia.common.tests.TestUtils.getAnnotationFromField;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestUtilsUTest {

    @Nested
    class AnnotationFromField {
        @LowerCase
        private String field;

        private String anotherField;

        @Test
        void validateAnnotationExistsInField() {
            assertDoesNotThrow(() -> getAnnotationFromField(this.getClass(), LowerCase.class.getName(), "field"));
        }

        @Test
        void validateAnnotationNotExistsInField() {
            assertThrows(IllegalClassFormatException.class, () -> getAnnotationFromField(this.getClass(), LowerCase.class.getName(), "anotherField"));
        }
    }

    @Nested
    class FromObjectToJson {

        private final String field = "Test Field";

        @Test
        void validateAnnotationExistsInField() {
            String json = fromObjectToJson(new FromObjectToJson());
            assertEquals("{\"field\":\"Test Field\"}", json);
        }
    }
}
