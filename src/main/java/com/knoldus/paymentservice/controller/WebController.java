package com.knoldus.paymentservice.controller;

import com.knoldus.paymentservice.model.Checkout;
import com.knoldus.paymentservice.model.CustomerData;
import com.knoldus.paymentservice.model.Order;
import com.knoldus.paymentservice.model.Payment;
import com.knoldus.paymentservice.repository.PaymentRepository;
import com.knoldus.paymentservice.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

//@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class WebController {
    
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    ProducerService producerService;
    @Value("${stripe.public.key}")
    String stripePublicKey;
    RestTemplate restTemplate = new RestTemplate();
    
    public CustomerData getCustomer(long customerId){
        CustomerData customerData = restTemplate.getForObject(
                "http://localhost:8085/api/v1/customers/"+customerId, CustomerData.class);
        return customerData;
    }
    public Order getOrder(long orderId){
        Order order = restTemplate.getForObject("http://localhost:8087/orders/"+orderId, Order.class);
        return order;
    }
    @GetMapping("/checkout/{orderId}")
    public String index(@PathVariable("orderId") Long orderId, Model model){
        Order order = this.getOrder(orderId);
        CustomerData customer = this.getCustomer(order.getCustomerId());
        Checkout checkout = new Checkout();
        checkout.setAmount(order.getAmount());
        checkout.setEmail(customer.getEmail());
        checkout.setOrderId(order.getId());
        model.addAttribute("checkout", checkout);
        return "index";
    }
    @PostMapping("/")
    public String checkout(@ModelAttribute @Valid Checkout checkout, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "index";
        }
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("amount", checkout.getAmount());
        model.addAttribute("email", checkout.getEmail());
        model.addAttribute("paymentId", checkout.getPaymentId());
        model.addAttribute("orderId",checkout.getOrderId());
        return "checkout";
    }
    @ResponseBody
    @GetMapping("/success/{id}")
    public ModelAndView getData(@RequestParam(required = false, defaultValue = "payment_intent",
            value="payment_intent") String payment_intent, @RequestParam String redirect_status) {
        // your code goes here
        System.out.println("-----------------"+redirect_status);
        Payment payment = paymentRepository.findByStripePaymentId(payment_intent);
        payment.setPaymentStatus(redirect_status);
        payment.setPaymentDate(java.time.LocalDate.now().toString());
        paymentRepository.save(payment);
        Order order = this.getOrder(payment.getOrderId());
        producerService.sendStatus(" 1"+order.getId());
        return new ModelAndView("redirect:" + "http://localhost:4200/order-receipt/"
                +order.getId());
    }
}
