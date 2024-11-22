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
 * Transfer object for editor order.
 * Contains details about the order, including its workflows, equipment, and products.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Getter
@Setter
@NotNull
public class EditorOrderTO {

    /**
     * The unique identifier for the order.
     */
    private Long id;

    /**
     * The order number, which cannot be null or empty.
     */
    @NotEmpty(message = "Order number can not be null or empty")
    private String orderNumber;

    /**
     * The name of the order, which cannot be null or empty.
     */
    @NotEmpty(message = "Order name can not be null or empty")
    private String name;

    /**
     * The description of the order.
     */
    private String description;

    /**
     * The list of workflows associated with the order, which cannot be null.
     */
    @NotNull(message = "Workflows list cannot be null")
    @Valid
    private List<@NotNull(message = "Workflow cannot be null") EditorWorkflowTO> workflows;

    /**
     * The ID of the user who created the order record.
     */
    private Long createdBy;

    /**
     * The ID of the user who last updated the order record.
     */
    private Long updatedBy;

    /**
     * The date and time when the order record was created.
     */
    private Date createdAt;

    /**
     * The date and time when the order record was last updated.
     */
    private Date updatedAt;

    /**
     * The product before the order.
     */
    private EditorProductTO productBefore;

    /**
     * The product after the order.
     */
    private EditorProductTO productAfter;

    @NotNull(message = "Equipment list cannot be null")
    private List<@NotNull(message = "Equipment cannot be null") EditorEquipmentTO> equipment;

    private EditorForecastTO forecast;

    /**
     * Constructs an EditorOrderTO with empty lists for workflows and equipment, and a new forecast object.
     */
    public EditorOrderTO() {
        this.workflows = new ArrayList<EditorWorkflowTO>();
        this.equipment = new ArrayList<EditorEquipmentTO>();
        this.forecast = new EditorForecastTO();
    }

}
