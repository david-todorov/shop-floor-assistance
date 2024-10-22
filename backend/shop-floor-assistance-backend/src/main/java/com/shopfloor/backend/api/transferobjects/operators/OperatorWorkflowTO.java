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
@NotNull(message = "Workflow can not be null")
public class OperatorWorkflowTO {

    private Long id;

    @NotEmpty(message = "Workflow name can not be null or empty")
    private String name;

    private String description;

    @Valid
    @NotNull
    private List<@NotNull(message = "Tasks cannot be null") OperatorTaskTO> tasks;

    public OperatorWorkflowTO() {
        this.tasks = new ArrayList<OperatorTaskTO>();
    }
}
