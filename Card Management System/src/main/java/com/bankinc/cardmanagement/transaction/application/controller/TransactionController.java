package com.bankinc.cardmanagement.transaction.application.controller;

import com.bankinc.cardmanagement.transaction.infrastructure.dto.AnulationRequestDTO;
import com.bankinc.cardmanagement.transaction.infrastructure.dto.PurchaseRequestDTO;
import com.bankinc.cardmanagement.transaction.infrastructure.dto.TransactionResponseDTO;
import com.bankinc.cardmanagement.transaction.application.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Handles the purchase transaction request.
     *
     * @param request the purchase request data transfer object
     * @return a response entity containing the transaction response and HTTP status
     */
    @PostMapping("/purchase")
    public ResponseEntity<TransactionResponseDTO> makePurchase(@Valid @RequestBody PurchaseRequestDTO request) {
        TransactionResponseDTO response = transactionService.makePurchase(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieves the details of a specific transaction.
     *
     * @param transactionId the ID of the transaction to retrieve
     * @return a response entity containing the transaction response
     */
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> getTransaction(@PathVariable String transactionId) {
        TransactionResponseDTO response = transactionService.getTransaction(transactionId);
        return ResponseEntity.ok(response);
    }

    /**
     * Handles the request to annul a transaction.
     *
     * @param request the anulation request data transfer object
     * @return a response entity with no content (204 No Content)
     */
    @PostMapping("/anulation")
    public ResponseEntity<Void> annulTransaction(@Valid @RequestBody AnulationRequestDTO request) {
        transactionService.annulTransaction(request);
        return ResponseEntity.ok().build();
    }
}
