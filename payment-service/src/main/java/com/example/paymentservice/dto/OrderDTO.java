package com.example.paymentservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDTO(Long id, BigDecimal totalPrice, String status, LocalDateTime createdAt) {
}
