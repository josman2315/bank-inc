package com.bankinc.cardmanagement;

import com.bankinc.cardmanagement.card.domain.model.Card;
import com.bankinc.cardmanagement.card.domain.repository.CardRepository;
import com.bankinc.cardmanagement.card.domain.service.CardDomainService;
import com.bankinc.cardmanagement.card.infrastructure.exception.CardNotFoundException;
import com.bankinc.cardmanagement.transaction.application.service.TransactionServiceImpl;
import com.bankinc.cardmanagement.transaction.domain.model.Transaction;
import com.bankinc.cardmanagement.transaction.domain.repository.TransactionRepository;
import com.bankinc.cardmanagement.transaction.domain.service.TransactionDomainService;
import com.bankinc.cardmanagement.transaction.domain.service.TransactionFactory;
import com.bankinc.cardmanagement.transaction.infrastructure.dto.AnulationRequestDTO;
import com.bankinc.cardmanagement.transaction.infrastructure.dto.PurchaseRequestDTO;
import com.bankinc.cardmanagement.transaction.infrastructure.dto.TransactionResponseDTO;
import com.bankinc.cardmanagement.transaction.infrastructure.exception.TransactionAlreadyAnnulledException;
import com.bankinc.cardmanagement.transaction.infrastructure.exception.TransactionAnnulationPeriodExceededException;
import com.bankinc.cardmanagement.transaction.infrastructure.exception.TransactionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionFactory transactionFactory;

    @Mock
    private CardRepository cardRepository;


    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionDomainService transactionDomainService;

    @Mock
    private CardDomainService cardDomainService;

    private Card card;

    private Transaction transaction;

    private PurchaseRequestDTO purchaseRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        purchaseRequest = new PurchaseRequestDTO();
        purchaseRequest.setCardId("1234500000000000");
        purchaseRequest.setPrice(50.00);

        transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setAmount(50.00);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAnnulled(false);

        card = new Card();
        card.setBalance(100.0);
        transaction.setCard(card);

    }

    @Test
    public void testMakePurchaseSuccess() {
        when(cardRepository.findByCardNumber(purchaseRequest.getCardId())).thenReturn(Optional.of(card));

        doNothing().when(transactionDomainService).validateForPurchase(card, purchaseRequest.getPrice());
        doNothing().when(transactionDomainService).deductBalance(card, purchaseRequest.getPrice());

        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setAmount(purchaseRequest.getPrice());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAnnulled(false);

        when(transactionFactory.createTransaction(card, purchaseRequest.getPrice())).thenReturn(transaction);

        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(cardRepository.save(card)).thenReturn(card);

        TransactionResponseDTO response = transactionService.makePurchase(purchaseRequest);

        assertEquals(transaction.getTransactionId(), response.getTransactionId());
        assertEquals(transaction.getAmount(), response.getAmount());
        assertEquals(transaction.getTransactionDate(), response.getTransactionDate());
        assertEquals(transaction.isAnnulled(), response.isAnnulled());

        verify(cardRepository, times(1)).findByCardNumber(purchaseRequest.getCardId());
        verify(transactionDomainService, times(1)).validateForPurchase(card, purchaseRequest.getPrice());
        verify(transactionDomainService, times(1)).deductBalance(card, purchaseRequest.getPrice());
        verify(transactionRepository, times(1)).save(transaction);
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    public void testMakePurchaseCardNotFound() {
        when(cardRepository.findByCardNumber(purchaseRequest.getCardId())).thenReturn(Optional.empty());

        CardNotFoundException exception = assertThrows(CardNotFoundException.class, () -> transactionService.makePurchase(purchaseRequest));

        assertEquals("Card not found", exception.getMessage());

        verify(transactionDomainService, never()).validateForPurchase(any(Card.class), anyDouble());
        verify(transactionDomainService, never()).deductBalance(any(Card.class), anyDouble());
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    public void testGetTransactionSuccess() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        TransactionResponseDTO response = transactionService.getTransaction("1");

        assertNotNull(response);
        assertEquals(transaction.getTransactionId(), response.getTransactionId());
        assertEquals(transaction.getAmount(), response.getAmount());
        assertEquals(transaction.getTransactionDate(), response.getTransactionDate());
        assertEquals(transaction.isAnnulled(), response.isAnnulled());

        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTransactionNotFound() {
        String transactionId = "1";
        when(transactionRepository.findById(Long.valueOf(transactionId))).thenReturn(Optional.empty());

        TransactionNotFoundException exception = assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransaction(transactionId));

        assertEquals("Transaction not found", exception.getMessage());

        verify(transactionRepository, times(1)).findById(Long.valueOf(transactionId));
    }


    @Test
    public void testAnnulTransactionSuccess() {
        AnulationRequestDTO request = new AnulationRequestDTO();
        request.setTransactionId("1");

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        transactionService.annulTransaction(request);

        assertTrue(transaction.isAnnulled());

        verify(transactionRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).save(transaction);
        verify(cardDomainService, times(1)).addBalance(transaction.getCard(), transaction.getAmount());
        verify(cardRepository, times(1)).save(transaction.getCard());
    }

    @Test
    public void testAnnulTransactionNotFound() {
        AnulationRequestDTO request = new AnulationRequestDTO();
        request.setTransactionId("1");

        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        TransactionNotFoundException exception = assertThrows(TransactionNotFoundException.class, () -> transactionService.annulTransaction(request));
        assertEquals("Transaction not found", exception.getMessage());

        verify(transactionRepository, times(1)).findById(1L);
        verify(transactionRepository, never()).save(any());
    }

    @Test
    public void testAnnulTransactionAlreadyAnnulled() {
        AnulationRequestDTO request = new AnulationRequestDTO();
        request.setTransactionId("1");

        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setAnnulled(true); // Transacción ya anulada

        when(transactionRepository.findById(Long.valueOf(request.getTransactionId()))).thenReturn(Optional.of(transaction));

        TransactionAlreadyAnnulledException exception = assertThrows(TransactionAlreadyAnnulledException.class, () -> transactionService.annulTransaction(request));

        assertEquals("Transaction already annulled", exception.getMessage());

        verify(transactionRepository, times(1)).findById(Long.valueOf(request.getTransactionId()));
    }

    @Test
    public void testAnnulTransactionPeriodExceeded() {
        AnulationRequestDTO request = new AnulationRequestDTO();
        request.setTransactionId("1");

        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setAnnulled(false);
        transaction.setTransactionDate(LocalDateTime.now().minusDays(1));

        when(transactionRepository.findById(Long.valueOf(request.getTransactionId()))).thenReturn(Optional.of(transaction));

        TransactionAnnulationPeriodExceededException exception = assertThrows(TransactionAnnulationPeriodExceededException.class, () -> transactionService.annulTransaction(request));

        assertEquals("Transaction can only be annulled within 24 hours", exception.getMessage());

        verify(transactionRepository, times(1)).findById(Long.valueOf(request.getTransactionId()));
    }


}
