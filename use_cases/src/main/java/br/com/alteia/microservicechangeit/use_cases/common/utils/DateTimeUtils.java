package br.com.alteia.microservicechangeit.use_cases.common.utils;

import br.com.alteia.microservicechangeit.use_cases.common.exceptions.InvalidRequestException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import static br.com.alteia.microservicechangeit.common.enums.EntityFieldsValidationsErrorMessages.INVALID_DATE;
import static br.com.alteia.microservicechangeit.use_cases.common.utils.FieldPatterns.DATE_ISO8601;

public class DateTimeUtils {

    private DateTimeUtils() {
    }

    public static LocalDate fromStringToLocalDateISO601(String dateInString) {
        if (Objects.isNull(dateInString)) {
            return null;
        }

        try {
            return LocalDate.parse(dateInString, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException ex) {
            throw new InvalidRequestException(INVALID_DATE.getCode(), INVALID_DATE.getMessage(), DATE_ISO8601);
        }
    }
}
