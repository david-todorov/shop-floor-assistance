package com.shopfloor.backend.database.repositories;

import com.shopfloor.backend.database.objects.ExecutionDBO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExecutionRepository extends JpaRepository<ExecutionDBO, Long> {
    Optional<ExecutionDBO> findById(Long exeId);
}
