package com.shopfloor.backend.api.transferobjects.operators;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NotNull
public class OperatorOrderTO {

    private Long id;

    @NotEmpty(message = "Order number can not be null or empty")
    private String orderNumber;

    @NotEmpty(message = "Order name can not be null or empty")
    private String name;

    private String description;

    @NotNull(message = "Workflows list cannot be null")
    @Valid
    private List<@NotNull(message = "Workflow cannot be null") OperatorWorkflowTO> workflows;

    private OperatorProductTO productBefore;

    @NotNull(message = "Product cannot be null")
    private OperatorProductTO productAfter;

    @NotNull(message = "Equipment list cannot be null")
    @Size(min = 1, message = "Equipment list cannot be empty")
    private List<@NotNull(message = "Equipment cannot be null") OperatorEquipmentTO> equipment;

    private OperatorForecastTO forecast;

    public OperatorOrderTO() {
        this.workflows = new ArrayList<OperatorWorkflowTO>();
        this.equipment = new ArrayList<OperatorEquipmentTO>();
        this.forecast = new OperatorForecastTO();
    }

}
