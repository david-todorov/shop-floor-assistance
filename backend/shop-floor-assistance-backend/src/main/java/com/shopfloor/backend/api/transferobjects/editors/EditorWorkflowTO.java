package com.shopfloor.backend.api.transferobjects.editors;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Transfer object for editor workflow.
 * Contains details about the workflow, including its tasks and metadata.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Getter
@Setter
@NotNull(message = "Workflow can not be null")
public class EditorWorkflowTO {

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
     * The ID of the user who created the workflow record.
     */
    private Long createdBy;

    /**
     * The ID of the user who last updated the workflow record.
     */
    private Long updatedBy;

    /**
     * The date and time when the workflow record was created.
     */
    private Date createdAt;

    /**
     * The date and time when the workflow record was last updated.
     */
    private Date updatedAt;

    /**
     * The list of tasks associated with the workflow, which cannot be null.
     */
    @Valid
    @NotNull
    private List<@NotNull(message = "Tasks cannot be null") EditorTaskTO> tasks;
    /**
     * Constructs an EditorWorkflowTO with an empty list of tasks.
     */
    public EditorWorkflowTO() {
        this.tasks = new ArrayList<EditorTaskTO>();
    }
}
