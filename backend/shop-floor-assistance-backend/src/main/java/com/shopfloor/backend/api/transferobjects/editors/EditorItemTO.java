package com.shopfloor.backend.api.transferobjects.editors;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EditorItemTO {

    private Long id;

    @NotEmpty(message = "Item name can not be null or empty")
    private String name;

    private String description;

    //TODO make it valid for integers only
    @Min(value = 1, message = "Time required must be greater than zero")
    private Integer timeRequired;

    private Long createdBy;

    private Long updatedBy;

    private Date createdAt;

    private Date updatedAt;

    public EditorItemTO() {

    }

    public EditorItemTO(String name, String description, Integer timeRequired) {
        this.name = name;
        this.description = description;
        this.timeRequired = timeRequired;
    }
}
