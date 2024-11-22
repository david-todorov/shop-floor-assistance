package com.shopfloor.backend.database.mappers;

import com.shopfloor.backend.database.objects.ExecutionDBO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Mapper class for converting and managing ExecutionDBO objects.
 * Provides methods to initialize, finish, and abort ExecutionDBO entities.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Component
public class ExecutionDBOMapper {

    /**
     * Initializes an ExecutionDBO with the given executor ID.
     * @param executorId the ID of the user starting the execution
     * @return the initialized ExecutionDBO
     */
    public ExecutionDBO initializeExecutionDBO(Long executorId) {
        ExecutionDBO executionDBO = new ExecutionDBO();
        executionDBO.setStartedBy(executorId);
        executionDBO.setStartedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return executionDBO;
    }

    /**
     * Marks an ExecutionDBO as finished with the given finisher ID.
     * @param executionDBO the ExecutionDBO to finish
     * @param finisherId the ID of the user finishing the execution
     * @return the finished ExecutionDBO
     */
    public ExecutionDBO finishExecutionDBO(ExecutionDBO executionDBO, Long finisherId) {
        executionDBO.setFinishedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        executionDBO.setFinishedBy(finisherId);
        return executionDBO;
    }

    /**
     * Marks an ExecutionDBO as aborted.
     * @param executionDBO the ExecutionDBO to abort
     * @return the aborted ExecutionDBO
     */
    public ExecutionDBO abortExecutionDBO(ExecutionDBO executionDBO) {
        executionDBO.setAborted(true);
        return executionDBO;
    }
}
