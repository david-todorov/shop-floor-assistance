package com.shopfloor.backend.services.database.repositories;

import com.shopfloor.backend.services.database.objects.OrderDBO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderDBO, Long> {

    boolean existsByOrderNumber(String orderNumber);
}
