package com.shopfloor.backend.database.repositories;

import com.shopfloor.backend.database.objects.EquipmentDBO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<EquipmentDBO, Long> {
    Optional<EquipmentDBO> findByEquipmentNumber(String orderNumber);

    boolean existsByEquipmentNumberAndIdNot(String equipmentNumber, Long id);

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
