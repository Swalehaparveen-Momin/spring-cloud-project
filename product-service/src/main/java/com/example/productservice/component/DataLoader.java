package com.example.productservice.component;

import com.example.productservice.dao.ProductRepository;
import com.example.productservice.entity.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataLoader(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(productRepository.count() == 0){
            productRepository.saveAll(
                    List.of(
                            new Product("Wireless BT","Head phone",new BigDecimal("1200"),150),
                            new Product("Keyboard","Keyboard",new BigDecimal("1000"),150),
                            new Product("SSD","Hard disk",new BigDecimal("12000"),150),
                            new Product("Mouse","wireless Mouse",new BigDecimal("1100"),150),
                            new Product("Lenovo Charger","Charger",new BigDecimal("1000"),150),
                            new Product("Intel Wireless BT","Head phone",new BigDecimal("1200"),150),
                            new Product("Intel Keyboard","Keyboard",new BigDecimal("1000"),150),
                            new Product("Ryzen SSD","Hard disk",new BigDecimal("12000"),150),
                            new Product("IBoll Mouse","wireless Mouse",new BigDecimal("1100"),150),
                            new Product("HP Charger","Charger",new BigDecimal("1000"),150)
                    )
            );
        }

    }
}
