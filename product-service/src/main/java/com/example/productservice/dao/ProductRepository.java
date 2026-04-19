package com.example.productservice.dao;

import com.example.productservice.entity.Product;
import org.apache.tomcat.util.digester.ObjectCreateRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<List<Product>> findProductByNameContainingIgnoreCase(String name);

    @Query(""" 
        SELECT p from Product p
          WHERE LOWER(p.name) LIKE LOWER(concat('%', :keyword ,'%'))
            OR LOWER(p.description) LIKE LOWER(concat('%', :keyword, '%')) 
  """)
    List<Product> findByKeyword(@Param("keyword") String keyword);


    @Query("""
        SELECT p FROM Product p
            WHERE lower(p.name) like lower(concat('%', :keyword, '%')) 
                OR lower(p.description) like lower(concat('%', :keyword, '%')) 
    """)
    Page<Product> searchByKeywordPagewise(@Param("keyword") String keyword, Pageable pageable);
}
