package com.bankinc.cardmanagement.transaction.infrastructure.exception;

public class TransactionAnnulationPeriodExceededException extends RuntimeException {
    public TransactionAnnulationPeriodExceededException(String message) {
        super(message);
    }
}
