package com.example.orderservice.client;

import com.example.orderservice.dto.ProductDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductClientFallback implements ProductClient {
    @Override
    public ProductDTO getProduct(Long id) {
        throw new RuntimeException("Product service is unavailable. Product validation is temporarily unavailable.");
    }

    @Override
    public ResponseEntity<Void> reduceStock(Long id, int quantity) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<Void> restoreStock(Long id, int quantity) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
