package com.shopfloor.backend.database.repositories;

import com.shopfloor.backend.database.objects.OrderDBO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for accessing Orders data in the database.
 *
 * This interface extends JpaRepository, providing CRUD operations
 * for OrderDBO entities, which represent orders in the database.
 * This interface will probably be used in some of our services
 */
public interface OrderRepository extends JpaRepository<OrderDBO, Long> {

    Optional<OrderDBO> findById(Long id);

    Optional<OrderDBO> findByOrderNumber(String orderNumber);

    boolean existsByOrderNumberAndIdNot(String orderNumber, Long id);

    boolean existsByOrderNumber(String orderNumber);

}
