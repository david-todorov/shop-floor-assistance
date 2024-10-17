package com.shopfloor.backend.api;

import com.shopfloor.backend.api.transferobjects.ItemTO;
import com.shopfloor.backend.api.transferobjects.OrderTO;
import com.shopfloor.backend.api.transferobjects.TaskTO;
import com.shopfloor.backend.api.transferobjects.WorkflowTO;
import com.shopfloor.backend.services.database.objects.ItemDBO;
import com.shopfloor.backend.services.database.objects.OrderDBO;
import com.shopfloor.backend.services.database.objects.TaskDBO;
import com.shopfloor.backend.services.database.objects.WorkflowDBO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TOMapper {

    public OrderTO toOrderTO(OrderDBO orderDBO) {
        OrderTO orderTO = new OrderTO();

        orderTO.setId(orderDBO.getId());
        orderTO.setOrderNumber(orderDBO.getOrderNumber());
        orderTO.setName(orderDBO.getName());
        orderTO.setDescription(orderDBO.getDescription());
        orderTO.setCreatedBy(orderDBO.getCreatedBy());
        orderTO.setUpdatedBy(orderDBO.getUpdatedBy());
        orderTO.setCreatedAt(orderDBO.getCreatedAt());
        orderTO.setUpdatedAt(orderDBO.getUpdatedAt());


        orderTO.setWorkflows(toWorkflowTOs(orderDBO.getWorkflows()));

        return orderTO;
    }

    public WorkflowTO toWorkflowTO(WorkflowDBO workflowDBO) {
        WorkflowTO workflowTO = new WorkflowTO();

        workflowTO.setId(workflowDBO.getId());
        workflowTO.setName(workflowDBO.getName());
        workflowTO.setDescription(workflowDBO.getDescription());
        workflowTO.setCreatedBy(workflowDBO.getCreatedBy());
        workflowTO.setUpdatedBy(workflowDBO.getUpdatedBy());
        workflowTO.setCreatedAt(workflowDBO.getCreatedAt());
        workflowTO.setUpdatedAt(workflowDBO.getUpdatedAt());
        workflowTO.setTasks(toTaskTOs(workflowDBO.getTasks()));
        return workflowTO;
    }

    public TaskTO toTaskTO(TaskDBO taskDBO) {
        TaskTO taskTO = new TaskTO();

        taskTO.setId(taskDBO.getId());
        taskTO.setName(taskDBO.getName());
        taskTO.setDescription(taskDBO.getDescription());
        taskTO.setCreatedBy(taskDBO.getCreatedBy());
        taskTO.setUpdatedBy(taskDBO.getUpdatedBy());
        taskTO.setCreatedAt(taskDBO.getCreatedAt());
        taskTO.setUpdatedAt(taskDBO.getUpdatedAt());
        taskTO.setItems(toItemTOs(taskDBO.getItems()));

        return taskTO;
    }

    public ItemTO toItemTO(ItemDBO itemDBO) {
        ItemTO itemTO = new ItemTO();

        itemTO.setId(itemDBO.getId());
        itemTO.setName(itemDBO.getName());
        itemTO.setDescription(itemDBO.getDescription());
        itemTO.setCreatedBy(itemDBO.getCreatedBy());
        itemTO.setUpdatedBy(itemDBO.getUpdatedBy());
        itemTO.setCreatedAt(itemDBO.getCreatedAt());
        itemTO.setUpdatedAt(itemDBO.getUpdatedAt());
        itemTO.setTimeRequired(itemDBO.getTimeRequired());
        return itemTO;
    }

    /**
     * WITH IDS
     */

    public List<OrderTO> toOrderTOs(List<OrderDBO> orderDBOS) {
        List<OrderTO> orderTOs = new ArrayList<>();
        orderDBOS.forEach(orderDBO -> orderTOs.add(toOrderTO(orderDBO)));
        return orderTOs;
    }

    public List<WorkflowTO> toWorkflowTOs(List<WorkflowDBO> workflowDBOs) {
        List<WorkflowTO> workflowTOs = new ArrayList<>();
        workflowDBOs.forEach(workflowDBO -> workflowTOs.add(toWorkflowTO(workflowDBO)));
        return workflowTOs;
    }

    public List<TaskTO> toTaskTOs(List<TaskDBO> taskDBOs) {
        List<TaskTO> taskTOs = new ArrayList<>();
        taskDBOs.forEach(taskDBO -> taskTOs.add(toTaskTO(taskDBO)));
        return taskTOs;
    }

    public List<ItemTO> toItemTOs(List<ItemDBO> itemDBOs) {
        List<ItemTO> itemTOs = new ArrayList<>();
        itemDBOs.forEach(itemDBO -> itemTOs.add(toItemTO(itemDBO)));
        return itemTOs;
    }
}
