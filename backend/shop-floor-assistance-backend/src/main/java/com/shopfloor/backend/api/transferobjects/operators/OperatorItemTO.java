package com.shopfloor.backend.api.transferobjects.operators;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperatorItemTO {

    private Long id;

    @NotEmpty(message = "Item name can not be null or empty")
    private String name;

    private String description;

    //TODO make it valid for integers only
    @Min(value = 1, message = "Time required must be greater than zero")
    private Integer timeRequired;

    public OperatorItemTO() {

    }

}
