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
public class EditorTaskTO {

    private Long id;

    @NotEmpty(message = "Task name can not be null or empty")
    private String name;

    private String description;

    private Long createdBy;

    private Long updatedBy;

    private Date createdAt;

    private Date updatedAt;

    @NotNull(message = "Items list cannot be null")
    @Valid
    private List<@NotNull(message = "Items cannot be null") EditorItemTO> items;

    public EditorTaskTO() {
        this.items = new ArrayList<EditorItemTO>();
    }
}
