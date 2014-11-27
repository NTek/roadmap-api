package com.ramotion.roadmap.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Validation error")
public class ValidationException extends RuntimeException {

    private HashMap<String, String> errors;

    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException withErrorMap(HashMap<String, String> errors) {
        this.errors = errors;
        return this;
    }

    public HashMap<String, String> getErrors() {
        return errors;
    }

    public void setErrors(HashMap<String, String> errors) {
        this.errors = errors;
    }
}
