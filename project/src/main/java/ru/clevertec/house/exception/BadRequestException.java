package ru.clevertec.house.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public static BadRequestException of(Class<?> clazz, Object field) {
        return new BadRequestException("Bad field " + field + " for " + clazz.getSimpleName() + " object");
    }
}
