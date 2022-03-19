package com.knoldus.paymentservice.dto;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class CreatePayment {
    //    Object[] items;
    @SerializedName("purchase")
    private JsonObject amount;
    
    public String getEmail(){
        return amount.get("email").getAsString();
    }
    
    public Long getAmount() {
        return amount.get("amount").getAsLong();
    }
    
    public Long getOrderId() {
        return amount.get("orderId").getAsLong();
    }
    
    public void setAmount(JsonObject amount) {
        this.amount = amount;
    }
}
