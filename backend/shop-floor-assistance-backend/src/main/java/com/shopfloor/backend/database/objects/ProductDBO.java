package com.shopfloor.backend.database.objects;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "products")
@Setter
@Getter
public class ProductDBO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_number", nullable = false, unique = true, length = 255)
    private String productNumber;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "type", nullable = false, length = 255)
    private String type;

    @Column(name = "country", nullable = false, length = 255)
    private String country;

    @Column(name = "package_size", nullable = false, length = 255)
    private String packageSize;

    @Column(name = "package_type", nullable = false, length = 255)
    private String packageType;

    @Column(name = "language", nullable = false, length = 255)
    private String language;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @OneToMany(mappedBy = "beforeProduct", fetch = FetchType.EAGER)
    private List<OrderDBO> ordersAsBeforeProduct;

    @OneToMany(mappedBy = "afterProduct", fetch = FetchType.EAGER)
    private List<OrderDBO> ordersAsAfterProduct;

    public ProductDBO() {
        this.ordersAsBeforeProduct = new ArrayList<OrderDBO>();
        this.ordersAsAfterProduct = new ArrayList<OrderDBO>();
    }

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
