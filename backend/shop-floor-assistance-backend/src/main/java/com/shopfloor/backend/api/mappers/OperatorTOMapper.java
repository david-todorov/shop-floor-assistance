package com.shopfloor.backend.api.mappers;

import com.shopfloor.backend.api.transferobjects.operators.*;
import com.shopfloor.backend.database.objects.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class for converting between various DBO and TO objects related to operators.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Component
public class OperatorTOMapper {

    /**
     * Converts an ExecutionDBO to an OperatorExecutionTO.
     * @param executionDBO the ExecutionDBO to convert
     * @return the converted OperatorExecutionTO
     */
    public OperatorExecutionTO toExecutionTO(ExecutionDBO executionDBO) {
        OperatorExecutionTO executionTO = new OperatorExecutionTO();
        executionTO.setId(executionDBO.getId());
        executionTO.setStartedBy(executionDBO.getStartedBy());
        executionTO.setFinishedBy(executionDBO.getFinishedBy());
        executionTO.setStartedAt(executionDBO.getStartedAt());
        executionTO.setFinishedAt(executionDBO.getFinishedAt());
        executionTO.setAborted(executionDBO.getAborted());

        return executionTO;
    }

    /**
     * Converts an OrderDBO to an OperatorOrderTO.
     * @param orderDBO the OrderDBO to convert
     * @return the converted OperatorOrderTO
     */
    public OperatorOrderTO toOrderTO(OrderDBO orderDBO) {
        // The basic properties
        OperatorOrderTO operatorOrderTO = mapBasicOrderProperties(orderDBO);

        // Product Before
        if (orderDBO.getBeforeProduct() != null) {
            OperatorProductTO operatorProductBeforeTO = toProductTO(orderDBO.getBeforeProduct());
            operatorOrderTO.setProductBefore(operatorProductBeforeTO);
        }

        // Product After
        if (orderDBO.getAfterProduct() != null) {
            OperatorProductTO operatorProductAfterTO = toProductTO(orderDBO.getAfterProduct());
            operatorOrderTO.setProductAfter(operatorProductAfterTO);
        }

        // Equipment
        operatorOrderTO.setEquipment(toEquipmentTOs(orderDBO.getEquipment()));

        // Workflows, tasks and items
        operatorOrderTO.setWorkflows(toWorkflowTOs(orderDBO.getWorkflows()));

        // Forecast
        operatorOrderTO.setForecast(toForecastTO(orderDBO));

        return operatorOrderTO;
    }

    /**
     * Converts a list of OrderDBO objects to a list of OperatorOrderTO objects.
     * @param orderDBOS the list of OrderDBO objects to convert
     * @return the list of converted OperatorOrderTO objects
     */
    public ArrayList<OperatorOrderTO> toOrderTOs(List<OrderDBO> orderDBOS) {
        ArrayList<OperatorOrderTO> operatorOrderTOS = new ArrayList<>();
        orderDBOS.forEach(orderDBO -> operatorOrderTOS.add(toOrderTO(orderDBO)));
        return operatorOrderTOS;
    }

    /**
     * Maps basic properties from an OrderDBO to an OperatorOrderTO.
     * @param orderDBO the OrderDBO to map
     * @return the mapped OperatorOrderTO
     */
    private OperatorOrderTO mapBasicOrderProperties(OrderDBO orderDBO) {
        OperatorOrderTO operatorOrderTO = new OperatorOrderTO();
        operatorOrderTO.setId(orderDBO.getId());
        operatorOrderTO.setOrderNumber(orderDBO.getOrderNumber());
        operatorOrderTO.setName(orderDBO.getName());
        operatorOrderTO.setDescription(orderDBO.getDescription());
        return operatorOrderTO;
    }

    /**
     * Converts a WorkflowDBO to an OperatorWorkflowTO.
     * @param workflowDBO the WorkflowDBO to convert
     * @return the converted OperatorWorkflowTO
     */
    public OperatorWorkflowTO toWorkflowTO(WorkflowDBO workflowDBO) {
        OperatorWorkflowTO operatorWorkflowTO = new OperatorWorkflowTO();
        operatorWorkflowTO.setId(workflowDBO.getId());
        operatorWorkflowTO.setName(workflowDBO.getName());
        operatorWorkflowTO.setDescription(workflowDBO.getDescription());
        operatorWorkflowTO.setTasks(toTaskTOs(workflowDBO.getTasks()));
        return operatorWorkflowTO;
    }

    /**
     * Converts a list of WorkflowDBO objects to a list of OperatorWorkflowTO objects.
     * @param workflowDBOs the list of WorkflowDBO objects to convert
     * @return the list of converted OperatorWorkflowTO objects
     */
    public ArrayList<OperatorWorkflowTO> toWorkflowTOs(List<WorkflowDBO> workflowDBOs) {
        ArrayList<OperatorWorkflowTO> operatorWorkflowTOS = new ArrayList<>();
        workflowDBOs.forEach(workflowDBO -> operatorWorkflowTOS.add(toWorkflowTO(workflowDBO)));
        return operatorWorkflowTOS;
    }

    /**
     * Converts a TaskDBO to an OperatorTaskTO.
     * @param taskDBO the TaskDBO to convert
     * @return the converted OperatorTaskTO
     */
    public OperatorTaskTO toTaskTO(TaskDBO taskDBO) {
        OperatorTaskTO operatorTaskTO = new OperatorTaskTO();
        operatorTaskTO.setId(taskDBO.getId());
        operatorTaskTO.setName(taskDBO.getName());
        operatorTaskTO.setDescription(taskDBO.getDescription());
        operatorTaskTO.setItems(toItemTOs(taskDBO.getItems()));
        return operatorTaskTO;
    }

    /**
     * Converts a list of TaskDBO objects to a list of OperatorTaskTO objects.
     * @param taskDBOs the list of TaskDBO objects to convert
     * @return the list of converted OperatorTaskTO objects
     */
    public ArrayList<OperatorTaskTO> toTaskTOs(List<TaskDBO> taskDBOs) {
        ArrayList<OperatorTaskTO> operatorTaskTOS = new ArrayList<>();
        taskDBOs.forEach(taskDBO -> operatorTaskTOS.add(toTaskTO(taskDBO)));
        return operatorTaskTOS;
    }

    /**
     * Converts an ItemDBO to an OperatorItemTO.
     * @param itemDBO the ItemDBO to convert
     * @return the converted OperatorItemTO
     */
    public OperatorItemTO toItemTO(ItemDBO itemDBO) {
        OperatorItemTO operatorItemTO = new OperatorItemTO();
        operatorItemTO.setId(itemDBO.getId());
        operatorItemTO.setName(itemDBO.getName());
        operatorItemTO.setDescription(itemDBO.getDescription());
        operatorItemTO.setTimeRequired(itemDBO.getTimeRequired());
        return operatorItemTO;
    }

    /**
     * Converts a list of ItemDBO objects to a list of OperatorItemTO objects.
     * @param itemDBOs the list of ItemDBO objects to convert
     * @return the list of converted OperatorItemTO objects
     */
    public ArrayList<OperatorItemTO> toItemTOs(List<ItemDBO> itemDBOs) {
        ArrayList<OperatorItemTO> operatorItemTOS = new ArrayList<>();
        itemDBOs.forEach(itemDBO -> operatorItemTOS.add(toItemTO(itemDBO)));
        return operatorItemTOS;
    }

    /**
     * Converts a ProductDBO to an OperatorProductTO.
     * @param productDBO the ProductDBO to convert
     * @return the converted OperatorProductTO
     */
    public OperatorProductTO toProductTO(ProductDBO productDBO) {
        OperatorProductTO operatorProductTO = mapBasicProductProperties(productDBO);
        productDBO.getOrdersAsBeforeProduct().forEach(
                (order) -> operatorProductTO.getOrdersBefore().add(mapBasicOrderProperties(order))
        );
        productDBO.getOrdersAsAfterProduct().forEach(
                (order) -> operatorProductTO.getOrdersAfter().add(mapBasicOrderProperties(order))
        );
        return operatorProductTO;
    }

    /**
     * Converts an OrderDBO to an OperatorForecastTO.
     * @param orderDBO the OrderDBO to convert
     * @return the converted OperatorForecastTO
     */
    public OperatorForecastTO toForecastTO(OrderDBO orderDBO) {
        OperatorForecastTO operatorForecastTO = new OperatorForecastTO();
        operatorForecastTO.setTotalTimeRequired(orderDBO.getTotalTimeRequired());

        return operatorForecastTO;
    }

    /**
     * Maps basic properties from a ProductDBO to an OperatorProductTO.
     * @param productDBO the ProductDBO to map
     * @return the mapped OperatorProductTO
     */
    private OperatorProductTO mapBasicProductProperties(ProductDBO productDBO) {
        OperatorProductTO operatorProductTO = new OperatorProductTO();
        operatorProductTO.setId(productDBO.getId());
        operatorProductTO.setProductNumber(productDBO.getProductNumber());
        operatorProductTO.setName(productDBO.getName());
        operatorProductTO.setType(productDBO.getType());
        operatorProductTO.setCountry(productDBO.getCountry());
        operatorProductTO.setPackageSize(productDBO.getPackageSize());
        operatorProductTO.setPackageType(productDBO.getPackageType());
        operatorProductTO.setLanguage(productDBO.getLanguage());
        operatorProductTO.setDescription(productDBO.getDescription());
        return operatorProductTO;
    }

    /**
     * Converts a list of EquipmentDBO objects to a list of OperatorEquipmentTO objects.
     * @param equipments the list of EquipmentDBO objects to convert
     * @return the list of converted OperatorEquipmentTO objects
     */
    public ArrayList<OperatorEquipmentTO> toEquipmentTOs(List<EquipmentDBO> equipments) {
        ArrayList<OperatorEquipmentTO> equipmentTOs = new ArrayList<>();
        equipments.forEach(equipment -> equipmentTOs.add(toEquipmentTO(equipment)));
        return equipmentTOs;
    }

    /**
     * Converts an EquipmentDBO to an OperatorEquipmentTO.
     * @param equipmentDBO the EquipmentDBO to convert
     * @return the converted OperatorEquipmentTO
     */
    private OperatorEquipmentTO toEquipmentTO(EquipmentDBO equipmentDBO) {
        OperatorEquipmentTO operatorEquipmentTO = this.mapBasicEquipmentProperties(equipmentDBO);

        List<OperatorOrderTO> operatorOrderTOList = new ArrayList<>();
        equipmentDBO.getOrders().forEach(order -> operatorOrderTOList.add(mapBasicOrderProperties(order)));
        operatorEquipmentTO.setOrders(operatorOrderTOList);
        return operatorEquipmentTO;
    }

    /**
     * Maps basic properties from an EquipmentDBO to an OperatorEquipmentTO.
     * @param equipmentDBO the EquipmentDBO to map
     * @return the mapped OperatorEquipmentTO
     */
    private OperatorEquipmentTO mapBasicEquipmentProperties(EquipmentDBO equipmentDBO) {
        OperatorEquipmentTO operatorEquipmentTO = new OperatorEquipmentTO();
        operatorEquipmentTO.setId(equipmentDBO.getId());
        operatorEquipmentTO.setEquipmentNumber(equipmentDBO.getEquipmentNumber());
        operatorEquipmentTO.setName(equipmentDBO.getName());
        operatorEquipmentTO.setType(equipmentDBO.getType());
        operatorEquipmentTO.setDescription(equipmentDBO.getDescription());
        return operatorEquipmentTO;
    }
}
