package br.com.alteia.microservicechangeit.common.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidEntityExceptionUTest {

    @Test
    void validate_class_have_required_methods() {
        try {
            throw new InvalidEntityException("SOMECODE", "Some message %s", "word");
        } catch (GenericException ex) {
            assertEquals("SOMECODE", ex.getCode());
            assertEquals("Some message word", ex.getMessage());
        }
    }
}