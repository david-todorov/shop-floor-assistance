package com.shopfloor.backend.services.database.repositories;

import com.shopfloor.backend.services.database.objects.OrderDBO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderDBO, Long> {

    Optional<OrderDBO> findByOrderNumber(String orderNumber);

    boolean existsByOrderNumberAndIdNot(String orderNumber, Long id);

}
