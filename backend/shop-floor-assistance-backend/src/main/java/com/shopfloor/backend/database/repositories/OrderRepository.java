package com.shopfloor.backend.database.repositories;

import com.shopfloor.backend.database.objects.OrderDBO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for accessing Orders data in the database.
 *
 * This interface extends JpaRepository, providing CRUD operations
 * for OrderDBO entities, which represent orders in the database.
 * @author David Todorov (https://github.com/david-todorov)
 */
public interface OrderRepository extends JpaRepository<OrderDBO, Long> {

    /**
     * Finds an order entity by its ID.
     *
     * @param id the ID of the order to find
     * @return an Optional containing the found OrderDBO, or empty if not found
     */
    Optional<OrderDBO> findById(Long id);

    /**
     * Finds an order entity by its order number.
     *
     * @param orderNumber the order number to search for
     * @return an Optional containing the found OrderDBO, or empty if not found
     */
    Optional<OrderDBO> findByOrderNumber(String orderNumber);

    /**
     * Checks if an order entity exists with the given order number and a different ID.
     *
     * @param orderNumber the order number to check
     * @param id the ID to exclude from the check
     * @return true if an order entity exists with the given order number and a different ID, false otherwise
     */
    boolean existsByOrderNumberAndIdNot(String orderNumber, Long id);

    /**
     * Checks if an order entity exists with the given order number.
     *
     * @param orderNumber the order number to check
     * @return true if an order entity exists with the given order number, false otherwise
     */
    boolean existsByOrderNumber(String orderNumber);

}
