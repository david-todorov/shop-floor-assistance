package com.shopfloor.backend.api.transferobjects.editors;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class EditorProductTO {
    private Long id;

    @NotEmpty(message = "Order number can not be null or empty")
    private String productNumber;

    @NotEmpty(message = "Name can not be null or empty")
    private String name;

    @NotEmpty(message = "Type can not be null or empty")
    private String type;

    @NotEmpty(message = "Country can not be null or empty")
    private String country;

    @NotEmpty(message = "Package size can not be null or empty")
    private String packageSize;

    @NotEmpty(message = "Package type can not be null or empty")
    private String packageType;

    @NotEmpty(message = "Language can not be null or empty")
    private String language;

    private String description;
    private Long createdBy;
    private Long updatedBy;
    private Date createdAt;
    private Date updatedAt;

    @NotNull(message = "Orders list cannot be null")
    private List<EditorOrderTO> orders;

    public EditorProductTO() {
        this.orders = new ArrayList<EditorOrderTO>();
    }
}
