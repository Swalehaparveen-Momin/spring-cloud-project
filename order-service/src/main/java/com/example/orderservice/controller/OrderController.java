package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.entity.Orders;
import com.example.orderservice.service.OrderService;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping
    public List<Orders> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Orders> getById(
            @PathVariable Long id
    ){
           return new ResponseEntity<>(orderService.getById(id),HttpStatus.OK) ;
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(
            @RequestBody OrderRequest orderRequest
            ){
        OrderResponse orderResponse = orderService.checkout(orderRequest);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Orders>> getOrderBetweenDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
            ){
        List<Orders> orders = orderService.getOrdersBetweenDates(start, end);
        if(orders.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/date-range-p")
    public ResponseEntity<Page<Orders>> getOrderBetweenDateRangePagination(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<Orders> orders = orderService.getOrdersBetweenDatesPageable(start, end, pageable);
        if(orders.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

}
