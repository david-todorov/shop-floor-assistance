package com.shopfloor.backend.api.transferobjects.mappers;

import com.shopfloor.backend.api.transferobjects.editors.*;
import com.shopfloor.backend.database.objects.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for mapping a DBO object to TO object
 * If representation of any TO object has to change this is th place
 */
@Component
public class EditorTOMapper {

    public ArrayList<EditorEquipmentTO> toEquipmentTOs(List<EquipmentDBO> equipments) {
        ArrayList<EditorEquipmentTO> equipmentTOs = new ArrayList<>();
        equipments.forEach(equipment -> {
            equipmentTOs.add(toEquipmentTO(equipment));
        });

        return equipmentTOs;
    }

    public EditorEquipmentTO toEquipmentTO(EquipmentDBO equipmentDBO) {
        EditorEquipmentTO editorEquipmentTO = this.mapBasicEquipmentProperties(equipmentDBO);

        List<EditorOrderTO> editorOrderTOList = new ArrayList<>();

        equipmentDBO.getOrders().forEach(order -> {
            editorOrderTOList.add(mapBasicOrderProperties(order));
        });
        editorEquipmentTO.setOrders(editorOrderTOList);

        return editorEquipmentTO;
    }

    public EditorProductTO toProductTO(ProductDBO productDBO) {

        EditorProductTO editorProductTO = mapBasicProductProperties(productDBO);

        List<EditorOrderTO> editorOrderBefore = new ArrayList<>();
        productDBO.getOrdersAsBeforeProduct().forEach(order -> {
            editorOrderBefore.add(mapBasicOrderProperties(order));
        });
        editorProductTO.setOrdersBefore(editorOrderBefore);

        List<EditorOrderTO> editorOrderAfter = new ArrayList<>();
        productDBO.getOrdersAsAfterProduct().forEach(order -> {
            editorOrderAfter.add(mapBasicOrderProperties(order));
        });
        editorProductTO.setOrdersAfter(editorOrderAfter);


        return editorProductTO;
    }

    public ArrayList<EditorProductTO> toProductTOs(List<ProductDBO> productDBOS) {
        ArrayList<EditorProductTO> editorProductTOs = new ArrayList<>();
        productDBOS.forEach(productDBO -> editorProductTOs.add(toProductTO(productDBO)));
        return editorProductTOs;
    }

    public EditorOrderTO toOrderTO(OrderDBO orderDBO) {
        EditorOrderTO editorOrderTO = mapBasicOrderProperties(orderDBO);

        if (orderDBO.getBeforeProduct() != null) {
            editorOrderTO.setProductBefore(mapBasicProductProperties(orderDBO.getBeforeProduct()));
        }
        if (orderDBO.getAfterProduct() != null) {
            editorOrderTO.setProductAfter(mapBasicProductProperties(orderDBO.getAfterProduct()));
        }
        if (editorOrderTO.getEquipment() != null) {
            editorOrderTO.setEquipment(toEquipmentTOs(orderDBO.getEquipment()));
        }

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

    private EditorOrderTO mapBasicOrderProperties(OrderDBO orderDBO) {
        EditorOrderTO editorOrderTO = new EditorOrderTO();
        editorOrderTO.setId(orderDBO.getId());
        editorOrderTO.setOrderNumber(orderDBO.getOrderNumber());
        editorOrderTO.setName(orderDBO.getName());
        editorOrderTO.setDescription(orderDBO.getDescription());
        editorOrderTO.setCreatedBy(orderDBO.getCreatedBy());
        editorOrderTO.setUpdatedBy(orderDBO.getUpdatedBy());
        editorOrderTO.setCreatedAt(orderDBO.getCreatedAt());
        editorOrderTO.setUpdatedAt(orderDBO.getUpdatedAt());

        return editorOrderTO;
    }

    private EditorProductTO mapBasicProductProperties(ProductDBO productDBO) {
        EditorProductTO editorProductTO = new EditorProductTO();
        editorProductTO.setId(productDBO.getId());
        editorProductTO.setProductNumber(productDBO.getProductNumber());
        editorProductTO.setName(productDBO.getName());
        editorProductTO.setType(productDBO.getType());
        editorProductTO.setCountry(productDBO.getCountry());
        editorProductTO.setPackageSize(productDBO.getPackageSize());
        editorProductTO.setPackageType(productDBO.getPackageType());
        editorProductTO.setLanguage(productDBO.getLanguage());
        editorProductTO.setDescription(productDBO.getDescription());

        editorProductTO.setCreatedBy(productDBO.getCreatedBy());
        editorProductTO.setUpdatedBy(productDBO.getUpdatedBy());
        editorProductTO.setCreatedAt(productDBO.getCreatedAt());
        editorProductTO.setUpdatedAt(productDBO.getUpdatedAt());

        return editorProductTO;
    }

    private EditorEquipmentTO mapBasicEquipmentProperties(EquipmentDBO equipmentDBO) {
        EditorEquipmentTO editorEquipmentTO = new EditorEquipmentTO();
        editorEquipmentTO.setId(equipmentDBO.getId());
        editorEquipmentTO.setEquipmentNumber(equipmentDBO.getEquipmentNumber());
        editorEquipmentTO.setName(equipmentDBO.getName());
        editorEquipmentTO.setType(equipmentDBO.getType());
        editorEquipmentTO.setDescription(equipmentDBO.getDescription());

        editorEquipmentTO.setCreatedBy(equipmentDBO.getCreatedBy());
        editorEquipmentTO.setUpdatedBy(equipmentDBO.getUpdatedBy());
        editorEquipmentTO.setCreatedAt(equipmentDBO.getCreatedAt());
        editorEquipmentTO.setUpdatedAt(equipmentDBO.getUpdatedAt());

        return editorEquipmentTO;
    }
}
