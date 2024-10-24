package com.bankinc.cardmanagement.transaction.infrastructure.exception;

public class TransactionAlreadyAnnulledException extends RuntimeException {
    public TransactionAlreadyAnnulledException(String message) {
        super(message);
    }
}
