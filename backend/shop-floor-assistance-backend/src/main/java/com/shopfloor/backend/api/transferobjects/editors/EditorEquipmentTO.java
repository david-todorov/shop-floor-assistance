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
@NotNull(message = "Equipment can not be null")
public class EditorEquipmentTO {
    private Long id;

    @NotEmpty(message = "Equipment number can not be null or empty")
    private String equipmentNumber;

    @NotEmpty(message = "Name can not be null or empty")
    private String name;

    @NotEmpty(message = "Type can not be null or empty")
    private String type;

    @NotNull(message = "Orders list cannot be null")
    private List<EditorOrderTO> orders;

    private String description;
    private Long createdBy;
    private Long updatedBy;
    private Date createdAt;
    private Date updatedAt;

    public EditorEquipmentTO() {
        this.orders = new ArrayList<EditorOrderTO>();
    }
}
