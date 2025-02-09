package com.javacode.testtask.service;

import com.javacode.testtask.exceptions.InsufficientFundsException;
import com.javacode.testtask.exceptions.WalletNotFoundException;
import com.javacode.testtask.model.WalletModel;
import com.javacode.testtask.repository.WalletRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {
    @Autowired
    private final @NotNull WalletRepository walletRepository;

    @Transactional
    public void save(@NonNull final WalletModel wallet) {
        this.walletRepository.saveAndFlush(wallet);
    }

    public @Nullable WalletModel getWallet(@NonNull final UUID walletId) {
        return this.walletRepository.findById(walletId).orElseThrow(() -> new WalletNotFoundException("Wallet not found", walletId));
    }

    @Transactional(readOnly = true)
    public double getBalance(@NonNull final UUID walletId) throws WalletNotFoundException {
        @Nullable val wallet = this.getWallet(walletId);

        if (wallet == null)
            throw new WalletNotFoundException("Wallet not found", walletId);

        return wallet.getBalance();
    }

    @Transactional
    public void deposit(@NonNull final UUID walletId, final double amount) {
        @Nullable val wallet = this.getWallet(walletId);

        if (wallet == null)
            throw new WalletNotFoundException("Wallet not found", walletId);

        wallet.setBalance(wallet.getBalance() + amount);

        this.walletRepository.save(wallet);
    }

    @Transactional
    public void withdraw(@NotNull final UUID walletId, final double amount) {
        @Nullable val wallet = this.getWallet(walletId);

        if (wallet == null)
            throw new WalletNotFoundException("Wallet not found", walletId);

        val balance = wallet.getBalance();

        if (balance < amount)
            throw new InsufficientFundsException("Insufficient funds");

        wallet.setBalance(balance - amount);

        this.walletRepository.save(wallet);
    }
}