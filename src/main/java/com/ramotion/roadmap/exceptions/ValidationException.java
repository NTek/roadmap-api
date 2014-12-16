package com.ramotion.roadmap.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Validation error")
public class ValidationException extends RuntimeException {

    private HashMap<String, Object> errors;

    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException withError(String field, Object error) {
        if (this.errors == null) {
            errors = new HashMap<>();
        }
        errors.put(field, error);
        return this;
    }

    public ValidationException withErrorMap(HashMap<String, Object> errors) {
        this.errors = errors;
        return this;
    }

    public ValidationException withBindingResult(BindingResult bindingResult) {
        if (bindingResult != null) {
            this.errors = new HashMap<>(bindingResult.getFieldErrorCount());
            for (FieldError err : bindingResult.getFieldErrors()) {
                errors.put(err.getField(), err.getDefaultMessage());
            }
        }
        return this;
    }

    public HashMap<String, Object> getErrors() {
        return errors;
    }

    public void setErrors(HashMap<String, Object> errors) {
        this.errors = errors;
    }
}
