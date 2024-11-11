package com.shopfloor.backend.api.transferobjects.mappers;

import com.shopfloor.backend.api.transferobjects.operators.*;
import com.shopfloor.backend.database.objects.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OperatorTOMapper {

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

    public OperatorOrderTO toOrderTO(OrderDBO orderDBO) {
        //The basic properties
        OperatorOrderTO operatorOrderTO = mapBasicOrderProperties(orderDBO);

        //Product Before
        if (orderDBO.getBeforeProduct() != null) {
            OperatorProductTO operatorProductBeforeTO = toProductTO(orderDBO.getBeforeProduct());
            operatorOrderTO.setProductBefore(operatorProductBeforeTO);
        }

        //Product After
        if (orderDBO.getAfterProduct() != null) {
            OperatorProductTO operatorProductAfterTO = toProductTO(orderDBO.getAfterProduct());
            operatorOrderTO.setProductAfter(operatorProductAfterTO);
        }

        // Equipment
        operatorOrderTO.setEquipment(toEquipmentTOs(orderDBO.getEquipment()));

        //Workflows, tasks and items
        operatorOrderTO.setWorkflows(toWorkflowTOs(orderDBO.getWorkflows()));

        return operatorOrderTO;
    }

    public ArrayList<OperatorOrderTO> toOrderTOs(List<OrderDBO> orderDBOS) {
        ArrayList<OperatorOrderTO> operatorOrderTOS = new ArrayList<>();
        orderDBOS.forEach(orderDBO -> operatorOrderTOS.add(toOrderTO(orderDBO)));
        return operatorOrderTOS;
    }

    private OperatorOrderTO mapBasicOrderProperties(OrderDBO orderDBO) {
        OperatorOrderTO operatorOrderTO = new OperatorOrderTO();
        operatorOrderTO.setId(orderDBO.getId());
        operatorOrderTO.setOrderNumber(orderDBO.getOrderNumber());
        operatorOrderTO.setName(orderDBO.getName());
        operatorOrderTO.setDescription(orderDBO.getDescription());
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

    public ArrayList<OperatorWorkflowTO> toWorkflowTOs(List<WorkflowDBO> workflowDBOs) {
        ArrayList<OperatorWorkflowTO> operatorWorkflowTOS = new ArrayList<>();
        workflowDBOs.forEach(workflowDBO -> operatorWorkflowTOS.add(toWorkflowTO(workflowDBO)));
        return operatorWorkflowTOS;
    }

    public OperatorTaskTO toTaskTO(TaskDBO taskDBO) {
        OperatorTaskTO operatorTaskTO = new OperatorTaskTO();
        operatorTaskTO.setId(taskDBO.getId());
        operatorTaskTO.setName(taskDBO.getName());
        operatorTaskTO.setDescription(taskDBO.getDescription());
        operatorTaskTO.setItems(toItemTOs(taskDBO.getItems()));
        return operatorTaskTO;
    }

    public ArrayList<OperatorTaskTO> toTaskTOs(List<TaskDBO> taskDBOs) {
        ArrayList<OperatorTaskTO> operatorTaskTOS = new ArrayList<>();
        taskDBOs.forEach(taskDBO -> operatorTaskTOS.add(toTaskTO(taskDBO)));
        return operatorTaskTOS;
    }

    public OperatorItemTO toItemTO(ItemDBO itemDBO) {
        OperatorItemTO operatorItemTO = new OperatorItemTO();
        operatorItemTO.setId(itemDBO.getId());
        operatorItemTO.setName(itemDBO.getName());
        operatorItemTO.setDescription(itemDBO.getDescription());
        operatorItemTO.setTimeRequired(itemDBO.getTimeRequired());
        return operatorItemTO;
    }

    public ArrayList<OperatorItemTO> toItemTOs(List<ItemDBO> itemDBOs) {
        ArrayList<OperatorItemTO> operatorItemTOS = new ArrayList<>();
        itemDBOs.forEach(itemDBO -> operatorItemTOS.add(toItemTO(itemDBO)));
        return operatorItemTOS;
    }

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

    public ArrayList<OperatorProductTO> toProductTOs(List<ProductDBO> productDBOS) {
        ArrayList<OperatorProductTO> operatorProductTOs = new ArrayList<>();
        productDBOS.forEach(productDBO -> operatorProductTOs.add(toProductTO(productDBO)));
        return operatorProductTOs;
    }

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

    public ArrayList<OperatorEquipmentTO> toEquipmentTOs(List<EquipmentDBO> equipments) {
        ArrayList<OperatorEquipmentTO> equipmentTOs = new ArrayList<>();
        equipments.forEach(equipment -> equipmentTOs.add(toEquipmentTO(equipment)));
        return equipmentTOs;
    }

    private OperatorEquipmentTO toEquipmentTO(EquipmentDBO equipmentDBO) {
        OperatorEquipmentTO operatorEquipmentTO = this.mapBasicEquipmentProperties(equipmentDBO);

        List<OperatorOrderTO> operatorOrderTOList = new ArrayList<>();
        equipmentDBO.getOrders().forEach(order -> operatorOrderTOList.add(mapBasicOrderProperties(order)));
        operatorEquipmentTO.setOrders(operatorOrderTOList);
        return operatorEquipmentTO;
    }

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
