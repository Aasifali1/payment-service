package com.knoldus.paymentservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
public class Order {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Long customerId;
    private Long productId;
    private int quantity;
    private String orderStatus;
    private String date;
    private double amount;
    private String orderTrack;
    
    
}
