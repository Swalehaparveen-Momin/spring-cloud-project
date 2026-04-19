package com.example.orderservice.dto;

import com.example.orderservice.entity.OrderItem;

import java.util.List;

public record OrderRequest (List<OrderItemRequest> items){}
