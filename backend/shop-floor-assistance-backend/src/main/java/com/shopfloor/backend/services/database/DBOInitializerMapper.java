package com.shopfloor.backend.services.database;

import com.shopfloor.backend.api.transferobjects.ItemTO;
import com.shopfloor.backend.api.transferobjects.OrderTO;
import com.shopfloor.backend.api.transferobjects.TaskTO;
import com.shopfloor.backend.api.transferobjects.WorkflowTO;
import com.shopfloor.backend.services.database.objects.ItemDBO;
import com.shopfloor.backend.services.database.objects.OrderDBO;
import com.shopfloor.backend.services.database.objects.TaskDBO;
import com.shopfloor.backend.services.database.objects.WorkflowDBO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DBOInitializerMapper {

    public OrderDBO toOrderDBO(OrderTO orderTO, Long creatorId) {
        OrderDBO orderDBO = new OrderDBO();
        orderDBO.setOrderNumber(orderTO.getOrderNumber());
        orderDBO.setName(orderTO.getName());
        orderDBO.setDescription(orderTO.getDescription());
        orderDBO.setCreatedBy(creatorId);
        orderDBO.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        List<WorkflowDBO> workflowDBOs = orderTO.getWorkflows().stream()
                .map(workflowTO -> toWorkflowDBO(workflowTO, creatorId)) // Updated to pass orderDBO
                .collect(Collectors.toList());

        orderDBO.setWorkflows(workflowDBOs);


        return orderDBO;
    }

    public WorkflowDBO toWorkflowDBO(WorkflowTO workflowTO, Long creatorId) {
        WorkflowDBO workflowDBO = new WorkflowDBO();
        workflowDBO.setName(workflowTO.getName());
        workflowDBO.setDescription(workflowTO.getDescription());
        workflowDBO.setCreatedBy(creatorId);
        workflowDBO.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        List<TaskDBO> taskDBOs = workflowTO.getTasks().stream()
                .map(taskTO -> toTaskDBO(taskTO, creatorId))
                .collect(Collectors.toList());

        workflowDBO.setTasks(taskDBOs);

        return workflowDBO;
    }

    public TaskDBO toTaskDBO(TaskTO taskTO, Long creatorId) {
        TaskDBO taskDBO = new TaskDBO();
        taskDBO.setName(taskTO.getName());
        taskDBO.setDescription(taskTO.getDescription());
        taskDBO.setCreatedBy(creatorId);
        taskDBO.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));


        List<ItemDBO> itemDBOs = taskTO.getItems().stream()
                .map(itemTO -> toItemDBO(itemTO, creatorId))
                .collect(Collectors.toList());

        taskDBO.setItems(itemDBOs);

        return taskDBO;
    }

    public ItemDBO toItemDBO(ItemTO itemTO, Long creatorId) {
        ItemDBO itemDBO = new ItemDBO();
        itemDBO.setName(itemTO.getName());
        itemDBO.setDescription(itemTO.getDescription());
        itemDBO.setTimeRequired(itemTO.getTimeRequired());
        itemDBO.setCreatedBy(creatorId);
        itemDBO.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return itemDBO;
    }
}
