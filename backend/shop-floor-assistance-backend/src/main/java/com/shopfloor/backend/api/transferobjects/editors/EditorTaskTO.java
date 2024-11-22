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
 * Transfer object for editor task.
 * Contains details about the task, including its items and metadata.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Getter
@Setter
public class EditorTaskTO {

    /**
     * The unique identifier for the task.
     */
    private Long id;

    /**
     * The name of the task, which cannot be null or empty.
     */
    @NotEmpty(message = "Task name can not be null or empty")
    private String name;

    /**
     * The description of the task.
     */
    private String description;

    /**
     * The ID of the user who created the task record.
     */
    private Long createdBy;

    /**
     * The ID of the user who last updated the task record.
     */
    private Long updatedBy;

    /**
     * The date and time when the task record was created.
     */
    private Date createdAt;

    /**
     * The date and time when the task record was last updated.
     */
    private Date updatedAt;

    /**
     * The list of items associated with the task, which cannot be null.
     */
    @NotNull(message = "Items list cannot be null")
    @Valid
    private List<@NotNull(message = "Items cannot be null") EditorItemTO> items;

    /**
     * Constructs an EditorTaskTO with an empty list of items.
     */
    public EditorTaskTO() {
        this.items = new ArrayList<EditorItemTO>();
    }
}
