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
public class OperatorTaskTO {

    private Long id;

    @NotEmpty(message = "Task name can not be null or empty")
    private String name;

    private String description;

    @NotNull(message = "Items list cannot be null")
    @Valid
    private List<@NotNull(message = "Items cannot be null") OperatorItemTO> items;

    public OperatorTaskTO() {
        this.items = new ArrayList<OperatorItemTO>();
    }
}
