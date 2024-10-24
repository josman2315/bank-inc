package com.bankinc.cardmanagement.card.domain.service;

import com.bankinc.cardmanagement.card.domain.model.Card;
import com.bankinc.cardmanagement.card.infrastructure.exception.CardAlreadyActivatedException;
import com.bankinc.cardmanagement.card.infrastructure.exception.CardBlockedException;
import com.bankinc.cardmanagement.transaction.infrastructure.exception.InsufficientBalanceException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CardDomainService {

    /**
     * Validates whether a card can be activated.
     *
     * @param card the card to validate
     * @throws CardAlreadyActivatedException if the card is already active
     * @throws CardBlockedException if the card is blocked
     */
    public void validateCardForActivation(Card card) {
        if (card.isActive()) {
            throw new CardAlreadyActivatedException("Card is already activated");
        }
        if (card.isBlocked()) {
            throw new CardBlockedException("Blocked card cannot be activated");
        }
    }

    /**
     * Activates the specified card.
     *
     * @param card the card to activate
     */
    public void activateCard(Card card) {
        card.setActive(true);
        card.setExpirationDate(LocalDate.now().plusYears(3));
    }

    /**
     * Validates whether a card can be blocked.
     *
     * @param card the card to validate
     * @throws CardBlockedException if the card is inactive or already blocked
     */
    public void validateCardForBlocking(Card card) {
        if (!card.isActive()) {
            throw new CardBlockedException("Inactive card cannot be blocked");
        }
        if (card.isBlocked()) {
            throw new CardBlockedException("Card is already blocked");
        }
    }

    /**
     * Blocks the specified card.
     *
     * @param card the card to block
     */
    public void blockCard(Card card) {
        card.setActive(false);
        card.setBlocked(true);
    }

    /**
     * Validates whether balance can be added to the card.
     *
     * @param card the card to validate
     * @param balanceToAdd the amount to add to the card balance
     * @throws CardBlockedException if the card is blocked
     * @throws InsufficientBalanceException if the resulting balance would be negative
     */
    public void validateCardForAddingBalance(Card card, double balanceToAdd) {
        if (card.isBlocked()) {
            throw new CardBlockedException("Blocked card cannot have balance added");
        }
        if (card.getBalance() + balanceToAdd < 0) {
            throw new InsufficientBalanceException("Balance cannot be negative");
        }
    }

    /**
     * Adds balance to the specified card.
     *
     * @param card the card to add balance to
     * @param balanceToAdd the amount to add
     */
    public void addBalance(Card card, double balanceToAdd) {
        card.setBalance(card.getBalance() + balanceToAdd);
    }
}
