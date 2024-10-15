package com.shopfloor.backend.api.transferobjects;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderTO {

    private Long id;

    private String orderNumber;

    private String name;

    private String shortDescription;

    private String longDescription;

    private List<WorkflowTO> workflows;

    private Long createdBy;

    public OrderTO() {
        this.workflows = new ArrayList<WorkflowTO>();
    }

}
