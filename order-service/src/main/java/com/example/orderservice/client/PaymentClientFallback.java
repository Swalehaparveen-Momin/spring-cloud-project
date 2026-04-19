package com.example.orderservice.client;

import com.example.orderservice.dto.PaymentDTO;
import com.example.orderservice.dto.PaymentRequest;
import com.example.orderservice.dto.ProductDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class PaymentClientFallback implements PaymentClient {

    @Override
    public PaymentDTO processPayment(PaymentRequest request) {
        return new PaymentDTO(0L,0L, BigDecimal.ZERO,"PENDING", LocalDateTime.now());
    }
}
