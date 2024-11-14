package com.shopfloor.backend.database.mappers;

import com.shopfloor.backend.database.objects.ExecutionDBO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class ExecutionDBOMapper {

    public ExecutionDBO initializeExecutionDBO(Long executorId) {

        ExecutionDBO executionDBO = new ExecutionDBO();
        executionDBO.setStartedBy(executorId);
        executionDBO.setStartedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        return executionDBO;
    }

    public ExecutionDBO finishExecutionDBO(ExecutionDBO executionDBO, Long finisherId) {
        executionDBO.setFinishedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        executionDBO.setFinishedBy(finisherId);
        return executionDBO;
    }

    public ExecutionDBO abortExecutionDBO(ExecutionDBO executionDBO) {
        executionDBO.setAborted(true);
        return executionDBO;
    }
}
