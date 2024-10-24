package com.bankinc.cardmanagement.card.infrastructure.exception;

public class CardBlockedException extends RuntimeException {
    public CardBlockedException(String message) {
        super(message);
    }
}
