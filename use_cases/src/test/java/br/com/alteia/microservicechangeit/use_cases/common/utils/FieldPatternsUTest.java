package br.com.alteia.microservicechangeit.use_cases.common.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldPatternsUTest {

    @Test
    void iso8601_date_should_be_as_excpected() {
        assertEquals("yyyy-MM-dd", FieldPatterns.DATE_ISO8601);
    }

    @Test
    void iso8601_datetime_should_be_as_excpected() {
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSz", FieldPatterns.DATE_AND_TIME_ISO8601);
    }

}