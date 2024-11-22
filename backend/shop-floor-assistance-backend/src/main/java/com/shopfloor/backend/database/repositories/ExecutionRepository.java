package com.shopfloor.backend.database.repositories;

import com.shopfloor.backend.database.objects.ExecutionDBO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for ExecutionDBO entities.
 * Provides methods for performing CRUD operations on execution data.
 * @author David Todorov (https://github.com/david-todorov)
 */
public interface ExecutionRepository extends JpaRepository<ExecutionDBO, Long> {

    /**
     * Finds an execution entity by its ID.
     *
     * @param exeId the ID of the execution to find
     * @return an Optional containing the found ExecutionDBO, or empty if not found
     */
    Optional<ExecutionDBO> findById(Long exeId);
}
