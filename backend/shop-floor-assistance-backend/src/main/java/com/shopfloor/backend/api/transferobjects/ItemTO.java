package com.shopfloor.backend.api.transferobjects;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ItemTO {

    private Long id;

    private String name;

    private String description;

    private Integer timeRequired;

    private Long createdBy;

    private Long updatedBy;

    private Date createdAt;

    private Date updatedAt;

    public ItemTO() {

    }

    public ItemTO(String name, String description, Integer timeRequired) {
        this.name = name;
        this.description = description;
        this.timeRequired = timeRequired;
    }
}
