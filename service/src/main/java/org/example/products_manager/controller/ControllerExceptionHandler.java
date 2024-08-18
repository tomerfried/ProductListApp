package org.example.products_manager.controller;

import org.example.products_manager.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = ProductsManagerController.class)
public class ControllerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(InvalidProductRequestException.class)
    public ResponseEntity<String> handleInvalidProductRequestException(InvalidProductRequestException e) {
        logger.error("Invalid product request", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(InvalidBarcodeException.class)
    public ResponseEntity<String> handleInvalidBarcodeException(InvalidBarcodeException e) {
        logger.error("Invalid barcode", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(BarcodeAlreadyExistsException.class)
    public ResponseEntity<String> handleBarcodeAlreadyExistsException(BarcodeAlreadyExistsException e) {
        logger.error("Barcode already exists", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(DatabaseAccessException.class)
    public ResponseEntity<String> handleDatabaseAccessException(DatabaseAccessException e) {
        logger.error("Database access error", e);
        if (e.getCause() != null) {
            logger.error("Underlying cause: " + e.getCause().getMessage(), e.getCause());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException e) {
        logger.error("Product not found", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}