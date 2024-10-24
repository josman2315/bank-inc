package com.bankinc.cardmanagement.transaction.application.service;

import com.bankinc.cardmanagement.card.domain.service.CardDomainService;
import com.bankinc.cardmanagement.card.infrastructure.exception.CardNotFoundException;
import com.bankinc.cardmanagement.transaction.infrastructure.dto.AnulationRequestDTO;
import com.bankinc.cardmanagement.transaction.infrastructure.dto.PurchaseRequestDTO;
import com.bankinc.cardmanagement.transaction.infrastructure.dto.TransactionResponseDTO;
import com.bankinc.cardmanagement.card.domain.model.Card;
import com.bankinc.cardmanagement.transaction.domain.model.Transaction;
import com.bankinc.cardmanagement.card.domain.repository.CardRepository;
import com.bankinc.cardmanagement.transaction.domain.repository.TransactionRepository;
import com.bankinc.cardmanagement.transaction.domain.service.TransactionDomainService;
import com.bankinc.cardmanagement.transaction.domain.service.TransactionFactory;
import com.bankinc.cardmanagement.transaction.infrastructure.exception.TransactionAlreadyAnnulledException;
import com.bankinc.cardmanagement.transaction.infrastructure.exception.TransactionAnnulationPeriodExceededException;
import com.bankinc.cardmanagement.transaction.infrastructure.exception.TransactionNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;
    private final TransactionDomainService transactionDomainService;
    private final CardDomainService cardDomainService;
    private final TransactionFactory transactionFactory;

    /**
     * Processes a purchase transaction.
     *
     * @param request the purchase request data transfer object
     * @return the transaction response data transfer object
     */
    @Override
    @Transactional
    public TransactionResponseDTO makePurchase(PurchaseRequestDTO request) {
        Card card = cardRepository.findByCardNumber(request.getCardId())
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        transactionDomainService.validateForPurchase(card, request.getPrice());

        transactionDomainService.deductBalance(card, request.getPrice());
        cardRepository.save(card);

        Transaction transaction = transactionFactory.createTransaction(card, request.getPrice());
        transactionRepository.save(transaction);

        log.info("Transaction {} made successfully for card {}", transaction.getTransactionId(), request.getCardId());
        return new TransactionResponseDTO(transaction.getTransactionId(), transaction.getAmount(), transaction.getTransactionDate(), transaction.isAnnulled());
    }

    /**
     * Retrieves a specific transaction by its ID.
     *
     * @param transactionId the ID of the transaction to retrieve
     * @return the transaction response data transfer object
     */
    @Override
    @Transactional(readOnly = true)
    public TransactionResponseDTO getTransaction(String transactionId) {
        Transaction transaction = transactionRepository.findById(Long.valueOf(transactionId))
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));

        return new TransactionResponseDTO(transaction.getTransactionId(), transaction.getAmount(), transaction.getTransactionDate(), transaction.isAnnulled());
    }

    /**
     * Annuls a transaction by its ID.
     *
     * @param request the anulation request data transfer object
     */
    @Override
    @Transactional
    public void annulTransaction(AnulationRequestDTO request) {
        Transaction transaction = transactionRepository.findById(Long.valueOf(request.getTransactionId()))
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));

        if (transaction.isAnnulled()) {
            throw new TransactionAlreadyAnnulledException("Transaction already annulled");
        }

        if (transaction.getTransactionDate().isBefore(LocalDateTime.now().minusHours(24))) {
            throw new TransactionAnnulationPeriodExceededException("Transaction can only be annulled within 24 hours");
        }

        transaction.setAnnulled(true);
        transactionRepository.save(transaction);

        Card card = transaction.getCard();
        cardDomainService.addBalance(card, transaction.getAmount());
        cardRepository.save(card);

        log.info("Transaction {} annulled successfully", transaction.getTransactionId());
    }

}
