package com.shopfloor.backend.api.transferobjects.operators;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * Transfer object for operator item.
 * Contains details about the item, including its name, description, and time required.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Getter
@Setter
public class OperatorItemTO {

    /**
     * The unique identifier for the operator item.
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
     * The time required for the item, which must be greater than zero.
     */
    @Min(value = 1, message = "Time required must be greater than zero")
    private Integer timeRequired;

    /**
     * Constructs an OperatorItemTO.
     */
    public OperatorItemTO() {

    }

}
