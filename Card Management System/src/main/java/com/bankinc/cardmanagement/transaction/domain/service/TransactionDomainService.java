package com.bankinc.cardmanagement.transaction.domain.service;

import com.bankinc.cardmanagement.card.domain.model.Card;
import com.bankinc.cardmanagement.card.infrastructure.exception.CardBlockedException;
import com.bankinc.cardmanagement.transaction.infrastructure.exception.InsufficientBalanceException;
import org.springframework.stereotype.Service;

@Service
public class TransactionDomainService {


    /**
     * Validates whether a purchase can be made with the given card and price.
     *
     * @param card  the card to validate
     * @param price the price of the purchase
     * @throws CardBlockedException      if the card is not active or is blocked
     * @throws InsufficientBalanceException if the card balance is insufficient
     */
    public void validateForPurchase(Card card, double price) {
        if (!card.isActive() || card.isBlocked()) {
            throw new CardBlockedException("Card is not active or is blocked");
        }
        if (card.getBalance() < price) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
    }

    /**
     * Deducts the specified price from the card's balance.
     *
     * @param card  the card from which to deduct the balance
     * @param price the amount to deduct
     */
    public void deductBalance(Card card, double price) {
        card.setBalance(card.getBalance() - price);
    }

}
