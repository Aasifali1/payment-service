package com.knoldus.paymentservice.repository;

import com.knoldus.paymentservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
     Payment findByStripePaymentId(String paymentId);
     Payment findByOrderId(Long id);
     Payment findTopByOrderByIdDesc();
     Payment findByStripePaymentId(long stripeId);
    
}
