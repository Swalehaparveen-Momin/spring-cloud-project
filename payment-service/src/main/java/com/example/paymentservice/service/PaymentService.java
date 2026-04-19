package com.example.paymentservice.service;

import com.example.paymentservice.dao.PaymentRepository;
import com.example.paymentservice.dto.PaymentDTO;
import com.example.paymentservice.dto.PaymentRequest;
import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.entity.PaymentStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }

    public PaymentDTO getPaymentByOrderId(Long id) {
        Payment p = paymentRepository.findByOrderId(id).orElseThrow(()-> new RuntimeException("Payment not found associated with orderId : "+id));
        return toPaymentDTO(p);
    }

    private PaymentDTO toPaymentDTO(Payment p) {
        return new PaymentDTO(
                p.getId(),
                p.getOrderId(),
                p.getAmount(),
                p.getPaymentStatus().name(),
                p.getTimestamp()
        );
    }


    public PaymentDTO processPayment(PaymentRequest paymentRequest) {
        Payment payment = new Payment();
        payment.setOrderId(paymentRequest.orderId());
        payment.setAmount(paymentRequest.amount());
        payment.setTimestamp(LocalDateTime.now());
       payment.setPaymentStatus(PaymentStatus.SUCCESS);
        Payment paymentSaved  = paymentRepository.save(payment);
        return toPaymentDTO(paymentSaved);
    }

}
