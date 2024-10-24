package com.bankinc.cardmanagement.card.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @Column(nullable = false, unique = true, length = 16)
    private String cardNumber;

    @Column(length = 100)
    private String holderName;

    private LocalDate expirationDate;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private boolean isBlocked;

    @Column(nullable = false)
    private double balance;

    public Card(String cardNumber, String holderName, LocalDate expirationDate) {
        this.cardNumber = cardNumber;
        this.holderName = holderName;
        this.expirationDate = expirationDate;
        this.isActive = false;
        this.isBlocked = false;
        this.balance = 0.0;
    }

}
