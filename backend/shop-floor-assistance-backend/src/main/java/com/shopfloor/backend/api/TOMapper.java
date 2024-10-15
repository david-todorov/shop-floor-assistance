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

    public List<OrderTO> toOrderTOs(List<OrderDBO> orderDBOS) {
        List<OrderTO> orderTOs = new ArrayList<>();
        orderDBOS.forEach(orderDBO -> orderTOs.add(toOrderTO(orderDBO, false)));
        return orderTOs;
    }

    public List<WorkflowTO> toWorkflowTOs(List<WorkflowDBO> workflowDBOs) {
        List<WorkflowTO> workflowTOs = new ArrayList<>();
        workflowDBOs.forEach(workflowDBO -> workflowTOs.add(toWorkflowTO(workflowDBO, false)));
        return workflowTOs;
    }

    public List<TaskTO> toTaskTOs(List<TaskDBO> taskDBOs) {
        List<TaskTO> taskTOs = new ArrayList<>();
        taskDBOs.forEach(taskDBO -> taskTOs.add(toTaskTO(taskDBO, false)));
        return taskTOs;
    }

    public List<ItemTO> toItemTOs(List<ItemDBO> itemDBOs) {
        List<ItemTO> itemTOs = new ArrayList<>();
        itemDBOs.forEach(itemDBO -> itemTOs.add(toItemTO(itemDBO, false)));
        return itemTOs;
    }

    public OrderTO toOrderTO(OrderDBO orderDBO, boolean idsIncluded) {
        OrderTO orderTO = new OrderTO();
        if (idsIncluded) {
            orderTO.setId(orderDBO.getId());
        }
        orderTO.setOrderNumber(orderDBO.getOrderNumber());
        orderTO.setName(orderDBO.getName());
        orderTO.setShortDescription(orderDBO.getShortDescription());
        orderTO.setLongDescription(orderDBO.getLongDescription());
        orderTO.setWorkflows(toWorkflowTOs(orderDBO.getWorkflows(), idsIncluded));
        return orderTO;
    }

    public WorkflowTO toWorkflowTO(WorkflowDBO workflowDBO, boolean idsIncluded) {
        WorkflowTO workflowTO = new WorkflowTO();
        if (idsIncluded) {
            workflowTO.setId(workflowDBO.getId());
        }
        workflowTO.setName(workflowDBO.getName());
        workflowTO.setDescription(workflowDBO.getDescription());
        workflowTO.setTasks(toTaskTOs(workflowDBO.getTasks(), idsIncluded));
        return workflowTO;
    }

    public TaskTO toTaskTO(TaskDBO taskDBO, boolean idsIncluded) {
        TaskTO taskTO = new TaskTO();
        if (idsIncluded) {
            taskTO.setId(taskDBO.getId());
        }
        taskTO.setName(taskDBO.getName());
        taskTO.setDescription(taskDBO.getDescription());
        taskTO.setItems(toItemTOs(taskDBO.getItems(), idsIncluded));
        return taskTO;
    }

    public ItemTO toItemTO(ItemDBO itemDBO, boolean idsIncluded) {
        ItemTO itemTO = new ItemTO();
        if (idsIncluded) {
            itemTO.setId(itemDBO.getId());
        }
        itemTO.setName(itemDBO.getName());
        itemTO.setShortDescription(itemDBO.getShortDescription());
        itemTO.setLongDescription(itemDBO.getLongDescription());
        itemTO.setCreatedAt(itemDBO.getCreatedAt());
        itemTO.setUpdatedAt(itemDBO.getUpdatedAt());
        itemTO.setTimeRequired(itemDBO.getTimeRequired());
        return itemTO;
    }

    /**
     * WITH IDS
     */

    public List<OrderTO> toOrderTOs(List<OrderDBO> orderDBOS, boolean idsIncluded) {
        List<OrderTO> orderTOs = new ArrayList<>();
        orderDBOS.forEach(orderDBO -> orderTOs.add(toOrderTO(orderDBO, idsIncluded)));
        return orderTOs;
    }

    public List<WorkflowTO> toWorkflowTOs(List<WorkflowDBO> workflowDBOs, boolean idsIncluded) {
        List<WorkflowTO> workflowTOs = new ArrayList<>();
        workflowDBOs.forEach(workflowDBO -> workflowTOs.add(toWorkflowTO(workflowDBO, idsIncluded)));
        return workflowTOs;
    }

    public List<TaskTO> toTaskTOs(List<TaskDBO> taskDBOs, boolean idsIncluded) {
        List<TaskTO> taskTOs = new ArrayList<>();
        taskDBOs.forEach(taskDBO -> taskTOs.add(toTaskTO(taskDBO, idsIncluded)));
        return taskTOs;
    }

    public List<ItemTO> toItemTOs(List<ItemDBO> itemDBOs, boolean idsIncluded) {
        List<ItemTO> itemTOs = new ArrayList<>();
        itemDBOs.forEach(itemDBO -> itemTOs.add(toItemTO(itemDBO, idsIncluded)));
        return itemTOs;
    }
}
