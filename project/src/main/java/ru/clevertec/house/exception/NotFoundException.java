package ru.clevertec.house.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException of(Class<?> clazz, Object field) {
        return new NotFoundException(clazz.getSimpleName() + " with " + field + " not found");
    }
}
