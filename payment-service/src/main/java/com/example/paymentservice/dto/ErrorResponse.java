package com.example.paymentservice.dto;

import java.time.LocalDateTime;

public record ErrorResponse(int status, String message, LocalDateTime timeStamp) {}
