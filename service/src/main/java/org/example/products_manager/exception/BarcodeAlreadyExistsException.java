package org.example.products_manager.exception;

public class BarcodeAlreadyExistsException extends RuntimeException {
    public BarcodeAlreadyExistsException(String message) {
        super(message);
    }
}