package com.bankinc.cardmanagement.card.application.service;


import com.bankinc.cardmanagement.card.infrastructure.dto.CardActivationRequestDTO;
import com.bankinc.cardmanagement.card.infrastructure.dto.CardBalanceRequestDTO;
import com.bankinc.cardmanagement.card.infrastructure.dto.CardBalanceResponseDTO;
import com.bankinc.cardmanagement.card.infrastructure.dto.CardNumberResponseDTO;

public interface CardService {
    CardNumberResponseDTO generateCardNumber(String productId);
    void activateCard(CardActivationRequestDTO request);
    void blockCard(Long cardId);
    void addBalance(CardBalanceRequestDTO request);
    CardBalanceResponseDTO getBalance(Long cardId);
}
