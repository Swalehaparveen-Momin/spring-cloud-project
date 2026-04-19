package com.example.productservice.service;

import com.example.productservice.dao.ProductRepository;
import com.example.productservice.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

   private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public List<Product> getAll(){
        return productRepository.findAll();
    }

    public Product getById(Long id){
        return productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product Not Found"));
    }

    public Product reduceStock(Long id, int quantity){
        Product p = getById(id);
        if(quantity > p.getStockQuantity())
            throw new RuntimeException("Insufficient stock for product - "+p.getName()+". The available quantity is "+p.getStockQuantity()+" and requested quantity is "+quantity);
        p.setStockQuantity(p.getStockQuantity() - quantity);
        return productRepository.save(p);

    }
    public Product restoreStock(Long id, int quantity){
        Product p = getById(id);
        p.setStockQuantity(p.getStockQuantity() + quantity);
        return productRepository.save(p);

    }
    public List<Product> searchByName(String name) {
        return productRepository.findProductByNameContainingIgnoreCase(name).orElseThrow(() -> new RuntimeException("Product not found"));
    }
    public List<Product> searchByKeyword(String keyword){
        return productRepository.findByKeyword(keyword);
    }

    public Page<Product> searchByKeywordPagewise(String keyword, Pageable pageable){
        return productRepository.searchByKeywordPagewise(keyword, pageable);
    }


}
