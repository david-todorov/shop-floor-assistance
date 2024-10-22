package com.shopfloor.backend.database.mappers;

import com.shopfloor.backend.api.transferobjects.editors.EditorItemTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorOrderTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorTaskTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorWorkflowTO;
import com.shopfloor.backend.database.objects.ItemDBO;
import com.shopfloor.backend.database.objects.OrderDBO;
import com.shopfloor.backend.database.objects.TaskDBO;
import com.shopfloor.backend.database.objects.WorkflowDBO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    public OrderDBO toOrderDBO(EditorOrderTO editorOrderTO, Long creatorId) {
        OrderDBO orderDBO = new OrderDBO();
        orderDBO.setOrderNumber(editorOrderTO.getOrderNumber());
        orderDBO.setName(editorOrderTO.getName());
        orderDBO.setDescription(editorOrderTO.getDescription());
        orderDBO.setCreatedBy(creatorId);
        orderDBO.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        List<WorkflowDBO> workflowDBOs = editorOrderTO.getWorkflows().stream()
                .map(workflowTO -> toWorkflowDBO(workflowTO, creatorId)) // Updated to pass orderDBO
                .collect(Collectors.toList());

        orderDBO.setWorkflows(workflowDBOs);


        return orderDBO;
    }

    public WorkflowDBO toWorkflowDBO(EditorWorkflowTO editorWorkflowTO, Long creatorId) {
        WorkflowDBO workflowDBO = new WorkflowDBO();
        workflowDBO.setName(editorWorkflowTO.getName());
        workflowDBO.setDescription(editorWorkflowTO.getDescription());
        workflowDBO.setCreatedBy(creatorId);
        workflowDBO.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        List<TaskDBO> taskDBOs = editorWorkflowTO.getTasks().stream()
                .map(taskTO -> toTaskDBO(taskTO, creatorId))
                .collect(Collectors.toList());

        workflowDBO.setTasks(taskDBOs);

        return workflowDBO;
    }

    public TaskDBO toTaskDBO(EditorTaskTO editorTaskTO, Long creatorId) {
        TaskDBO taskDBO = new TaskDBO();
        taskDBO.setName(editorTaskTO.getName());
        taskDBO.setDescription(editorTaskTO.getDescription());
        taskDBO.setCreatedBy(creatorId);
        taskDBO.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));


        List<ItemDBO> itemDBOs = editorTaskTO.getItems().stream()
                .map(itemTO -> toItemDBO(itemTO, creatorId))
                .collect(Collectors.toList());

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
}
