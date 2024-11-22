package com.shopfloor.backend.api.transferobjects.editors;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Transfer object for editor product.
 * Contains details about the product, including its number, name, type, country, package details, and associated orders.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Getter
@Setter
public class EditorProductTO {
    /**
     * The unique identifier for the product.
     */
    private Long id;

    /**
     * The product number, which cannot be null or empty.
     */
    @NotEmpty(message = "Product number can not be null or empty")
    private String productNumber;

    /**
     * The name of the product, which cannot be null or empty.
     */
    @NotEmpty(message = "Name can not be null or empty")
    private String name;

    /**
     * The type of the product, which cannot be null or empty.
     */
    @NotEmpty(message = "Type can not be null or empty")
    private String type;

    /**
     * The country of origin of the product, which cannot be null or empty.
     */
    @NotEmpty(message = "Country can not be null or empty")
    private String country;

    /**
     * The size of the product package, which cannot be null or empty.
     */
    @NotEmpty(message = "Package size can not be null or empty")
    private String packageSize;

    /**
     * The type of the product package, which cannot be null or empty.
     */
    @NotEmpty(message = "Package type can not be null or empty")
    private String packageType;

    /**
     * The language of the product, which cannot be null or empty.
     */
    @NotEmpty(message = "Language can not be null or empty")
    private String language;

    /**
     * The description of the product.
     */
    private String description;

    /**
     * The ID of the user who created the product record.
     */
    private Long createdBy;

    /**
     * The ID of the user who last updated the product record.
     */
    private Long updatedBy;

    /**
     * The date and time when the product record was created.
     */
    private Date createdAt;

    /**
     * The date and time when the product record was last updated.
     */
    private Date updatedAt;

    /**
     * The list of orders associated with the product after a certain point.
     */
    private List<EditorOrderTO> ordersAfter;

    /**
     * The list of orders associated with the product before a certain point.
     */
    private List<EditorOrderTO> ordersBefore;

    /**
     * Constructs an EditorProductTO with empty lists for ordersAfter and ordersBefore.
     */
    public EditorProductTO() {
        this.ordersAfter = new ArrayList<EditorOrderTO>();
        this.ordersBefore = new ArrayList<EditorOrderTO>();
    }
}
