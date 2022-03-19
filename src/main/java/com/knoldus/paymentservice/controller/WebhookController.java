//package com.knoldus.paymentservice.controller;
//
//import com.stripe.Stripe;
//import com.stripe.exception.SignatureVerificationException;
//import com.stripe.model.*;
//import com.stripe.net.Webhook;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class WebhookController {
//
//    private Logger logger = LoggerFactory.getLogger(WebhookController.class);
//
//    @Value("${stripe.webhook.secret}")
//    private String endpointSecret;
//
//    @PostMapping("/stripe/events")
//    public String handleStripeEvent(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader){
//        // If you are using an endpoint defined with the API or dashboard, look in your webhook settings
//        // at https://dashboard.stripe.com/webhooks
//        System.out.println("hello");
//        if (sigHeader == null){
//            return "";
//        }
//        Event event;
//                // Only verify the event if you have an endpoint secret defined.
//                // Otherwise use the basic event deserialized with GSON.
//                try {
//                    event = Webhook.constructEvent(
//                            payload, sigHeader, endpointSecret
//                    );
//                } catch (SignatureVerificationException e) {
//                    // Invalid signature
//                    System.out.println("⚠️  Webhook error while validating signature.");
////                    response.status(400);
//                    return "";
//                }
//            // Deserialize the nested object inside the event
//            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
//            StripeObject stripeObject = null;
//            if (dataObjectDeserializer.getObject().isPresent()) {
//                stripeObject = dataObjectDeserializer.getObject().get();
//            } else {
//                // Deserialization failed, probably due to an API version mismatch.
//                // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
//                // instructions on how to handle this case, or return an error here.
//            }
//            // Handle the event
//            switch (event.getType()) {
//                case "payment_intent.succeeded":
//                    PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
//                    System.out.println("Payment for " + paymentIntent.getAmount() + " succeeded.");
//                    // Then define and call a method to handle the successful payment intent.
//                    // handlePaymentIntentSucceeded(paymentIntent);
//                    break;
//                case "payment_method.attached":
//                    PaymentMethod paymentMethod = (PaymentMethod) stripeObject;
//                    // Then define and call a method to handle the successful attachment of a PaymentMethod.
//                    // handlePaymentMethodAttached(paymentMethod);
//                    break;
//                default:
//                    System.out.println("Unhandled event type: " + event.getType());
//                    break;
//            }
////            response.status(200);
//            return "";
//    }
//}
