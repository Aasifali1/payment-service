package com.knoldus.paymentservice.controller;

import com.knoldus.paymentservice.model.Order;
import com.knoldus.paymentservice.model.Payment;
import com.knoldus.paymentservice.model.stripe.StripeClient;
import com.knoldus.paymentservice.service.PaymentService;
import com.knoldus.paymentservice.service.ProducerService;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

//@CrossOrigin(origins = "http://localhost:36843")
@RestController
//@RequestMapping("/payments")
public class PaymentController {

    private StripeClient stripeClient;

    @Autowired
    PaymentController(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }
    
    @Autowired
    PaymentService paymentService;
    
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ProducerService producerService;
    
    @Value("${stripe.secret.key}")
    String stripeKey;
    
    @GetMapping("/api")
    public String index(){
        return "hello"+stripeKey;
    }
    
    @PostMapping("/create/{orderId}")
    public Payment createPayment( @RequestBody Payment payment, @PathVariable("orderId") Long orderId) {
        Order order = restTemplate.getForObject("http://localhost:8087/orders/"+orderId, Order.class);
        payment.setOrderId(orderId);
        payment.setPaymentDate(String.valueOf(java.time.LocalDate.now()));
        producerService.sendStatus(payment.getPaymentStatus()+" "+orderId);
        return paymentService.createPayment(payment);
    }
    @PostMapping("/update")
    public Payment updatePayment(@RequestBody Payment payment) {
        return paymentService.updatePayment(payment);
    }
    
    @PostMapping("/charge")
    public Charge chargeCard(HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        System.out.println("runnin..........");
//        Double amount = Double.parseDouble(request.getHeader("amount"));
        return this.stripeClient.chargeCreditCard(token, 100);
    }
}
