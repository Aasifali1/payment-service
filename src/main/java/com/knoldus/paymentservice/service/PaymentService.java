package com.knoldus.paymentservice.service;

import com.knoldus.paymentservice.model.Payment;
import com.knoldus.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }
    public Payment updatePayment(Payment payment) {
        return paymentRepository.save(payment);
    }
}
