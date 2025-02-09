package com.javacode.testtask.exceptions;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public final class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(@NonNull final String message) {
        super(message);
    }
}