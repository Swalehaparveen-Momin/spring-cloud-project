package com.example.productservice.controller;

import com.example.productservice.entity.Product;
import com.example.productservice.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {


    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAll(){
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @PathVariable Long id
    ){
        try {
            return new ResponseEntity<>(productService.getById(id),HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/{id}/reduce-stock")
    public ResponseEntity<Void> reduceStock(
            @PathVariable Long id,
            @RequestParam int quantity
    ){
        try{
            productService.reduceStock(id, quantity);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/restore-stock")
    public ResponseEntity<Void> restoreStock(
            @PathVariable Long id,
            @RequestParam int quantity
    ){
        try{
            productService.restoreStock(id, quantity);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchByName(
            @RequestParam String name
    ){
        try {
            return new ResponseEntity<>(productService.searchByName(name), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search-by-keyword")
    public  ResponseEntity<List<Product>> searchByKeyword(
            @RequestParam String keyword
    ){
        List<Product> products = productService.searchByKeyword(keyword);
        if(products!= null){
            return new  ResponseEntity<>(products, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/search-pagewise")
    public ResponseEntity<Page<Product>> searchByKeywordPagewise(
        @RequestParam String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "1") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> resultProducts = productService.searchByKeywordPagewise(keyword, pageable);
        return new ResponseEntity<>(resultProducts, HttpStatus.OK);
    }
}
