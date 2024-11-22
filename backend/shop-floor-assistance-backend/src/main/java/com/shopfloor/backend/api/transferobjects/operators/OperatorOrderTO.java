package com.shopfloor.backend.api.transferobjects.operators;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Transfer object for operator order.
 * Contains details about the order, including its number, name, description, workflows, products, equipment, and forecast.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Getter
@Setter
@NotNull
public class OperatorOrderTO {

    /**
     * The unique identifier for the operator order.
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
    private List<@NotNull(message = "Workflow cannot be null") OperatorWorkflowTO> workflows;

    /**
     * The product before the order.
     */
    private OperatorProductTO productBefore;

    /**
     * The product after the order, which cannot be null.
     */
    @NotNull(message = "Product cannot be null")
    private OperatorProductTO productAfter;

    /**
     * The list of equipment associated with the order, which cannot be null and must contain at least one item.
     */
    @NotNull(message = "Equipment list cannot be null")
    @Size(min = 1, message = "Equipment list cannot be empty")
    private List<@NotNull(message = "Equipment cannot be null") OperatorEquipmentTO> equipment;

    /**
     * The forecast for the order.
     */
    private OperatorForecastTO forecast;

    /**
     * Constructs an OperatorOrderTO with empty lists for workflows and equipment, and a new forecast.
     */
    public OperatorOrderTO() {
        this.workflows = new ArrayList<OperatorWorkflowTO>();
        this.equipment = new ArrayList<OperatorEquipmentTO>();
        this.forecast = new OperatorForecastTO();
    }

}
