package com.bankinc.cardmanagement.transaction.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnulationRequestDTO {

    @NotBlank(message = "El ID de la tarjeta no puede estar vacío")
    @Size(min = 16, max = 16, message = "El ID de la tarjeta debe tener 16 dígitos")
    private String cardId;

    @NotBlank(message = "El ID de la transacción no puede estar vacío")
    private String transactionId;
}
