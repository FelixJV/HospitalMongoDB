package org.example.domain.errors;

public class ForeignKeyException extends RuntimeException {
    public ForeignKeyException(String message) {
        super(message);
    }
}
