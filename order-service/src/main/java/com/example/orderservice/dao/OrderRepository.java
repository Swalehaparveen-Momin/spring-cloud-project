package com.example.orderservice.dao;

import com.example.orderservice.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Optional<List <Orders>>findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    Optional<Page<Orders>> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
