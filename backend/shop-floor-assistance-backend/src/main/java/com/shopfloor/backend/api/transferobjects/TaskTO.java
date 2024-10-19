package com.shopfloor.backend.api.transferobjects;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TaskTO {

    private Long id;

    private String name;

    private String description;

    private Long createdBy;

    private Long updatedBy;

    private Date createdAt;

    private Date updatedAt;

    private List<ItemTO> items;

    public TaskTO() {
        this.items = new ArrayList<ItemTO>();
    }
}
