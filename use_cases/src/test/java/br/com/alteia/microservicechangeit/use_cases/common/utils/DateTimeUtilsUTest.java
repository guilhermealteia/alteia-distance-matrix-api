package br.com.alteia.microservicechangeit.use_cases.common.utils;

import br.com.alteia.microservicechangeit.use_cases.common.exceptions.InvalidRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static br.com.alteia.microservicechangeit.use_cases.common.utils.DateTimeUtils.fromStringToLocalDateISO601;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateTimeUtilsUTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void valid_date_from_string_to_tocalDate_ISO601() {
        // Dados os elementos abaixo
        LocalDate localDate = fromStringToLocalDateISO601("2000-12-31");
        assertEquals(LocalDate.parse("2000-12-31"), localDate);
    }

    @Test
    void invalid_date_from_string_to_tocalDate_ISO601() {
        // Dados os elementos abaixo
        assertThrows(InvalidRequestException.class, () -> fromStringToLocalDateISO601(""));
    }

    @Test
    void null_date_from_string_to_tocalDate_ISO601() {
        // Dados os elementos abaixo
        assertNull(fromStringToLocalDateISO601(null));
    }

}