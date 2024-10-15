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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class DBOMapper {

    public OrderDBO toOrderDBO(OrderTO orderTO, Long creatorId) {
        OrderDBO orderDBO = new OrderDBO();
        orderDBO.setOrderNumber(orderTO.getOrderNumber());
        orderDBO.setName(orderTO.getName());
        orderDBO.setShortDescription(orderTO.getShortDescription());
        orderDBO.setLongDescription(orderTO.getLongDescription());
        orderDBO.setCreatedBy(creatorId);



        orderDBO.setWorkflows(toWorkflowDBOs(orderTO.getWorkflows()));
        return orderDBO;
    }



    public WorkflowDBO toWorkflowDBO(WorkflowTO workflowTO) {
        WorkflowDBO workflowDBO = new WorkflowDBO();
        workflowDBO.setName(workflowTO.getName());
        workflowDBO.setDescription(workflowTO.getDescription());
        workflowDBO.setTasks(toTaskDBOs(workflowTO.getTasks()));
        return workflowDBO;
    }

    public TaskDBO toTaskDBO(TaskTO taskTO) {
        TaskDBO taskDBO = new TaskDBO();
        taskDBO.setName(taskTO.getName());
        taskDBO.setDescription(taskTO.getDescription());
        taskDBO.setItems(toItemDBOs(taskTO.getItems()));
        return taskDBO;
    }

    public ItemDBO toItemDBO(ItemTO itemTO) {
        ItemDBO itemDBO = new ItemDBO();
        itemDBO.setName(itemTO.getName());
        itemDBO.setShortDescription(itemTO.getShortDescription());
        itemDBO.setLongDescription(itemTO.getLongDescription());
        LocalDateTime now = LocalDateTime.now();
        itemDBO.setCreatedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
        itemDBO.setTimeRequired(itemTO.getTimeRequired());
        return itemDBO;
    }

    public List<OrderDBO> toOrderDBOs(List<OrderTO> orderTOs, Long creatorId) {
        List<OrderDBO> orderDBOs = new ArrayList<>();
        orderTOs.forEach(orderTO -> orderDBOs.add(toOrderDBO(orderTO, creatorId)));
        return orderDBOs;
    }

    public List<WorkflowDBO> toWorkflowDBOs(List<WorkflowTO> workflowTOs) {
        List<WorkflowDBO> workflowDBOs = new ArrayList<>();
        workflowTOs.forEach(workflowTO -> workflowDBOs.add(toWorkflowDBO(workflowTO)));
        return workflowDBOs;
    }

    public List<TaskDBO> toTaskDBOs(List<TaskTO> taskTOs) {
        List<TaskDBO> taskDBOs = new ArrayList<>();
        taskTOs.forEach(taskTO -> taskDBOs.add(toTaskDBO(taskTO)));
        return taskDBOs;
    }

    public List<ItemDBO> toItemDBOs(List<ItemTO> itemTOs) {
        List<ItemDBO> itemDBOs = new ArrayList<>();
        itemTOs.forEach(itemTO -> itemDBOs.add(toItemDBO(itemTO)));
        return itemDBOs;
    }
}
