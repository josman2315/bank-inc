package com.bankinc.cardmanagement.card.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardBalanceResponseDTO {
    private double balance;
}