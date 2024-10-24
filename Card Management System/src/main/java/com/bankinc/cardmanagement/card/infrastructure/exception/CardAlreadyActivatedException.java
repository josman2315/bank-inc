package com.bankinc.cardmanagement.card.infrastructure.exception;

public class CardAlreadyActivatedException extends RuntimeException {
    public CardAlreadyActivatedException(String message) {
        super(message);
    }
}
