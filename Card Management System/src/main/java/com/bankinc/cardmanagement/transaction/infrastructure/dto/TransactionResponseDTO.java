package com.bankinc.cardmanagement.transaction.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionResponseDTO {
    private Long transactionId;
    private double amount;
    private LocalDateTime transactionDate;
    private boolean isAnnulled;
}
