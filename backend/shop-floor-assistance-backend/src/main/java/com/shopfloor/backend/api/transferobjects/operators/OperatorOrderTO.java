package com.shopfloor.backend.api.transferobjects.operators;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Product cannot be null")
    private OperatorProductTO product;

    public OperatorOrderTO() {
        this.workflows = new ArrayList<OperatorWorkflowTO>();
    }

}
