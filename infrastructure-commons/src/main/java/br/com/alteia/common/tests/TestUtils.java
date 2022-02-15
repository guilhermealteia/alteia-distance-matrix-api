package br.com.alteia.common.tests;

import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.instrument.IllegalClassFormatException;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class TestUtils {

    private TestUtils() {
    }

    public static <T> Annotation getAnnotationFromField(Class<T> objectClass, String annnotationClass, String fieldName) throws IllegalClassFormatException {
        List<Annotation> annotations = List.of(requireNonNull(Arrays.stream(objectClass.getDeclaredFields()).filter(
                f -> fieldName.equals(f.getName())
        ).findFirst().orElse(null)).getDeclaredAnnotations());

        return annotations.stream().filter(
                fieldAnnotations -> annnotationClass.equals(fieldAnnotations.annotationType().getName())
        ).findFirst().orElseThrow(IllegalClassFormatException::new);
    }

    public static String fromObjectToJson(Object object) {
        return new Gson().toJson(object);
    }
}
