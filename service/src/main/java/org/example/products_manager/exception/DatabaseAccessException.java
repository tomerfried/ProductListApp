package org.example.products_manager.exception;

import org.springframework.dao.DataAccessException;

public class DatabaseAccessException extends DataAccessException {
    public DatabaseAccessException(String message) {
        super(message);
    }
}