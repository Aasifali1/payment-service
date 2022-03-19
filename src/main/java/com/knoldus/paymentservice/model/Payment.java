package com.knoldus.paymentservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Setter
@Getter
@Entity
public class Payment {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long orderId;
    private String paymentDate;
    private String paymentMethod;
    private String transactionId;
    private Long customerId;
    private String stripePaymentId;
    private String paymentStatus;
    private double amount;
    private String email;
    private String receiptUrl;
}
