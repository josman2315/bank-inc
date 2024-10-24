package com.bankinc.cardmanagement;

import com.bankinc.cardmanagement.card.domain.model.Card;
import com.bankinc.cardmanagement.card.infrastructure.exception.CardBlockedException;
import com.bankinc.cardmanagement.transaction.domain.service.TransactionDomainService;
import com.bankinc.cardmanagement.transaction.infrastructure.exception.InsufficientBalanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionDomainServiceTest {

    private TransactionDomainService transactionDomainService;
    private Card card;

    @BeforeEach
    void setUp() {
        transactionDomainService = new TransactionDomainService();
        card = mock(Card.class);
    }

    @Test
    void validateForPurchase_CardIsNotActive_ShouldThrowCardBlockedException() {
        when(card.isActive()).thenReturn(false);
        when(card.isBlocked()).thenReturn(false);

        CardBlockedException exception = assertThrows(CardBlockedException.class, () -> {
            transactionDomainService.validateForPurchase(card, 100.0);
        });

        assertEquals("Card is not active or is blocked", exception.getMessage());
    }

    @Test
    void validateForPurchase_CardIsBlocked_ShouldThrowCardBlockedException() {
        when(card.isActive()).thenReturn(true);
        when(card.isBlocked()).thenReturn(true);

        CardBlockedException exception = assertThrows(CardBlockedException.class, () -> {
            transactionDomainService.validateForPurchase(card, 100.0);
        });

        assertEquals("Card is not active or is blocked", exception.getMessage());
    }

    @Test
    void validateForPurchase_InsufficientBalance_ShouldThrowInsufficientBalanceException() {
        when(card.isActive()).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        when(card.getBalance()).thenReturn(50.0);

        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> {
            transactionDomainService.validateForPurchase(card, 100.0);
        });

        assertEquals("Insufficient balance", exception.getMessage());
    }

    @Test
    void validateForPurchase_ValidCard_ShouldNotThrowException() {
        when(card.isActive()).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        when(card.getBalance()).thenReturn(100.0);

        assertDoesNotThrow(() -> {
            transactionDomainService.validateForPurchase(card, 100.0);
        });
    }

    @Test
    void deductBalance_ShouldDeductCorrectAmount() {
        when(card.getBalance()).thenReturn(200.0);
        transactionDomainService.deductBalance(card, 100.0);

        verify(card).setBalance(100.0);
    }
}
