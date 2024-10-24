package com.bankinc.cardmanagement.transaction.domain.service;

import com.bankinc.cardmanagement.card.domain.model.Card;
import com.bankinc.cardmanagement.transaction.domain.model.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionFactory {

    /**
     * Creates a new transaction with the given card and amount.
     *
     * @param card   the card associated with the transaction
     * @param amount the amount of the transaction
     * @return a new Transaction object
     */
    public Transaction createTransaction(Card card, double amount) {
        Transaction transaction = new Transaction();
        transaction.setCard(card);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAnnulled(false);

        return transaction;
    }
}