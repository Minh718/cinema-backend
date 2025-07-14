package com.movie.paymentservice.entities;

import java.time.LocalDateTime;

import com.movie.paymentservice.enums.PaymentMethod;
import com.movie.paymentservice.enums.PaymentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookingId;
    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus; // PAID, FAILED, etc.

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; // MOMO, ZALOPAY

    private String transactionId;
    private String message;
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
