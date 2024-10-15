package com.shopfloor.backend.api.transferobjects;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskTO {

    private Long id;

    private String name;

    private String description;

    private List<ItemTO> items;

    public TaskTO() {

    }
}
