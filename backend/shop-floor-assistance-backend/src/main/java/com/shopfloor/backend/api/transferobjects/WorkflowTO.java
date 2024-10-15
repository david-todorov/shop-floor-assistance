package com.shopfloor.backend.api.transferobjects;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WorkflowTO {

    private Long id;

    private String name;

    private String description;

    private List<TaskTO> tasks;

    public WorkflowTO() {
        this.tasks = new ArrayList<TaskTO>();
    }
}
