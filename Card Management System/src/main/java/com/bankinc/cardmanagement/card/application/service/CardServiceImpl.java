package com.bankinc.cardmanagement.card.application.service;

import com.bankinc.cardmanagement.card.infrastructure.dto.CardActivationRequestDTO;
import com.bankinc.cardmanagement.card.infrastructure.dto.CardBalanceRequestDTO;
import com.bankinc.cardmanagement.card.infrastructure.dto.CardBalanceResponseDTO;
import com.bankinc.cardmanagement.card.infrastructure.dto.CardNumberResponseDTO;
import com.bankinc.cardmanagement.card.domain.model.Card;
import com.bankinc.cardmanagement.card.infrastructure.exception.CardNotFoundException;
import com.bankinc.cardmanagement.card.domain.repository.CardRepository;
import com.bankinc.cardmanagement.card.domain.service.CardDomainService;
import com.bankinc.cardmanagement.card.domain.service.CardFactoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardFactoryService cardFactoryService;
    private final CardDomainService cardDomainService;

    /**
     * Generates a card number for the specified product and saves it to the repository.
     *
     * @param productId the ID of the product for which to generate a card number
     * @return a response DTO containing the generated card number
     */
    @Override
    public CardNumberResponseDTO generateCardNumber(String productId) {
        String cardNumber = String.valueOf(cardFactoryService.generateCardNumber(productId));
        log.info("Generated card number: {}", cardNumber);

        Card newCard = cardFactoryService.createNewCard(cardNumber);
        cardRepository.save(newCard);

        return new CardNumberResponseDTO(cardNumber);
    }

    /**
     * Activates a card based on the provided activation request.
     *
     * @param request the activation request containing necessary details to activate the card
     */
    @Override
    @Transactional
    public void activateCard(CardActivationRequestDTO request) {
        Card card = cardRepository.findByCardNumber(request.getCardId())
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        cardDomainService.validateCardForActivation(card);
        cardDomainService.activateCard(card);

        cardRepository.save(card);
        log.info("Card {} activated successfully", request.getCardId());
    }

    /**
     * Blocks a card identified by its ID.
     *
     * @param cardId the ID of the card to be blocked
     */
    @Override
    @Transactional
    public void blockCard(Long cardId) {
        Card card = cardRepository.findByCardNumber(String.valueOf(cardId))
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        cardDomainService.validateCardForBlocking(card);
        cardDomainService.blockCard(card);

        cardRepository.save(card);
        log.info("Card {} blocked successfully", cardId);
    }

    /**
     * Adds balance to a card based on the provided balance request.
     *
     * @param request the balance request containing the amount to add
     */
    @Override
    @Transactional
    public void addBalance(CardBalanceRequestDTO request) {
        Card card = cardRepository.findByCardNumber(request.getCardId())
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        cardDomainService.validateCardForAddingBalance(card, request.getBalance());
        cardDomainService.addBalance(card, request.getBalance());

        cardRepository.save(card);
        log.info("Added balance of {} to card {}", request.getBalance(), request.getCardId());
    }

    /**
     * Retrieves the balance of a card identified by its ID.
     *
     * @param cardId the ID of the card whose balance is to be retrieved
     * @return a response DTO containing the card balance
     */
    @Override
    public CardBalanceResponseDTO getBalance(Long cardId) {
        Card card = cardRepository.findByCardNumber(String.valueOf(cardId))
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        log.info("Retrieved balance for card {}: {}", cardId, card.getBalance());
        return new CardBalanceResponseDTO(card.getBalance());
    }
}
