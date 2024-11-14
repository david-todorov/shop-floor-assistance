package com.shopfloor.backend.database.objects;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

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
    @OrderBy("orderingIndex ASC")
    private List<WorkflowDBO> workflows;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(name = "order_equipment", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "equipment_id"))
    private List<EquipmentDBO> equipment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "before_product_id")
    private ProductDBO beforeProduct;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "after_product_id")
    private ProductDBO afterProduct;

    @Column(name = "total_time_required")
    private Integer totalTimeRequired;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY, mappedBy = "order")
    private List<ExecutionDBO> executions;

    public OrderDBO() {
        this.equipment = new ArrayList<EquipmentDBO>();
        this.workflows = new ArrayList<WorkflowDBO>();
        this.executions = new ArrayList<ExecutionDBO>();
    }

    public void setBeforeProduct(ProductDBO newBeforeProduct) {
        // If there is an existing beforeProduct, remove the association
        this.clearBeforeProduct();

        // Set the new beforeProduct and add this order to its association list
        this.beforeProduct = newBeforeProduct;
        if (newBeforeProduct != null) {
            newBeforeProduct.getOrdersAsBeforeProduct().add(this);
        }
    }

    public void setAfterProduct(ProductDBO newAfterProduct) {
        // If there is an existing afterProduct, remove the association
        this.clearAfterProduct();

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

    public void clearEquipmentList() {
        for (EquipmentDBO equipment : new ArrayList<>(this.equipment)) {
            removeEquipment(equipment);
        }
    }

    public void synchronizeEquipmentList(List<EquipmentDBO> equipments) {
        // Create a set of equipment IDs in the new list for fast lookup
        Set<Long> newEquipmentIds = equipments.stream()
                .map(EquipmentDBO::getId)
                .collect(Collectors.toSet());

        // Remove equipment that are not in the new list
        for (EquipmentDBO existingEquipment : new ArrayList<>(this.equipment)) {
            if (!newEquipmentIds.contains(existingEquipment.getId())) {
                removeEquipment(existingEquipment);
            }
        }

        // Add equipment from the new list that are not already in the current list
        for (EquipmentDBO newEquipment : equipments) {
            if (!this.equipment.contains(newEquipment)) {
                addEquipment(newEquipment);
            }
        }
    }

    public void setEquipmentList(List<EquipmentDBO> equipments) {
        // Clear all existing equipment using the helper method
        clearEquipmentList();

        // Add each equipment from the new list using the addEquipment helper
        for (EquipmentDBO equipment : equipments) {
            addEquipment(equipment);
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

    public void sortEntities() {
        // Sort workflows if they are not empty
        if (!workflows.isEmpty()) {
            workflows.sort(Comparator.comparingInt(WorkflowDBO::getOrderingIndex));
            for (WorkflowDBO workflow : workflows) {
                // Sort tasks within the workflow if they are not empty
                if (!workflow.getTasks().isEmpty()) {
                    workflow.getTasks().sort(Comparator.comparingInt(TaskDBO::getOrderingIndex));
                    for (TaskDBO task : workflow.getTasks()) {
                        // Sort items within the task if they are not empty
                        if (!task.getItems().isEmpty()) {
                            task.getItems().sort(Comparator.comparingInt(ItemDBO::getOrderingIndex));
                        }
                    }
                }
            }
        }
    }
}
