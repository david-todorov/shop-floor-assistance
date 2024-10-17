package com.shopfloor.backend.api.transferobjects;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderTO {

    private Long id;

    private String orderNumber;

    private String name;

    private String description;

    private List<WorkflowTO> workflows;

    private Long createdBy;

    private Long updatedBy;

    private Date createdAt;

    private Date updatedAt;

    public OrderTO() {
        this.workflows = new ArrayList<WorkflowTO>();
    }

}
