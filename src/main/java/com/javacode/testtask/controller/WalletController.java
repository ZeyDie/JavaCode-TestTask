package com.javacode.testtask.controller;

import com.javacode.testtask.dto.WalletRequestDto;
import com.javacode.testtask.exceptions.InsufficientFundsException;
import com.javacode.testtask.exceptions.WalletNotFoundException;
import com.javacode.testtask.model.WalletModel;
import com.javacode.testtask.service.WalletService;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WalletController {
    @Getter
    @Autowired
    private final @NotNull WalletService walletService;

    @PostMapping("/wallet/create/{walletId}")
    public ResponseEntity<?> createWallet(@PathVariable @NonNull final UUID walletId) {
        if (this.getBalance(walletId).getStatusCode() == HttpStatus.NOT_FOUND) {
            @NonNull val wallet = new WalletModel(walletId, 0);

            this.walletService.save(wallet);

            return ResponseEntity.ok().build();
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wallet already exist");
    }

    @PostMapping("/wallet")
    public @NotNull ResponseEntity<?> updateBalance(@RequestBody @NonNull final WalletRequestDto requestDto) {
        try {
            @NonNull val walletId = requestDto.getWalletId();
            val amount = requestDto.getAmount();
            @NonNull val operation = requestDto.getOperation();

            switch (operation) {
                case DEPOSIT -> {
                    this.walletService.deposit(walletId, amount);
                    return ResponseEntity.ok().build();
                }
                case WITHDRAW -> {
                    this.walletService.withdraw(walletId, amount);
                    return ResponseEntity.ok().build();
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad operation");
                }
            }
        } catch (final InsufficientFundsException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient funds");
        } catch (final WalletNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wallet not found");
        }
    }

    @GetMapping("/wallets/{walletId}")
    public @NotNull ResponseEntity<Double> getBalance(@PathVariable @NonNull final UUID walletId) {
        try {
            val balance = this.walletService.getBalance(walletId);

            return ResponseEntity.ok().body(balance);
        } catch (final WalletNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
