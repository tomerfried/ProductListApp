package org.example.products_manager.exception;

public class InvalidProductRequestException extends RuntimeException {
    public InvalidProductRequestException(String message) {
        super(message);
    }
}