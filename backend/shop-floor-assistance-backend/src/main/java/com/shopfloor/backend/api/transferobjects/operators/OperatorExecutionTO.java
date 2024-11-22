package com.shopfloor.backend.api.transferobjects.operators;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Transfer object for operator execution.
 * Contains details about the execution, including its start and finish times, whether it was aborted, and the IDs of the users who started and finished it.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Getter
@Setter
public class OperatorExecutionTO {
    /**
     * The unique identifier for the operator execution.
     */
    private Long id;

    /**
     * The date and time when the execution started.
     */
    private Date startedAt;

    /**
     * The date and time when the execution finished.
     */
    private Date finishedAt;

    /**
     * Indicates whether the execution was aborted.
     */
    private Boolean aborted;

    /**
     * The ID of the user who started the execution.
     */
    private Long startedBy;

    /**
     * The ID of the user who finished the execution.
     */
    private Long finishedBy;
}
