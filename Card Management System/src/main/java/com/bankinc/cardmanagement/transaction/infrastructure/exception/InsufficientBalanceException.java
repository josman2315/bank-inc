package com.bankinc.cardmanagement.transaction.infrastructure.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
