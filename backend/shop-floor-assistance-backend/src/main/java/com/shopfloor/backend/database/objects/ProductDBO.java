package com.shopfloor.backend.database.objects;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity representing a product in the database.
 * Contains details about the product, including its number, name, type,
 * country, package size, package type, language, description, creator, timestamps, and associated orders.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Entity
@Table(name = "products")
@Setter
@Getter
public class ProductDBO {

    /**
     * Unique identifier for the product.
     * Auto-generated by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private Long id;

    /**
     * Unique number assigned to the product.
     * Cannot be null and must be unique.
     */
    @Column(name = "product_number", nullable = false, unique = true, length = 255)
    private String productNumber;

    /**
     * Name of the product.
     * Cannot be null.
     */
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * Type of the product.
     * Cannot be null.
     */
    @Column(name = "type", nullable = false, length = 255)
    private String type;

    /**
     * Country of origin of the product.
     * Cannot be null.
     */
    @Column(name = "country", nullable = false, length = 255)
    private String country;

    /**
     * Package size of the product.
     * Cannot be null.
     */
    @Column(name = "package_size", nullable = false, length = 255)
    private String packageSize;

    /**
     * Package type of the product.
     * Cannot be null.
     */
    @Column(name = "package_type", nullable = false, length = 255)
    private String packageType;

    /**
     * Language of the product.
     * Cannot be null.
     */
    @Column(name = "language", nullable = false, length = 255)
    private String language;

    /**
     * Description of the product.
     * Stored as a text column in the database.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * ID of the user who created the product.
     */
    @Column(name = "created_by")
    private Long createdBy;

    /**
     * ID of the user who last updated the product.
     */
    @Column(name = "updated_by")
    private Long updatedBy;

    /**
     * Timestamp when the product was created.
     * Cannot be null and is not updatable.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    /**
     * Timestamp when the product was last updated.
     */
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    /**
     * List of orders where this product is the before product.
     * Fetch type is set to EAGER, meaning the orders are loaded immediately.
     */
    @OneToMany(mappedBy = "beforeProduct", fetch = FetchType.EAGER)
    private List<OrderDBO> ordersAsBeforeProduct;

    /**
     * List of orders where this product is the after product.
     * Fetch type is set to EAGER, meaning the orders are loaded immediately.
     */
    @OneToMany(mappedBy = "afterProduct", fetch = FetchType.EAGER)
    private List<OrderDBO> ordersAsAfterProduct;

    /**
     * Constructs a ProductDBO with empty lists for ordersAsBeforeProduct and ordersAsAfterProduct.
     */
    public ProductDBO() {
        this.ordersAsBeforeProduct = new ArrayList<OrderDBO>();
        this.ordersAsAfterProduct = new ArrayList<OrderDBO>();
    }

    /**
     * Clears references to this product in associated orders.
     */
    public void clearOrderReferences() {
        // Clear associations in ordersAsBeforeProduct
        for (OrderDBO order : new ArrayList<>(ordersAsBeforeProduct)) {
            order.clearBeforeProduct();
        }
        // Clear associations in ordersAsAfterProduct
        for (OrderDBO order : new ArrayList<>(ordersAsAfterProduct)) {
            order.clearAfterProduct();
        }
    }

}
