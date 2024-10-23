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
@NotNull(message = "Workflow can not be null")
public class EditorWorkflowTO {

    private Long id;

    @NotEmpty(message = "Workflow name can not be null or empty")
    private String name;

    private String description;

    private Long createdBy;

    private Long updatedBy;

    private Date createdAt;

    private Date updatedAt;

    @Valid
    @NotNull
    private List<@NotNull(message = "Tasks cannot be null") EditorTaskTO> tasks;

    public EditorWorkflowTO() {
        this.tasks = new ArrayList<EditorTaskTO>();
    }
}
