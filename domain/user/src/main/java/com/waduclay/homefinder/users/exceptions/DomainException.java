package com.waduclay.homefinder.users.exceptions;

/**
 * Base exception class for all domain-specific exceptions.
 * This class extends RuntimeException to avoid forcing clients to catch exceptions
 * that they might not be able to recover from.
 * 
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public abstract class DomainException extends RuntimeException {
    
    /**
     * Creates a new domain exception with the specified message.
     * 
     * @param message the detail message
     */
    protected DomainException(String message) {
        super(message);
    }
    
    /**
     * Creates a new domain exception with the specified message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    protected DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
