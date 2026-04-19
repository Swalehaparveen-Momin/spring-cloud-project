package com.example.paymentservice.controller;

import com.example.paymentservice.dto.PaymentDTO;
import com.example.paymentservice.dto.PaymentRequest;
import com.example.paymentservice.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentDTO> getPaymentByOrderId(
            @PathVariable Long orderId
    ){
        try {
            return new ResponseEntity<>(paymentService.getPaymentByOrderId(orderId), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> processPayment(
            @RequestBody PaymentRequest paymentRequest
            ){
        return new ResponseEntity<>(paymentService.processPayment(paymentRequest),HttpStatus.OK);
    }
}
