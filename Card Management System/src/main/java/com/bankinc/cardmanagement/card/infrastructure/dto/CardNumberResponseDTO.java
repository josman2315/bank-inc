package com.bankinc.cardmanagement.card.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardNumberResponseDTO {

    @JsonProperty("cardId")
    private String cardNumber;

    @Override
    public String toString() {
        return cardNumber;
    }
}