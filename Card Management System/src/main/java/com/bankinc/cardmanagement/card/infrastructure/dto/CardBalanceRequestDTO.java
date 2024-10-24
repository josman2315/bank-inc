package com.bankinc.cardmanagement.card.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CardBalanceRequestDTO {

    @NotBlank(message = "El ID de la tarjeta no puede estar vacío")
    @Size(min = 16, max = 16, message = "El ID de la tarjeta debe tener 16 dígitos")
    private String cardId;

    @NotNull(message = "El saldo debe ser proporcionado")
    @Positive(message = "El saldo debe ser un número positivo")
    private double balance;
}
