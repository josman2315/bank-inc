package com.bankinc.cardmanagement.transaction.infrastructure.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(String message) {
        super(message);
    }
}
