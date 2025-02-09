package com.javacode.testtask.repository;

import com.javacode.testtask.model.WalletModel;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<WalletModel, UUID> {
    @Override
    @NotNull
    Optional<WalletModel> findById(@NonNull final UUID walletId);
}