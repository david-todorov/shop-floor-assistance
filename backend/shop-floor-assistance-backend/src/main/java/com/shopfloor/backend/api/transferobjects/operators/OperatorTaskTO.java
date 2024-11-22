package com.shopfloor.backend.api.transferobjects.operators;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Transfer object for operator task.
 * Contains details about the task, including its name, description, and associated items.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Getter
@Setter
public class OperatorTaskTO {

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
     * The list of items associated with the task, which cannot be null.
     */
    @NotNull(message = "Items list cannot be null")
    @Valid
    private List<@NotNull(message = "Items cannot be null") OperatorItemTO> items;

    /**
     * Constructs an OperatorTaskTO with an empty list of items.
     */
    public OperatorTaskTO() {
        this.items = new ArrayList<OperatorItemTO>();
    }
}
