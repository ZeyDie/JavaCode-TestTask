package com.javacode.testtask.exceptions;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public final class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(@NonNull final String message, @Nullable final UUID walletId) {
        super(message + ": " + walletId);
    }
}