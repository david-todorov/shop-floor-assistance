package com.shopfloor.backend.api.transferobjects.editors;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NotNull
public class EditorOrderTO {

    private Long id;

    @NotEmpty(message = "Order number can not be null or empty")
    private String orderNumber;

    @NotEmpty(message = "Order name can not be null or empty")
    private String name;

    private String description;

    @NotNull(message = "Workflows list cannot be null")
    @Valid
    private List<@NotNull(message = "Workflow cannot be null") EditorWorkflowTO> workflows;

    private Long createdBy;

    private Long updatedBy;

    private Date createdAt;

    private Date updatedAt;

    private EditorProductTO productBefore;

    @NotNull(message = "Product cannot be null")
    private EditorProductTO productAfter;

    @NotNull(message = "Equipment list cannot be null")
    private List<@NotNull(message = "Equipment cannot be null")EditorEquipmentTO> equipment;

    public EditorOrderTO() {
        this.workflows = new ArrayList<EditorWorkflowTO>();
        this.equipment = new ArrayList<EditorEquipmentTO>();
    }

}
