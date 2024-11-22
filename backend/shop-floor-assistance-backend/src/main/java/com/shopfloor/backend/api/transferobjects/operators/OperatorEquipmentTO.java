package com.shopfloor.backend.api.transferobjects.operators;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Transfer object for operator equipment.
 * Contains details about the equipment, including its number, name, type, associated orders, and description.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Getter
@Setter
@NotNull(message = "Equipment can not be null")
public class OperatorEquipmentTO {

    /**
     * The unique identifier for the equipment.
     */
    private Long id;

    /**
     * The equipment number, which cannot be null or empty.
     */
    @NotEmpty(message = "Equipment number can not be null or empty")
    private String equipmentNumber;

    /**
     * The name of the equipment, which cannot be null or empty.
     */
    @NotEmpty(message = "Name can not be null or empty")
    private String name;

    /**
     * The type of the equipment, which cannot be null or empty.
     */
    @NotEmpty(message = "Type can not be null or empty")
    private String type;

    /**
     * The list of orders associated with the equipment, which cannot be null.
     */
    @NotNull(message = "Orders list cannot be null")
    private List<OperatorOrderTO> orders;

    /**
     * The description of the equipment.
     */
    private String description;

    /**
     * Constructs an OperatorEquipmentTO with an empty list of orders.
     */
    public OperatorEquipmentTO() {
        this.orders = new ArrayList<OperatorOrderTO>();
    }
}
