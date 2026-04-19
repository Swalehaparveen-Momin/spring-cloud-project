package com.example.orderservice.client;

import com.example.orderservice.dto.PaymentDTO;
import com.example.orderservice.dto.PaymentRequest;
import com.example.orderservice.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "payment-service", fallback = PaymentClientFallback.class)
public interface PaymentClient {

    @PostMapping("/api/payments")
    public PaymentDTO processPayment(
            @RequestBody PaymentRequest request
    );


}
