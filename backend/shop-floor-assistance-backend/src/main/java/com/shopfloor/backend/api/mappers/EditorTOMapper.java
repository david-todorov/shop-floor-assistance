package com.shopfloor.backend.api.mappers;

import com.shopfloor.backend.api.transferobjects.editors.*;
import com.shopfloor.backend.database.objects.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class for converting between various DBO and TO objects related to editors.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Component
public class EditorTOMapper {

    /**
     * Converts a list of EquipmentDBO objects to a list of EditorEquipmentTO objects.
     * @param equipments the list of EquipmentDBO objects to convert
     * @return the list of converted EditorEquipmentTO objects
     */
    public ArrayList<EditorEquipmentTO> toEquipmentTOs(List<EquipmentDBO> equipments) {
        ArrayList<EditorEquipmentTO> equipmentTOs = new ArrayList<>();
        equipments.forEach(equipment -> {
            equipmentTOs.add(toEquipmentTO(equipment));
        });

        return equipmentTOs;
    }

    /**
     * Converts an EquipmentDBO to an EditorEquipmentTO.
     * @param equipmentDBO the EquipmentDBO to convert
     * @return the converted EditorEquipmentTO
     */
    public EditorEquipmentTO toEquipmentTO(EquipmentDBO equipmentDBO) {
        EditorEquipmentTO editorEquipmentTO = this.mapBasicEquipmentProperties(equipmentDBO);

        List<EditorOrderTO> editorOrderTOList = new ArrayList<>();

        equipmentDBO.getOrders().forEach(order -> {
            editorOrderTOList.add(mapBasicOrderProperties(order));
        });
        editorEquipmentTO.setOrders(editorOrderTOList);

        return editorEquipmentTO;
    }

    /**
     * Converts a ProductDBO to an EditorProductTO.
     * @param productDBO the ProductDBO to convert
     * @return the converted EditorProductTO
     */
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

    /**
     * Converts a list of ProductDBO objects to a list of EditorProductTO objects.
     * @param productDBOS the list of ProductDBO objects to convert
     * @return the list of converted EditorProductTO objects
     */
    public ArrayList<EditorProductTO> toProductTOs(List<ProductDBO> productDBOS) {
        ArrayList<EditorProductTO> editorProductTOs = new ArrayList<>();
        productDBOS.forEach(productDBO -> editorProductTOs.add(toProductTO(productDBO)));
        return editorProductTOs;
    }

    /**
     * Converts an OrderDBO to an EditorOrderTO.
     * @param orderDBO the OrderDBO to convert
     * @return the converted EditorOrderTO
     */
    public EditorOrderTO toOrderTO(OrderDBO orderDBO) {
        EditorOrderTO editorOrderTO = mapBasicOrderProperties(orderDBO);

        if (orderDBO.getBeforeProduct() != null) {
            editorOrderTO.setProductBefore(mapBasicProductProperties(orderDBO.getBeforeProduct()));
        }
        if (orderDBO.getAfterProduct() != null) {
            editorOrderTO.setProductAfter(mapBasicProductProperties(orderDBO.getAfterProduct()));
        }

        editorOrderTO.setEquipment(toEquipmentTOs(orderDBO.getEquipment()));

        editorOrderTO.setWorkflows(toWorkflowTOs(orderDBO.getWorkflows()));

        editorOrderTO.setForecast(toForecastTO(orderDBO));

        return editorOrderTO;
    }

    /**
     * Converts a WorkflowDBO to an EditorWorkflowTO.
     * @param workflowDBO the WorkflowDBO to convert
     * @return the converted EditorWorkflowTO
     */
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

    /**
     * Converts a TaskDBO to an EditorTaskTO.
     * @param taskDBO the TaskDBO to convert
     * @return the converted EditorTaskTO
     */
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

    /**
     * Converts an ItemDBO to an EditorItemTO.
     * @param itemDBO the ItemDBO to convert
     * @return the converted EditorItemTO
     */
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

    /**
     * Converts an OrderDBO to an EditorForecastTO.
     * @param orderDBO the OrderDBO to convert
     * @return the converted EditorForecastTO
     */
    public EditorForecastTO toForecastTO(OrderDBO orderDBO) {
        EditorForecastTO editorForecastTO = new EditorForecastTO();
        editorForecastTO.setTotalTimeRequired(orderDBO.getTotalTimeRequired());

        return editorForecastTO;
    }

    /**
     * Converts a list of OrderDBO objects to a list of EditorOrderTO objects.
     * @param orderDBOS the list of OrderDBO objects to convert
     * @return the list of converted EditorOrderTO objects
     */
    public ArrayList<EditorOrderTO> toOrderTOs(List<OrderDBO> orderDBOS) {
        ArrayList<EditorOrderTO> editorOrderTOS = new ArrayList<>();
        orderDBOS.forEach(orderDBO -> editorOrderTOS.add(toOrderTO(orderDBO)));
        return editorOrderTOS;
    }

    /**
     * Converts a list of WorkflowDBO objects to a list of EditorWorkflowTO objects.
     * @param workflowDBOs the list of WorkflowDBO objects to convert
     * @return the list of converted EditorWorkflowTO objects
     */
    public ArrayList<EditorWorkflowTO> toWorkflowTOs(List<WorkflowDBO> workflowDBOs) {
        ArrayList<EditorWorkflowTO> editorWorkflowTOS = new ArrayList<>();
        workflowDBOs.forEach(workflowDBO -> editorWorkflowTOS.add(toWorkflowTO(workflowDBO)));
        return editorWorkflowTOS;
    }

    /**
     * Converts a list of TaskDBO objects to a list of EditorTaskTO objects.
     * @param taskDBOs the list of TaskDBO objects to convert
     * @return the list of converted EditorTaskTO objects
     */
    public ArrayList<EditorTaskTO> toTaskTOs(List<TaskDBO> taskDBOs) {
        ArrayList<EditorTaskTO> editorTaskTOS = new ArrayList<>();
        taskDBOs.forEach(taskDBO -> editorTaskTOS.add(toTaskTO(taskDBO)));
        return editorTaskTOS;
    }

    /**
     * Converts a list of ItemDBO objects to a list of EditorItemTO objects.
     * @param itemDBOs the list of ItemDBO objects to convert
     * @return the list of converted EditorItemTO objects
     */
    public ArrayList<EditorItemTO> toItemTOs(List<ItemDBO> itemDBOs) {
        ArrayList<EditorItemTO> editorItemTOS = new ArrayList<>();
        itemDBOs.forEach(itemDBO -> editorItemTOS.add(toItemTO(itemDBO)));
        return editorItemTOS;
    }

    /**
     * Maps basic properties from an OrderDBO to an EditorOrderTO.
     * @param orderDBO the OrderDBO to map
     * @return the mapped EditorOrderTO
     */
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
        editorOrderTO.getForecast().setTotalTimeRequired(orderDBO.getTotalTimeRequired());

        return editorOrderTO;
    }

    /**
     * Maps basic properties from a ProductDBO to an EditorProductTO.
     * @param productDBO the ProductDBO to map
     * @return the mapped EditorProductTO
     */
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

    /**
     * Maps basic properties from an EquipmentDBO to an EditorEquipmentTO.
     * @param equipmentDBO the EquipmentDBO to map
     * @return the mapped EditorEquipmentTO
     */
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
