package br.com.alteia.microservicechangeit.use_cases.common.utils;

import br.com.alteia.microservicechangeit.common.exceptions.InvalidEntityException;

import java.math.BigDecimal;
import java.util.Objects;

import static br.com.alteia.microservicechangeit.common.enums.EntityFieldsValidationsErrorMessages.FIELD_LENGTH;
import static br.com.alteia.microservicechangeit.common.enums.EntityFieldsValidationsErrorMessages.FIELD_MIN_LENGTH;
import static br.com.alteia.microservicechangeit.common.enums.EntityFieldsValidationsErrorMessages.FIELD_NOT_EMPTY;
import static br.com.alteia.microservicechangeit.common.enums.EntityFieldsValidationsErrorMessages.INVALID_CPF;
import static br.com.alteia.microservicechangeit.common.enums.EntityFieldsValidationsErrorMessages.PATTERN_MISMATCH;
import static java.util.Objects.isNull;

public class EntityFieldsValidations {

    private EntityFieldsValidations() {
    }

    public static final String ONLY_LETTERS_PATTERN = "(?i)^([-'a-zÀ-ÿ ])+$";

    public static void standardizedString(String fieldName, String value, String pattern) {
        if (!value.matches(pattern)) {
            throw new InvalidEntityException(PATTERN_MISMATCH.getCode(), PATTERN_MISMATCH.getMessage(), fieldName, pattern);
        }
    }

    public static void standardizedSizedString(String fieldName, String value, String pattern, int minSize, int maxSize) {
        boolean isValid = Objects.nonNull(value) && !value.isBlank() && value.length() >= minSize && value.length() <= maxSize;

        if (!isValid) {
            throw new InvalidEntityException(FIELD_LENGTH.getCode(), FIELD_LENGTH.getMessage(), fieldName, minSize, maxSize);
        } else if (Objects.nonNull(pattern)) {
            standardizedString(fieldName, value, pattern);
        }
    }

    public static void standardizedSizedString(String fieldName, String value, int minSize, int maxSize) {
        standardizedSizedString(fieldName, value, null, minSize, maxSize);
    }

    public static void notNull(String fieldName, Object value) {
        if (isNull(value)) {
            throw new InvalidEntityException(FIELD_NOT_EMPTY.getCode(), FIELD_NOT_EMPTY.getMessage(), fieldName);
        }
    }

    public static void minValue(String fieldName, Object numericalValue, Object minValue) {
        BigDecimal numericalValueParsed = new BigDecimal(numericalValue.toString());
        BigDecimal minValueParsed = new BigDecimal(minValue.toString());

        if (numericalValueParsed.compareTo(minValueParsed) < 0) {
            throw new InvalidEntityException(FIELD_MIN_LENGTH.getCode(), FIELD_MIN_LENGTH.getMessage(), fieldName, minValue);
        }
    }

    public static void minLength(String fieldName, String fieldValue, int minValue) {
        if (fieldValue.length() < minValue) {
            throw new InvalidEntityException(FIELD_MIN_LENGTH.getCode(), FIELD_MIN_LENGTH.getMessage(), fieldName, minValue);
        }
    }

    public static void isCpfValid(String cpf) {
        if (isNull(cpf)) {
            throwInvalidCpfException(null);
        }
        cpf = cpf.replaceAll("\\.|-", "");
        if (cpf.matches(cpf.substring(0, 1).repeat(11)) || (cpf.length() != 11))
            throwInvalidCpfException(cpf);

        char dig10;
        char dig11;

        int sm;
        int i;
        int r;
        int num;
        int peso;

        // Calculo do 1o. Digito Verificador
        sm = 0;
        peso = 10;
        for (i = 0; i < 9; i++) {
            // converte o i-esimo caractere do CPF em um numero:
            // por exemplo, transforma o caractere '0' no inteiro 0
            // (48 eh a posicao de '0' na tabela ASCII)
            num = (cpf.charAt(i) - 48);
            sm = sm + (num * peso);
            peso = peso - 1;
        }

        r = 11 - (sm % 11);
        if ((r == 10) || (r == 11))
            dig10 = '0';
        else dig10 = (char) (r + 48); // converte no respectivo caractere numerico

        // Calculo do 2o. Digito Verificador
        sm = 0;
        peso = 11;
        for (i = 0; i < 10; i++) {
            num = (cpf.charAt(i) - 48);
            sm = sm + (num * peso);
            peso = peso - 1;
        }

        r = 11 - (sm % 11);
        if ((r == 10) || (r == 11))
            dig11 = '0';
        else dig11 = (char) (r + 48);

        // Verifica se os digitos calculados conferem com os digitos informados.
        if ((dig10 != cpf.charAt(9)) || (dig11 != cpf.charAt(10))) {
            throwInvalidCpfException(cpf);
        }
    }

    private static void throwInvalidCpfException(String cpf) {
        throw new InvalidEntityException(INVALID_CPF.getCode(), INVALID_CPF.getMessage(), cpf);
    }
}
