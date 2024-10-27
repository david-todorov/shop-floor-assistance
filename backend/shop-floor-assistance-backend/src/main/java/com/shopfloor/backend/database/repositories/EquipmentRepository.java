package com.shopfloor.backend.database.repositories;

import com.shopfloor.backend.database.objects.EquipmentDBO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<EquipmentDBO, Long> {
    Optional<EquipmentDBO> findByEquipmentNumber(String orderNumber);

    boolean existsByEquipmentNumberAndIdNot(String equipmentNumber, Long id);
}
