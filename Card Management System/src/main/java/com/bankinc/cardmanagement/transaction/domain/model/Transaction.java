package com.bankinc.cardmanagement.transaction.domain.model;

import com.bankinc.cardmanagement.card.domain.model.Card;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @Column(nullable = false)
    private boolean isAnnulled;

    public Transaction(Card card, double amount, LocalDateTime transactionDate) {
        this.card = card;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.isAnnulled = false;
    }

}
