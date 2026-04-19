package com.example.orderservice.service;

import com.example.orderservice.client.PaymentClient;
import com.example.orderservice.client.ProductClient;
import com.example.orderservice.dao.OrderRepository;
import com.example.orderservice.dto.*;
import com.example.orderservice.entity.OrderItem;
import com.example.orderservice.entity.OrderStatus;
import com.example.orderservice.entity.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final PaymentClient paymentClient;

    public OrderService(OrderRepository orderRepository, ProductClient productClient, PaymentClient paymentClient ){
        this.orderRepository = orderRepository;
        this.productClient = productClient;
        this.paymentClient = paymentClient;
    }

    public List<Orders> getAllOrders(){
        return orderRepository.findAll();
    }

    public Orders getById(Long id){
        return orderRepository.findById(id).orElseThrow(()-> new RuntimeException("Not found"));
    }

    @Transactional
    public OrderResponse checkout(OrderRequest orderRequest) {
        List<OrderItem> items = new ArrayList<>();
        try {
            for (OrderItemRequest orderItemRequest : orderRequest.items()) {
                ProductDTO productDTO = productClient.getProduct(orderItemRequest.productId());
                productClient.reduceStock(orderItemRequest.productId(), orderItemRequest.quantity());

                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(productDTO.id());
                orderItem.setProductName(productDTO.name());
                orderItem.setQuantity(orderItemRequest.quantity());
                orderItem.setPrice(productDTO.price());

                items.add(orderItem);
            }
            BigDecimal totalPrice = items.stream()
                    .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Orders order = new Orders();
            order.setItems(items);
            order.setOrderStatus(OrderStatus.CREATED);
            order.setTotalPrice(totalPrice);
            order.setCreatedAt(LocalDateTime.now());
//        order = orderRepository.save(order);
            PaymentDTO paymentDTO = paymentClient.processPayment(new PaymentRequest(order.getId(), totalPrice));

//            String str = "hello";
//            char x = str.charAt(5);
//            System.out.println(x);
            // if exception occur we have to restore the stock

            if (paymentDTO != null || "PENDING".equals(paymentDTO.status())) {
                order.setOrderStatus(OrderStatus.PAYMENT_PENDING);
            } else if ("SUCCESS".equals(paymentDTO.status())) {
                order.setOrderStatus(OrderStatus.CONFIRMED);
            } else
                order.setOrderStatus(OrderStatus.PAYMENT_FAILED);

            order = orderRepository.save(order);

            return toOrderResponse(order);
        } catch (Exception e) {
            for (OrderItemRequest orderItemRequest : orderRequest.items()) {
                ProductDTO productDTO = productClient.getProduct(orderItemRequest.productId());
                try {
                    productClient.restoreStock(orderItemRequest.productId(), orderItemRequest.quantity());
                } catch (Exception ex) {
                    System.err.println("CRITICAL: Fail to restore stock for ProductID : "+orderItemRequest.productId()+
                            " : "+ex.getMessage());
                }
            }
            throw new RuntimeException("Checkout failed, all stock has been restored: "+e.getMessage());
        }
    }

    private OrderResponse toOrderResponse(Orders order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> new OrderItemResponse(item.getProductId(),
                        item.getProductName(),
                        item.getQuantity(),
                        item.getPrice()))
                .toList();
        return new OrderResponse(order.getId(),itemResponses,order.getTotalPrice(),order.getOrderStatus().name(),order.getCreatedAt());
    }

    public List<Orders> getOrdersBetweenDates(LocalDateTime start, LocalDateTime end){
        return orderRepository.findByCreatedAtBetween(start, end).orElseThrow(()-> new RuntimeException("Order not found"));
    }

    public Page<Orders> getOrdersBetweenDatesPageable(LocalDateTime start, LocalDateTime end, Pageable pageable){
        return orderRepository.findByCreatedAtBetween(start, end,pageable).orElseThrow(()-> new RuntimeException("Orders not found"));
    }
}
