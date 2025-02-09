package com.javacode.testtask.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletRequestDto {
    private UUID walletId;
    private Operation operation;
    private double amount;

    public enum Operation {
        DEPOSIT,
        WITHDRAW;
    }
}