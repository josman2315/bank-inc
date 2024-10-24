package com.bankinc.cardmanagement;

import com.bankinc.cardmanagement.card.application.service.CardServiceImpl;
import com.bankinc.cardmanagement.card.domain.model.Card;
import com.bankinc.cardmanagement.card.domain.repository.CardRepository;
import com.bankinc.cardmanagement.card.domain.service.CardDomainService;
import com.bankinc.cardmanagement.card.domain.service.CardFactoryService;
import com.bankinc.cardmanagement.card.infrastructure.dto.CardActivationRequestDTO;
import com.bankinc.cardmanagement.card.infrastructure.dto.CardBalanceRequestDTO;
import com.bankinc.cardmanagement.card.infrastructure.dto.CardBalanceResponseDTO;
import com.bankinc.cardmanagement.card.infrastructure.dto.CardNumberResponseDTO;
import com.bankinc.cardmanagement.card.infrastructure.exception.CardNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;


public class CardServiceImplTest {

    @InjectMocks
    private CardServiceImpl cardService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardFactoryService cardFactoryService;

    @Mock
    private CardDomainService cardDomainService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Card card = new Card();
        card.setCardNumber("1234567890");
        card.setBalance(0.0);
    }

    @Test
    void testGenerateCardNumber() {
        String productId = "123450";
        String expectedCardNumber = "1234500000000000";
        CardNumberResponseDTO expectedResponse = new CardNumberResponseDTO(expectedCardNumber);

        when(cardFactoryService.generateCardNumber(productId)).thenReturn(expectedResponse);
        when(cardFactoryService.createNewCard(expectedCardNumber)).thenReturn(new Card());

        CardNumberResponseDTO actualResponse = cardService.generateCardNumber(productId);

        assertNotNull(actualResponse);
        assertEquals(expectedCardNumber, actualResponse.getCardNumber());
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    public void testActivateCardSuccess() {
        CardActivationRequestDTO request = new CardActivationRequestDTO();
        request.setCardId("1234500000000000");

        Card card = new Card();
        when(cardRepository.findByCardNumber(request.getCardId())).thenReturn(Optional.of(card));

        cardService.activateCard(request);

        verify(cardRepository, times(1)).findByCardNumber(request.getCardId());
        verify(cardDomainService, times(1)).validateCardForActivation(card);
        verify(cardDomainService, times(1)).activateCard(card);
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    public void testActivateCardThrowsCardNotFoundException() {
        CardActivationRequestDTO request = new CardActivationRequestDTO();
        request.setCardId("1234500000000000");

        when(cardRepository.findByCardNumber(request.getCardId())).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardService.activateCard(request));

        verify(cardRepository, times(1)).findByCardNumber(request.getCardId());
        verify(cardDomainService, never()).validateCardForActivation(any());
        verify(cardDomainService, never()).activateCard(any());
        verify(cardRepository, never()).save(any());
    }

    @Test
    public void testBlockCardSuccess() {
        Long cardId = 1234500000000000L;
        Card card = new Card();

        when(cardRepository.findByCardNumber(String.valueOf(cardId))).thenReturn(Optional.of(card));

        cardService.blockCard(cardId);

        verify(cardRepository, times(1)).findByCardNumber(String.valueOf(cardId));
        verify(cardDomainService, times(1)).validateCardForBlocking(card);
        verify(cardDomainService, times(1)).blockCard(card);
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    public void testBlockCardThrowsCardNotFoundException() {
        Long cardId = 1234500000000000L;

        when(cardRepository.findByCardNumber(String.valueOf(cardId))).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardService.blockCard(cardId));

        verify(cardRepository, times(1)).findByCardNumber(String.valueOf(cardId));
        verify(cardDomainService, never()).validateCardForBlocking(any());
        verify(cardDomainService, never()).blockCard(any());
        verify(cardRepository, never()).save(any());
    }

    @Test
    public void testAddBalanceSuccess() {
        long cardId = 1234500000000000L;
        double balanceToAdd = 100.0;
        Card card = new Card();
        CardBalanceRequestDTO request = new CardBalanceRequestDTO();
        request.setCardId(Long.toString(cardId));
        request.setBalance(balanceToAdd);

        when(cardRepository.findByCardNumber(request.getCardId())).thenReturn(Optional.of(card));

        cardService.addBalance(request);

        verify(cardRepository, times(1)).findByCardNumber(request.getCardId());
        verify(cardDomainService, times(1)).validateCardForAddingBalance(card, balanceToAdd);
        verify(cardDomainService, times(1)).addBalance(card, balanceToAdd);
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    public void testAddBalanceThrowsCardNotFoundException() {
        long cardId = 1234500000000000L;
        CardBalanceRequestDTO request = new CardBalanceRequestDTO();
        request.setCardId(Long.toString(cardId));
        request.setBalance(100.0);

        when(cardRepository.findByCardNumber(request.getCardId())).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardService.addBalance(request));

        verify(cardRepository, times(1)).findByCardNumber(request.getCardId());
        verify(cardDomainService, never()).validateCardForAddingBalance(any(), anyDouble());
        verify(cardDomainService, never()).addBalance(any(), anyDouble());
        verify(cardRepository, never()).save(any());
    }

    @Test
    public void testGetBalanceSuccess() {
        Long cardId = 1234500000000000L;
        double balance = 250.0;
        Card card = new Card();
        card.setBalance(balance);

        when(cardRepository.findByCardNumber(String.valueOf(cardId))).thenReturn(Optional.of(card));

        CardBalanceResponseDTO response = cardService.getBalance(cardId);

        assertEquals(balance, response.getBalance());
        verify(cardRepository, times(1)).findByCardNumber(String.valueOf(cardId));
    }

    @Test
    public void testGetBalanceThrowsCardNotFoundException() {
        Long cardId = 1234500000000000L;

        when(cardRepository.findByCardNumber(String.valueOf(cardId))).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardService.getBalance(cardId));

        verify(cardRepository, times(1)).findByCardNumber(String.valueOf(cardId));
    }
}
