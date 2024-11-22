package com.shopfloor.backend.database.repositories;

import com.shopfloor.backend.database.objects.EquipmentDBO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for EquipmentDBO entities.
 * Provides methods for performing CRUD operations and custom queries on equipment data.
 * @author David Todorov (https://github.com/david-todorov)
 */
public interface EquipmentRepository extends JpaRepository<EquipmentDBO, Long> {

    /**
     * Finds an equipment entity by its equipment number.
     *
     * @param equipmentNumber the equipment number to search for
     * @return an Optional containing the found EquipmentDBO, or empty if not found
     */
    Optional<EquipmentDBO> findByEquipmentNumber(String equipmentNumber);

    /**
     * Checks if an equipment entity exists with the given equipment number and a different ID.
     *
     * @param equipmentNumber the equipment number to check
     * @param id the ID to exclude from the check
     * @return true if an equipment entity exists with the given equipment number and a different ID, false otherwise
     */
    boolean existsByEquipmentNumberAndIdNot(String equipmentNumber, Long id);

    /**
     * Finds the top referenced equipment entities based on the number of associated orders.
     *
     * @param pageable the pagination information
     * @return a list of the top referenced EquipmentDBO entities
     */
    @Query("SELECT e "
            +
            "FROM EquipmentDBO e "
            +
            "JOIN e.orders o "
            +
            "GROUP BY e "
            +
            "ORDER BY COUNT(o) DESC")
    List<EquipmentDBO> findTopReferencedEquipment(Pageable pageable);
}
