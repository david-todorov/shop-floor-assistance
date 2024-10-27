package com.shopfloor.backend.database.repositories;

import com.shopfloor.backend.database.objects.ProductDBO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductDBO, Long> {

    Optional<ProductDBO> findByProductNumber(String orderNumber);

    boolean existsByProductNumberAndIdNot(String productNumber, Long id);

    @Query("SELECT p "
            +
            "FROM ProductDBO p "
            +
            "JOIN p.orders o "
            +
            "GROUP BY p "
            +
            "ORDER BY COUNT(o) DESC")
    List<ProductDBO> findTopReferencedProducts(Pageable pageable);
}
