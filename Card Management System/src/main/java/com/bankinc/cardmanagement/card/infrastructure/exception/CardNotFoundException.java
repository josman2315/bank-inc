package com.bankinc.cardmanagement.card.infrastructure.exception;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(String message) {
        super(message);
    }
}
