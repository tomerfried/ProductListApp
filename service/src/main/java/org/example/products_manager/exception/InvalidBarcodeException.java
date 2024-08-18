package org.example.products_manager.exception;

public class InvalidBarcodeException extends RuntimeException {
    public InvalidBarcodeException(String message) {
        super(message);
    }
}