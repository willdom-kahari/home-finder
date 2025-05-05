package com.waduclay.homefinder.shared.exceptions;

/**
 * Exception thrown when validation of input data fails.
 * This exception is used to indicate that the input data provided to a domain operation
 * does not meet the required validation rules.
 *
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class ValidationException extends DomainException {

    /**
     * Creates a new validation exception with the specified message.
     *
     * @param message the detail message
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Creates a new validation exception with the specified message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
