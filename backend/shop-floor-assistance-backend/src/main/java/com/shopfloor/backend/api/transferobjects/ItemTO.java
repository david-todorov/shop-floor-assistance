package com.shopfloor.backend.api.transferobjects;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ItemTO {

    private Long id;

    private String name;

    private String shortDescription;

    private String longDescription;

    private Integer timeRequired;

    private Date createdAt;

    private Date updatedAt;

    public ItemTO() {

    }
}
