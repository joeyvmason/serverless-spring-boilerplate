package com.joeyvmason.serverless.spring.application.exceptions;

public class NotFoundException extends Exception {
    private static final long serialVersionUID = -5360146086225284938L;

    public NotFoundException() {
        super("Resource not found");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
