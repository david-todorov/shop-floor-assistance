package com.shopfloor.backend.database.mappers;

import com.shopfloor.backend.api.transferobjects.editors.*;
import com.shopfloor.backend.database.objects.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class serve as initializer and mapper for new orders
 * It maps the "basic" properties on an OrderDBO from EditorOrderTO
 * Sets the creator id and timestamp for creation
 *                    IMPORTANT
 * It returns new OrderDBO rather than updating them
 * For copying DBOUpdaterMapper is used
 */
@Component
public class DBOInitializerMapper {

    public EquipmentDBO toEquipmentDBO(EditorEquipmentTO editorEquipmentTO, Long creatorId) {
        EquipmentDBO equipmentDBO = new EquipmentDBO();
        equipmentDBO.setEquipmentNumber(editorEquipmentTO.getEquipmentNumber());
        equipmentDBO.setName(editorEquipmentTO.getName());
        equipmentDBO.setDescription(editorEquipmentTO.getDescription());
        equipmentDBO.setType(editorEquipmentTO.getType());

        equipmentDBO.setCreatedBy(creatorId);
        equipmentDBO.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        return equipmentDBO;
    }

    public ProductDBO toProductDBO(EditorProductTO productTO, Long creatorId) {
        ProductDBO productDBO = new ProductDBO();


        productDBO.setProductNumber(productTO.getProductNumber());
        productDBO.setName(productTO.getName());
        productDBO.setType(productTO.getType());
        productDBO.setCountry(productTO.getCountry());
        productDBO.setPackageSize(productTO.getPackageSize());
        productDBO.setPackageType(productTO.getPackageType());
        productDBO.setLanguage(productTO.getLanguage());
        productDBO.setDescription(productTO.getDescription());

        productDBO.setCreatedBy(creatorId);
        productDBO.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        return productDBO;
    }

    public OrderDBO toOrderDBO(EditorOrderTO editorOrderTO, Long creatorId) {
        OrderDBO orderDBO = new OrderDBO();
        orderDBO.setOrderNumber(editorOrderTO.getOrderNumber());
        orderDBO.setName(editorOrderTO.getName());
        orderDBO.setDescription(editorOrderTO.getDescription());
        orderDBO.setCreatedBy(creatorId);
        orderDBO.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        List<WorkflowDBO> workflowDBOs = new ArrayList<>();

        // The 'orderingWorkflowIndex' is used to define the order of the workflows within order
        Integer orderingWorkflowIndex = 0;
        for (EditorWorkflowTO workflowTO : editorOrderTO.getWorkflows()) {
            WorkflowDBO workflowDBO = toWorkflowDBO(workflowTO, creatorId);

            // Set the ordering index for each workflow
            workflowDBO.setOrderingIndex(orderingWorkflowIndex++);
            workflowDBOs.add(workflowDBO);
        }

        orderDBO.setWorkflows(workflowDBOs);

        return orderDBO;
    }

    public WorkflowDBO toWorkflowDBO(EditorWorkflowTO editorWorkflowTO, Long creatorId) {
        WorkflowDBO workflowDBO = new WorkflowDBO();
        workflowDBO.setName(editorWorkflowTO.getName());
        workflowDBO.setDescription(editorWorkflowTO.getDescription());
        workflowDBO.setCreatedBy(creatorId);
        workflowDBO.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        List<TaskDBO> taskDBOs = new ArrayList<>();

        // The 'orderingTaskIndex' is used to define the order of the tasks within workflow
        Integer orderingTaskIndex = 0;
        for (EditorTaskTO taskTO : editorWorkflowTO.getTasks()) {
            TaskDBO taskDBO = toTaskDBO(taskTO, creatorId);

            // Set the ordering index for each task
            taskDBO.setOrderingIndex(orderingTaskIndex++);
            taskDBOs.add(taskDBO);
        }

        workflowDBO.setTasks(taskDBOs);

        return workflowDBO;
    }

    public TaskDBO toTaskDBO(EditorTaskTO editorTaskTO, Long creatorId) {
        TaskDBO taskDBO = new TaskDBO();
        taskDBO.setName(editorTaskTO.getName());
        taskDBO.setDescription(editorTaskTO.getDescription());
        taskDBO.setCreatedBy(creatorId);
        taskDBO.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        List<ItemDBO> itemDBOs = new ArrayList<>();

        // The 'orderingItemIndex' is used to define the order of the items within task
        Integer orderingItemIndex = 0;
        for (EditorItemTO itemTO : editorTaskTO.getItems()) {
            ItemDBO itemDBO = toItemDBO(itemTO, creatorId);

            // Set the ordering index for each item
            itemDBO.setOrderingIndex(orderingItemIndex++);
            itemDBOs.add(itemDBO);
        }

        taskDBO.setItems(itemDBOs);

        return taskDBO;
    }

    public ItemDBO toItemDBO(EditorItemTO editorItemTO, Long creatorId) {
        ItemDBO itemDBO = new ItemDBO();
        itemDBO.setName(editorItemTO.getName());
        itemDBO.setDescription(editorItemTO.getDescription());
        itemDBO.setTimeRequired(editorItemTO.getTimeRequired());
        itemDBO.setCreatedBy(creatorId);
        itemDBO.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return itemDBO;
    }

    public ExecutionDBO toExecutionDBO(Long executorId) {

        ExecutionDBO executionDBO = new ExecutionDBO();
        executionDBO.setStartedBy(executorId);
        executionDBO.setStartedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        return executionDBO;
    }
}
