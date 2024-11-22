package com.shopfloor.backend.api.transferobjects.operators;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Transfer object for operator product.
 * Contains details about the product, including its number, name, type, country, package size, package type, language, description, and associated orders.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Getter
@Setter
public class OperatorProductTO {

    /**
     * The unique identifier for the product.
     */
    private Long id;

    /**
     * The product number, which cannot be null or empty.
     */
    private String productNumber;

    /**
     * The name of the product, which cannot be null or empty.
     */
    private String name;

    /**
     * The type of the product, which cannot be null or empty.
     */
    private String type;

    /**
     * The country of origin for the product.
     */
    private String country;

    /**
     * The size of the product package.
     */
    private String packageSize;

    /**
     * The type of the product package.
     */
    private String packageType;

    /**
     * The language associated with the product.
     */
    private String language;

    /**
     * The description of the product.
     */
    private String description;

    /**
     * The list of orders before the product, which cannot be null.
     */
    private List<OperatorOrderTO> ordersBefore;

    /**
     * The list of orders after the product, which cannot be null.
     */
    private List<OperatorOrderTO> ordersAfter;

    /**
     * Constructs an OperatorProductTO with empty lists for orders before and after.
     */
    public OperatorProductTO() {
        this.ordersBefore = new ArrayList<OperatorOrderTO>();
        this.ordersAfter = new ArrayList<OperatorOrderTO>();
    }
}
