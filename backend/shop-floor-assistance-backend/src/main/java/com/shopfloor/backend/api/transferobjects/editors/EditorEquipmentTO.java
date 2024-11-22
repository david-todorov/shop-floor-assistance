package com.shopfloor.backend.api.transferobjects.editors;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Transfer object for editor equipment.
 * Contains details about the equipment, including its number, name, type, and associated orders.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Getter
@Setter
@NotNull(message = "Equipment can not be null")
public class EditorEquipmentTO {

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
     * The list of associated orders, which cannot be null.
     */
    @NotNull(message = "Orders list cannot be null")
    private List<EditorOrderTO> orders;

    /**
     * The description of the equipment.
     */
    private String description;

    /**
     * The ID of the user who created the equipment record.
     */
    private Long createdBy;

    /**
     * The ID of the user who last updated the equipment record.
     */
    private Long updatedBy;

    /**
     * The date and time when the equipment record was created.
     */
    private Date createdAt;

    /**
     * The date and time when the equipment record was last updated.
     */
    private Date updatedAt;

    /**
     * Constructs an EditorEquipmentTO with an empty list of orders.
     */
    public EditorEquipmentTO() {
        this.orders = new ArrayList<EditorOrderTO>();
    }
}
