package br.com.dio.util;

import br.com.dio.exception.ValidationException;

public final class ValidationUtils {

    private ValidationUtils() {}

    public static String requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(message);
        }
        return value;
    }

    public static long requirePositive(long value, String message) {
        if (value <= 0) {
            throw new ValidationException(message);
        }
        return value;
    }
}
