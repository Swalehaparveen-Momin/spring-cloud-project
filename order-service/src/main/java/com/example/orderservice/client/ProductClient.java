package com.example.orderservice.client;

import com.example.orderservice.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service", fallback = ProductClientFallback.class)
public interface ProductClient {
    @GetMapping("/api/products/{id}")
    ProductDTO getProduct(@PathVariable Long id);

    @PutMapping("/api/products/{id}/reduce-stock")
    ResponseEntity<Void> reduceStock(@PathVariable Long id, @RequestParam int quantity);

    @PutMapping("/api/products/{id}/restore-stock")
    ResponseEntity<Void> restoreStock(@PathVariable Long id, @RequestParam int quantity);
}
