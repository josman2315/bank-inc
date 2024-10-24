package com.bankinc.cardmanagement.card.domain.service;

import com.bankinc.cardmanagement.card.infrastructure.dto.CardNumberResponseDTO;
import com.bankinc.cardmanagement.card.domain.model.Card;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CardFactoryService {

    private final Random random = new Random();


    /**
     * Generates a card number based on the provided product ID.
     *
     * @param productId the product ID to be included in the card number
     * @return a DTO containing the generated card number
     */
    public CardNumberResponseDTO generateCardNumber(String productId) {
        String cardNumber = productId + String.format("%010d", random.nextInt(1000000000));
        return new CardNumberResponseDTO(cardNumber);
    }

    /**
     * Creates a new card with the specified card number and default values.
     *
     * @param cardNumber the card number to assign to the new card
     * @return a newly created Card object
     */
    public Card createNewCard(String cardNumber) {
        Card newCard = new Card();
        newCard.setCardNumber(cardNumber);
        newCard.setHolderName(null);
        newCard.setActive(false);
        newCard.setBlocked(false);
        newCard.setBalance(0.0);
        newCard.setExpirationDate(null);
        return newCard;
    }
}
