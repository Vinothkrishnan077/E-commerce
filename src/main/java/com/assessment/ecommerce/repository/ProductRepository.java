package com.assessment.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.assessment.ecommerce.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByProductUrl(String productUrl);

    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:category IS NULL OR LOWER(p.category) LIKE LOWER(CONCAT('%', :category, '%'))) " +
            "AND (:minPrice IS NULL OR p.newPrice >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.newPrice <= :maxPrice)")
    List<Product> searchProducts(
            @Param("name") String name,
            @Param("category") String category,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice);

}
