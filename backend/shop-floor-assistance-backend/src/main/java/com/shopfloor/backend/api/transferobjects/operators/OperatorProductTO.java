package com.shopfloor.backend.api.transferobjects.operators;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OperatorProductTO {
    private Long id;
    private String productNumber;
    private String name;
    private String type;
    private String country;
    private String packageSize;
    private String packageType;
    private String language;
    private String description;
    private List<OperatorOrderTO> orders;

    public OperatorProductTO() {
        this.orders = new ArrayList<OperatorOrderTO>();
    }
}
