package com.waigel.healthcard.exceptions;

/**
 * Exception thrown when a health card action fails.
 */
public class HealthCardExceptionJava extends Exception {
    /**
     * Constructor.of the exception.
     * @param message The message of the exception.
     */
    public HealthCardExceptionJava(String message) {
        super(message);
    }
}
