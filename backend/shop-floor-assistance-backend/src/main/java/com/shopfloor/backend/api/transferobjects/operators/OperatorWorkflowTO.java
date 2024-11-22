package com.shopfloor.backend.api.transferobjects.operators;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Transfer object for operator workflow.
 * Contains details about the workflow, including its name, description, and associated tasks.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Getter
@Setter
@NotNull(message = "Workflow can not be null")
public class OperatorWorkflowTO {

    /**
     * The unique identifier for the workflow.
     */
    private Long id;

    /**
     * The name of the workflow, which cannot be null or empty.
     */
    @NotEmpty(message = "Workflow name can not be null or empty")
    private String name;

    /**
     * The description of the workflow.
     */
    private String description;

    /**
     * The list of tasks associated with the workflow, which cannot be null.
     */
    @Valid
    @NotNull
    private List<@NotNull(message = "Tasks cannot be null") OperatorTaskTO> tasks;

    /**
     * Constructs an OperatorWorkflowTO with an empty list of tasks.
     */
    public OperatorWorkflowTO() {
        this.tasks = new ArrayList<OperatorTaskTO>();
    }
}
