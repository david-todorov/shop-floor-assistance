package com.shopfloor.backend.database.repositories;

import com.shopfloor.backend.database.objects.ProductDBO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ProductDBO entities.
 * Provides methods for performing CRUD operations and custom queries on product data.
 * @author David Todorov (https://github.com/david-todorov)
 */
public interface ProductRepository extends JpaRepository<ProductDBO, Long> {

    /**
     * Finds a product entity by its product number.
     *
     * @param productNumber the product number to search for
     * @return an Optional containing the found ProductDBO, or empty if not found
     */
    Optional<ProductDBO> findByProductNumber(String productNumber);

    /**
     * Checks if a product entity exists with the given product number and a different ID.
     *
     * @param productNumber the product number to check
     * @param id the ID to exclude from the check
     * @return true if a product entity exists with the given product number and a different ID, false otherwise
     */
    boolean existsByProductNumberAndIdNot(String productNumber, Long id);

    /**
     * Finds the top referenced product entities based on the number of associated orders.
     *
     * @param pageable the pagination information
     * @return a list of the top referenced ProductDBO entities
     */
    @Query("SELECT p "
            + "FROM ProductDBO p "
            + "JOIN p.ordersAsAfterProduct o "
            + "GROUP BY p "
            + "ORDER BY COUNT(o) DESC")
    List<ProductDBO> findTopReferencedProducts(Pageable pageable);

    /**
     * Finds a product entity by its ID.
     *
     * @param productId the ID of the product to find
     * @return an Optional containing the found ProductDBO, or empty if not found
     */
    Optional<ProductDBO> findById(Long productId);

}
