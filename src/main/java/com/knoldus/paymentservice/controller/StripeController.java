package com.knoldus.paymentservice.controller;

import com.google.gson.JsonSyntaxException;
import com.knoldus.paymentservice.dto.CreatePayment;
import com.knoldus.paymentservice.dto.CreatePaymentResponse;
import com.knoldus.paymentservice.model.Payment;
import com.knoldus.paymentservice.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.ApiResource;
import com.stripe.param.PaymentIntentCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class StripeController {
    @Autowired
    PaymentRepository paymentRepository;
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;
    Logger logger = LoggerFactory.getLogger(StripeController.class);
    
    @PostMapping("/create-payment-intent")
    public CreatePaymentResponse createPaymentIntent(@RequestBody CreatePayment createPayment) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(createPayment.getAmount()*100)
                .putMetadata("email", createPayment.getEmail())
                .setReceiptEmail(createPayment.getEmail())
                .setCurrency("inr")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods
                                .builder()
                                .setEnabled(true)
                                .build())
                .build();
        logger.info("intent method is running...... "+createPayment.getEmail());
        PaymentIntent paymentIntent = PaymentIntent.create(params);
        Payment payment = new Payment();
        payment.setPaymentMethod(paymentIntent.getStatus());
        payment.setStripePaymentId(paymentIntent.getId());
        payment.setAmount(paymentIntent.getAmount() / 100);
        payment.setEmail(paymentIntent.getReceiptEmail());
        payment.setOrderId(createPayment.getOrderId());
        paymentRepository.save(payment);
        CreatePaymentResponse paymentResponse = new CreatePaymentResponse(paymentIntent.getClientSecret());
        return paymentResponse;
    }
    
    @PostMapping("/webhook")
    public String getWebhookData(@RequestBody String payload){
        Event event = null;
        try {
            event = ApiResource.GSON.fromJson(payload, Event.class);
        } catch (JsonSyntaxException e) {
            // Invalid payload
            logger.info("⚠️  Webhook error while parsing basic request.");
            return "";
        }
        
        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        }
        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                Payment payment = paymentRepository.findByStripePaymentId(paymentIntent.getId());
                payment.setTransactionId(paymentIntent.getCharges().getData().get(0).getBalanceTransaction());
                payment.setPaymentStatus(paymentIntent.getStatus());
                payment.setReceiptUrl(paymentIntent.getCharges().getData().get(0).getReceiptUrl());
                payment.setPaymentMethod(paymentIntent.getPaymentMethod());
                paymentRepository.save(payment);
                break;
            case "payment_intent.payment_failed":
                PaymentIntent paymentFailed = (PaymentIntent) stripeObject;
                break;
            case "payment_method.attached":
                PaymentMethod paymentMethod = (PaymentMethod) stripeObject;
                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());
                break;
        }
        return "";
    }
    @GetMapping("/payment/{id}")
    public ResponseEntity<Payment> getPaymentByOrderId(@PathVariable("id") long id){
        Payment payment = paymentRepository.findByOrderId(id);
        return ResponseEntity.ok(payment);
    }
}
