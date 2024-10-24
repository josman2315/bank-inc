package com.bankinc.cardmanagement.transaction.application.service;

import com.bankinc.cardmanagement.transaction.infrastructure.dto.AnulationRequestDTO;
import com.bankinc.cardmanagement.transaction.infrastructure.dto.PurchaseRequestDTO;
import com.bankinc.cardmanagement.transaction.infrastructure.dto.TransactionResponseDTO;

public interface TransactionService {
    TransactionResponseDTO makePurchase(PurchaseRequestDTO request);
    TransactionResponseDTO getTransaction(String transactionId);
    void annulTransaction(AnulationRequestDTO request);
}
