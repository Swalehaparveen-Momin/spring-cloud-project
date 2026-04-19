package com.example.paymentservice.exception;

public class PaymentNotFoundException extends RuntimeException{

    public PaymentNotFoundException(Long id){
        super("Payment Not Found with Payment ID : "+id);
    }
}
