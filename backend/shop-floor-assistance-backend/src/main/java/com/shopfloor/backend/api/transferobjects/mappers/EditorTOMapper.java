package com.shopfloor.backend.api.transferobjects.mappers;

import com.shopfloor.backend.api.transferobjects.editors.EditorItemTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorOrderTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorTaskTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorWorkflowTO;
import com.shopfloor.backend.database.objects.ItemDBO;
import com.shopfloor.backend.database.objects.OrderDBO;
import com.shopfloor.backend.database.objects.TaskDBO;
import com.shopfloor.backend.database.objects.WorkflowDBO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for mapping a DBO object to TO object
 * If representation of any TO object has to change this is th place
 */
@Component
public class EditorTOMapper {

    public EditorOrderTO toOrderTO(OrderDBO orderDBO) {
        EditorOrderTO editorOrderTO = new EditorOrderTO();

        editorOrderTO.setId(orderDBO.getId());
        editorOrderTO.setOrderNumber(orderDBO.getOrderNumber());
        editorOrderTO.setName(orderDBO.getName());
        editorOrderTO.setDescription(orderDBO.getDescription());
        editorOrderTO.setCreatedBy(orderDBO.getCreatedBy());
        editorOrderTO.setUpdatedBy(orderDBO.getUpdatedBy());
        editorOrderTO.setCreatedAt(orderDBO.getCreatedAt());
        editorOrderTO.setUpdatedAt(orderDBO.getUpdatedAt());


        editorOrderTO.setWorkflows(toWorkflowTOs(orderDBO.getWorkflows()));

        return editorOrderTO;
    }

    public EditorWorkflowTO toWorkflowTO(WorkflowDBO workflowDBO) {
        EditorWorkflowTO editorWorkflowTO = new EditorWorkflowTO();

        editorWorkflowTO.setId(workflowDBO.getId());
        editorWorkflowTO.setName(workflowDBO.getName());
        editorWorkflowTO.setDescription(workflowDBO.getDescription());
        editorWorkflowTO.setCreatedBy(workflowDBO.getCreatedBy());
        editorWorkflowTO.setUpdatedBy(workflowDBO.getUpdatedBy());
        editorWorkflowTO.setCreatedAt(workflowDBO.getCreatedAt());
        editorWorkflowTO.setUpdatedAt(workflowDBO.getUpdatedAt());
        editorWorkflowTO.setTasks(toTaskTOs(workflowDBO.getTasks()));
        return editorWorkflowTO;
    }

    public EditorTaskTO toTaskTO(TaskDBO taskDBO) {
        EditorTaskTO editorTaskTO = new EditorTaskTO();

        editorTaskTO.setId(taskDBO.getId());
        editorTaskTO.setName(taskDBO.getName());
        editorTaskTO.setDescription(taskDBO.getDescription());
        editorTaskTO.setCreatedBy(taskDBO.getCreatedBy());
        editorTaskTO.setUpdatedBy(taskDBO.getUpdatedBy());
        editorTaskTO.setCreatedAt(taskDBO.getCreatedAt());
        editorTaskTO.setUpdatedAt(taskDBO.getUpdatedAt());
        editorTaskTO.setItems(toItemTOs(taskDBO.getItems()));

        return editorTaskTO;
    }

    public EditorItemTO toItemTO(ItemDBO itemDBO) {
        EditorItemTO editorItemTO = new EditorItemTO();

        editorItemTO.setId(itemDBO.getId());
        editorItemTO.setName(itemDBO.getName());
        editorItemTO.setDescription(itemDBO.getDescription());
        editorItemTO.setCreatedBy(itemDBO.getCreatedBy());
        editorItemTO.setUpdatedBy(itemDBO.getUpdatedBy());
        editorItemTO.setCreatedAt(itemDBO.getCreatedAt());
        editorItemTO.setUpdatedAt(itemDBO.getUpdatedAt());
        editorItemTO.setTimeRequired(itemDBO.getTimeRequired());
        return editorItemTO;
    }

    public ArrayList<EditorOrderTO> toOrderTOs(List<OrderDBO> orderDBOS) {
        ArrayList<EditorOrderTO> editorOrderTOS = new ArrayList<>();
        orderDBOS.forEach(orderDBO -> editorOrderTOS.add(toOrderTO(orderDBO)));
        return editorOrderTOS;
    }

    public ArrayList<EditorWorkflowTO> toWorkflowTOs(List<WorkflowDBO> workflowDBOs) {
        ArrayList<EditorWorkflowTO> editorWorkflowTOS = new ArrayList<>();
        workflowDBOs.forEach(workflowDBO -> editorWorkflowTOS.add(toWorkflowTO(workflowDBO)));
        return editorWorkflowTOS;
    }

    public ArrayList<EditorTaskTO> toTaskTOs(List<TaskDBO> taskDBOs) {
        ArrayList<EditorTaskTO> editorTaskTOS = new ArrayList<>();
        taskDBOs.forEach(taskDBO -> editorTaskTOS.add(toTaskTO(taskDBO)));
        return editorTaskTOS;
    }

    public ArrayList<EditorItemTO> toItemTOs(List<ItemDBO> itemDBOs) {
        ArrayList<EditorItemTO> editorItemTOS = new ArrayList<>();
        itemDBOs.forEach(itemDBO -> editorItemTOS.add(toItemTO(itemDBO)));
        return editorItemTOS;
    }
}
