package com.waduclay.homefinder.shared.exceptions;

/**
 * Exception thrown when attempting to create a user with a username that already exists.
 * This exception is used to indicate that a user with the specified username already exists
 * in the system, and therefore a new user with the same username cannot be created.
 * 
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class UserAlreadyExistsException extends DomainException {
    
    /**
     * Creates a new UserAlreadyExistsException with the specified message.
     * 
     * @param message the detail message
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
    
    /**
     * Creates a new UserAlreadyExistsException with the specified message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
