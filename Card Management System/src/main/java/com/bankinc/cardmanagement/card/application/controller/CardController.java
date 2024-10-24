package com.bankinc.cardmanagement.card.application.controller;

import com.bankinc.cardmanagement.card.infrastructure.dto.CardActivationRequestDTO;
import com.bankinc.cardmanagement.card.infrastructure.dto.CardBalanceRequestDTO;
import com.bankinc.cardmanagement.card.infrastructure.dto.CardBalanceResponseDTO;
import com.bankinc.cardmanagement.card.infrastructure.dto.CardNumberResponseDTO;
import com.bankinc.cardmanagement.card.application.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    /**
     * Generates a card number for the specified product.
     *
     * @param productId the ID of the product for which to generate a card number
     * @return a response entity containing the generated card number
     */
    @GetMapping("/{productId}/number")
    public ResponseEntity<CardNumberResponseDTO> generateCardNumber(@PathVariable String productId) {
        CardNumberResponseDTO response = cardService.generateCardNumber(productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Activates a card based on the provided activation request.
     *
     * @param request the activation request containing necessary details
     * @return a response entity indicating the activation result
     */
    @PostMapping("/enroll")
    public ResponseEntity<Void> activateCard(@Valid @RequestBody CardActivationRequestDTO request) {
        cardService.activateCard(request);
        return ResponseEntity.ok().build();
    }

    /**
     * Blocks a card with the specified ID.
     *
     * @param cardId the ID of the card to be blocked
     * @return a response entity indicating the blocking result
     */
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> blockCard(@PathVariable Long cardId) {
        cardService.blockCard(cardId);
        return ResponseEntity.ok().build();
    }

    /**
     * Adds balance to a card based on the provided balance request.
     *
     * @param request the balance request containing the amount to add
     * @return a response entity indicating the result of the addition
     */
    @PostMapping("/balance")
    public ResponseEntity<Void> addBalance(@Valid @RequestBody CardBalanceRequestDTO request) {
        cardService.addBalance(request);
        return ResponseEntity.ok().build();
    }


    /**
     * Retrieves the balance of the card with the specified ID.
     *
     * @param cardId the ID of the card whose balance is to be retrieved
     * @return a response entity containing the card balance
     */
    @GetMapping("/balance/{cardId}")
    public ResponseEntity<CardBalanceResponseDTO> getBalance(@PathVariable Long cardId) {
        CardBalanceResponseDTO response = cardService.getBalance(cardId);
        return ResponseEntity.ok(response);
    }
}
