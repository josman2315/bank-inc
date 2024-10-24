package com.bankinc.cardmanagement;

import com.bankinc.cardmanagement.card.domain.model.Card;
import com.bankinc.cardmanagement.card.domain.service.CardDomainService;
import com.bankinc.cardmanagement.card.infrastructure.exception.CardAlreadyActivatedException;
import com.bankinc.cardmanagement.card.infrastructure.exception.CardBlockedException;
import com.bankinc.cardmanagement.transaction.infrastructure.exception.InsufficientBalanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CardDomainServiceTest {

    private CardDomainService cardDomainService;
    private Card card;

    @BeforeEach
    void setUp() {
        cardDomainService = new CardDomainService();
        card = new Card();
    }

    @Test
    void validateCardForActivation_WhenCardIsInactiveAndNotBlocked_ShouldNotThrowException() {
        card.setActive(false);
        card.setBlocked(false);

        assertDoesNotThrow(() -> {
            cardDomainService.validateCardForActivation(card);
        });
    }

    @Test
    void validateCardForBlocking_WhenCardIsActiveAndNotBlocked_ShouldNotThrowException() {
        card.setActive(true); // La tarjeta está activa
        card.setBlocked(false); // La tarjeta no está bloqueada
        assertDoesNotThrow(() -> {
            cardDomainService.validateCardForBlocking(card);
        });
    }

    @Test
    void validateCardForActivation_WhenCardIsActive_ShouldThrowCardAlreadyActivatedException() {
        card.setActive(true);
        Exception exception = assertThrows(CardAlreadyActivatedException.class, () -> cardDomainService.validateCardForActivation(card));
        assertEquals("Card is already activated", exception.getMessage());
    }

    @Test
    void validateCardForAddingBalance_WhenCardIsActiveAndBalanceIsValid_ShouldNotThrowException() {
        card.setBlocked(false); // La tarjeta no está bloqueada
        card.setBalance(50); // El balance actual es 50
        double balanceToAdd = 20; // Intentamos agregar 20
        assertDoesNotThrow(() -> {
            cardDomainService.validateCardForAddingBalance(card, balanceToAdd);
        });
    }

    @Test
    void validateCardForActivation_WhenCardIsBlocked_ShouldThrowCardBlockedException() {
        card.setBlocked(true);
        Exception exception = assertThrows(CardBlockedException.class, () -> cardDomainService.validateCardForActivation(card));
        assertEquals("Blocked card cannot be activated", exception.getMessage());
    }

    @Test
    void activateCard_ShouldSetActiveAndExpirationDate() {
        cardDomainService.activateCard(card);
        assertTrue(card.isActive());
        assertEquals(LocalDate.now().plusYears(3), card.getExpirationDate());
    }

    @Test
    void validateCardForBlocking_WhenCardIsInactive_ShouldThrowCardBlockedException() {
        card.setActive(false);
        Exception exception = assertThrows(CardBlockedException.class, () -> cardDomainService.validateCardForBlocking(card));
        assertEquals("Inactive card cannot be blocked", exception.getMessage());
    }

    @Test
    void validateCardForBlocking_WhenCardIsAlreadyBlocked_ShouldThrowCardBlockedException() {
        card.setActive(true);
        card.setBlocked(true);
        Exception exception = assertThrows(CardBlockedException.class, () -> cardDomainService.validateCardForBlocking(card));
        assertEquals("Card is already blocked", exception.getMessage());
    }

    @Test
    void blockCard_ShouldSetInactiveAndBlocked() {
        card.setActive(true);
        cardDomainService.blockCard(card);
        assertFalse(card.isActive());
        assertTrue(card.isBlocked());
    }

    @Test
    void validateCardForAddingBalance_WhenCardIsBlocked_ShouldThrowCardBlockedException() {
        card.setBlocked(true);
        Exception exception = assertThrows(CardBlockedException.class, () -> cardDomainService.validateCardForAddingBalance(card, 100));
        assertEquals("Blocked card cannot have balance added", exception.getMessage());
    }

    @Test
    void validateCardForAddingBalance_WhenBalanceWouldBeNegative_ShouldThrowInsufficientBalanceException() {
        card.setBalance(50);
        Exception exception = assertThrows(InsufficientBalanceException.class, () -> cardDomainService.validateCardForAddingBalance(card, -100));
        assertEquals("Balance cannot be negative", exception.getMessage());
    }

    @Test
    void addBalance_ShouldIncreaseCardBalance() {
        card.setBalance(100);
        cardDomainService.addBalance(card, 50);
        assertEquals(150, card.getBalance());
    }
}
