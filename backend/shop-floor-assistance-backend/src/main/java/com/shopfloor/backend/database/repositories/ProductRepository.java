package com.shopfloor.backend.database.repositories;

import com.shopfloor.backend.database.objects.ProductDBO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductDBO, Long> {

    Optional<ProductDBO> findByProductNumber(String orderNumber);

    boolean existsByProductNumberAndIdNot(String productNumber, Long id);
}
