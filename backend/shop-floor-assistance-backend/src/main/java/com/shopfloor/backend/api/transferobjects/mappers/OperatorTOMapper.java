package com.shopfloor.backend.api.transferobjects.mappers;

import com.shopfloor.backend.api.transferobjects.operators.OperatorItemTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorOrderTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorTaskTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorWorkflowTO;
import com.shopfloor.backend.database.objects.ItemDBO;
import com.shopfloor.backend.database.objects.OrderDBO;
import com.shopfloor.backend.database.objects.TaskDBO;
import com.shopfloor.backend.database.objects.WorkflowDBO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OperatorTOMapper {

    public OperatorOrderTO toOrderTO(OrderDBO orderDBO) {
        OperatorOrderTO operatorOrderTO = new OperatorOrderTO();
        operatorOrderTO.setId(orderDBO.getId());
        operatorOrderTO.setOrderNumber(orderDBO.getOrderNumber());
        operatorOrderTO.setName(orderDBO.getName());
        operatorOrderTO.setDescription(orderDBO.getDescription());
        operatorOrderTO.setWorkflows(toWorkflowTOs(orderDBO.getWorkflows()));

        return operatorOrderTO;
    }

    public OperatorWorkflowTO toWorkflowTO(WorkflowDBO workflowDBO) {
        OperatorWorkflowTO operatorWorkflowTO = new OperatorWorkflowTO();
        operatorWorkflowTO.setId(workflowDBO.getId());
        operatorWorkflowTO.setName(workflowDBO.getName());
        operatorWorkflowTO.setDescription(workflowDBO.getDescription());
        operatorWorkflowTO.setTasks(toTaskTOs(workflowDBO.getTasks()));

        return operatorWorkflowTO;
    }

    public OperatorTaskTO toTaskTO(TaskDBO taskDBO) {
        OperatorTaskTO operatorTaskTO = new OperatorTaskTO();
        operatorTaskTO.setId(taskDBO.getId());
        operatorTaskTO.setName(taskDBO.getName());
        operatorTaskTO.setDescription(taskDBO.getDescription());
        operatorTaskTO.setItems(toItemTOs(taskDBO.getItems()));

        return operatorTaskTO;
    }

    public OperatorItemTO toItemTO(ItemDBO itemDBO) {
        OperatorItemTO operatorItemTO = new OperatorItemTO();
        operatorItemTO.setId(itemDBO.getId());
        operatorItemTO.setName(itemDBO.getName());
        operatorItemTO.setDescription(itemDBO.getDescription());
        operatorItemTO.setTimeRequired(itemDBO.getTimeRequired());

        return operatorItemTO;
    }

    public ArrayList<OperatorOrderTO> toOrderTOs(List<OrderDBO> orderDBOS) {
        ArrayList<OperatorOrderTO> operatorOrderTOS = new ArrayList<>();
        orderDBOS.forEach(orderDBO -> operatorOrderTOS.add(toOrderTO(orderDBO)));
        return operatorOrderTOS;
    }

    public ArrayList<OperatorWorkflowTO> toWorkflowTOs(List<WorkflowDBO> workflowDBOs) {
        ArrayList<OperatorWorkflowTO> operatorWorkflowTOS = new ArrayList<>();
        workflowDBOs.forEach(workflowDBO -> operatorWorkflowTOS.add(toWorkflowTO(workflowDBO)));
        return operatorWorkflowTOS;
    }

    public ArrayList<OperatorTaskTO> toTaskTOs(List<TaskDBO> taskDBOs) {
        ArrayList<OperatorTaskTO> operatorTaskTOS = new ArrayList<>();
        taskDBOs.forEach(taskDBO -> operatorTaskTOS.add(toTaskTO(taskDBO)));
        return operatorTaskTOS;
    }

    public ArrayList<OperatorItemTO> toItemTOs(List<ItemDBO> itemDBOs) {
        ArrayList<OperatorItemTO> operatorItemTOS = new ArrayList<>();
        itemDBOs.forEach(itemDBO -> operatorItemTOS.add(toItemTO(itemDBO)));
        return operatorItemTOS;
    }
}
