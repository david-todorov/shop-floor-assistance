package com.shopfloor.backend.database.objects;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Setter
@Getter
public class OrderDBO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true, length = 255)
    private String orderNumber;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<WorkflowDBO> workflows;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "order_equipment",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_id")
    )
    private List<EquipmentDBO> equipment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "before_product_id")
    private ProductDBO beforeProduct;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "after_product_id")
    private ProductDBO afterProduct;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy = "order")
    private List<ExecutionDBO> executions;

    public OrderDBO() {
        this.equipment = new ArrayList<EquipmentDBO>();
        this.workflows = new ArrayList<WorkflowDBO>();
        this.executions = new ArrayList<ExecutionDBO>();
    }

    public void setBeforeProduct(ProductDBO newBeforeProduct) {
        // If there is an existing beforeProduct, remove the association
        if (this.beforeProduct != null) {
            this.beforeProduct.getOrdersAsBeforeProduct().remove(this);
        }

        // Set the new beforeProduct and add this order to its association list
        this.beforeProduct = newBeforeProduct;
        if (newBeforeProduct != null) {
            newBeforeProduct.getOrdersAsBeforeProduct().add(this);
        }
    }

    public void setAfterProduct(ProductDBO newAfterProduct) {
        // If there is an existing afterProduct, remove the association
        if (this.afterProduct != null) {
            this.afterProduct.getOrdersAsAfterProduct().remove(this);
        }

        // Set the new afterProduct and add this order to its association list
        this.afterProduct = newAfterProduct;
        if (newAfterProduct != null) {
            newAfterProduct.getOrdersAsAfterProduct().add(this);
        }
    }

    public void clearBeforeProduct() {
        if (this.beforeProduct != null) {
            this.beforeProduct.getOrdersAsBeforeProduct().remove(this);
            this.beforeProduct = null;
        }
    }

    public void clearAfterProduct() {
        if (this.afterProduct != null) {
            this.afterProduct.getOrdersAsAfterProduct().remove(this);
            this.afterProduct = null;
        }
    }

    public void addEquipment(EquipmentDBO equipment) {
        if (!this.equipment.contains(equipment)) {
            this.equipment.add(equipment);
            equipment.getOrders().add(this); // Ensure bidirectional consistency
        }
    }

    public void removeEquipment(EquipmentDBO equipment) {
        if (this.equipment.contains(equipment)) {
            this.equipment.remove(equipment);
            equipment.getOrders().remove(this); // Ensure bidirectional consistency
        }
    }

    public void clearEquipment() {
        for (EquipmentDBO equipment : new ArrayList<>(this.equipment)) {
            removeEquipment(equipment);
        }
    }

    public void addExecution(ExecutionDBO execution) {
        if (!this.executions.contains(execution)) {
            this.executions.add(execution);
            execution.setOrder(this); // Ensure bidirectional consistency
        }
    }

    public void removeExecution(ExecutionDBO execution) {
        if (this.executions.contains(execution)) {
            this.executions.remove(execution);
            execution.setOrder(null); // Remove reference but keep ExecutionDBO
        }
    }

    public void clearExecutions() {
        for (ExecutionDBO execution : new ArrayList<>(this.executions)) {
            removeExecution(execution);
        }
    }

}
