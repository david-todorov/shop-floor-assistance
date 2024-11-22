package com.shopfloor.backend.api.transferobjects.editors;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Transfer object for editor item.
 * Contains details about the item, including its name, description, and time required.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Getter
@Setter
public class EditorItemTO {

    /**
     * The unique identifier for the item.
     */
    private Long id;

    /**
     * The name of the item, which cannot be null or empty.
     */
    @NotEmpty(message = "Item name can not be null or empty")
    private String name;

    /**
     * The description of the item.
     */
    private String description;

    /**
     * The time required for the item, must be greater than zero.
     */
    @Min(value = 1, message = "Time required must be greater than zero")
    private Integer timeRequired;

    /**
     * The ID of the user who created the item record.
     */
    private Long createdBy;

    /**
     * The ID of the user who last updated the item record.
     */
    private Long updatedBy;

    /**
     * The date and time when the item record was created.
     */
    private Date createdAt;

    /**
     * The date and time when the item record was last updated.
     */
    private Date updatedAt;

    /**
     * Constructs an EditorItemTO with default values.
     */
    public EditorItemTO() {

    }

    /**
     * Constructs an EditorItemTO with the specified name, description, and time required.
     *
     * @param name the name of the item
     * @param description the description of the item
     * @param timeRequired the time required for the item, must be greater than zero
     */
    public EditorItemTO(String name, String description, Integer timeRequired) {
        this.name = name;
        this.description = description;
        this.timeRequired = timeRequired;
    }
}
