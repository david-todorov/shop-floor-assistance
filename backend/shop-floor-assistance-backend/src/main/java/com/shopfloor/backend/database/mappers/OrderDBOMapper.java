package com.shopfloor.backend.database.mappers;

import com.shopfloor.backend.api.transferobjects.editors.EditorItemTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorOrderTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorTaskTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorWorkflowTO;
import com.shopfloor.backend.database.objects.ItemDBO;
import com.shopfloor.backend.database.objects.OrderDBO;
import com.shopfloor.backend.database.objects.TaskDBO;
import com.shopfloor.backend.database.objects.WorkflowDBO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mapper class for converting between EditorOrderTO and OrderDBO objects.
 * Provides methods to initialize and update OrderDBO, WorkflowDBO, TaskDBO, and ItemDBO entities.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Component
public class OrderDBOMapper {

    /**
     * Initializes an OrderDBO from an EditorOrderTO and creator ID.
     * Orderings of workflows, tasks, and items are set based on their order in the transfer objects.
     * @param editorOrderTO the transfer object containing order details
     * @param creatorId the ID of the user creating the order
     * @return the initialized OrderDBO
     */
    public OrderDBO initializeOrderDBOFrom(EditorOrderTO editorOrderTO, Long creatorId) {
        OrderDBO orderDBO = new OrderDBO();
        orderDBO.setOrderNumber(editorOrderTO.getOrderNumber());
        orderDBO.setName(editorOrderTO.getName());
        orderDBO.setDescription(editorOrderTO.getDescription());
        orderDBO.setCreatedBy(creatorId);
        orderDBO.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        List<WorkflowDBO> workflowDBOs = new ArrayList<>();

        // The 'orderingWorkflowIndex' is used to define the order of the workflows within order
        Integer orderingWorkflowIndex = 0;
        for (EditorWorkflowTO workflowTO : editorOrderTO.getWorkflows()) {
            WorkflowDBO workflowDBO = this.initializeWorkflowDBOFrom(workflowTO, creatorId);

            // Set the ordering index for each workflow
            workflowDBO.setOrderingIndex(orderingWorkflowIndex++);
            workflowDBOs.add(workflowDBO);
        }

        orderDBO.setWorkflows(workflowDBOs);
        orderDBO.setTotalTimeRequired(this.calculateTotalTimeRequired(editorOrderTO));

        return orderDBO;
    }

    /**
     * Initializes a WorkflowDBO from an EditorWorkflowTO and creator ID.
     * @param editorWorkflowTO the transfer object containing workflow details
     * @param creatorId the ID of the user creating the workflow
     * @return the initialized WorkflowDBO
     */
    public WorkflowDBO initializeWorkflowDBOFrom(EditorWorkflowTO editorWorkflowTO, Long creatorId) {
        WorkflowDBO workflowDBO = new WorkflowDBO();
        workflowDBO.setName(editorWorkflowTO.getName());
        workflowDBO.setDescription(editorWorkflowTO.getDescription());
        workflowDBO.setCreatedBy(creatorId);
        workflowDBO.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        List<TaskDBO> taskDBOs = new ArrayList<>();

        // The 'orderingTaskIndex' is used to define the order of the tasks within workflow
        Integer orderingTaskIndex = 0;
        for (EditorTaskTO taskTO : editorWorkflowTO.getTasks()) {
            TaskDBO taskDBO = this.initializeTaskDBOFrom(taskTO, creatorId);

            // Set the ordering index for each task
            taskDBO.setOrderingIndex(orderingTaskIndex++);
            taskDBOs.add(taskDBO);
        }

        workflowDBO.setTasks(taskDBOs);

        return workflowDBO;
    }

    /**
     * Initializes a TaskDBO from an EditorTaskTO and creator ID.
     * @param editorTaskTO the transfer object containing task details
     * @param creatorId the ID of the user creating the task
     * @return the initialized TaskDBO
     */
    public TaskDBO initializeTaskDBOFrom(EditorTaskTO editorTaskTO, Long creatorId) {
        TaskDBO taskDBO = new TaskDBO();
        taskDBO.setName(editorTaskTO.getName());
        taskDBO.setDescription(editorTaskTO.getDescription());
        taskDBO.setCreatedBy(creatorId);
        taskDBO.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        List<ItemDBO> itemDBOs = new ArrayList<>();

        // The 'orderingItemIndex' is used to define the order of the items within task
        Integer orderingItemIndex = 0;
        for (EditorItemTO itemTO : editorTaskTO.getItems()) {
            ItemDBO itemDBO = this.initializeItemDBOFrom(itemTO, creatorId);

            // Set the ordering index for each item
            itemDBO.setOrderingIndex(orderingItemIndex++);
            itemDBOs.add(itemDBO);
        }

        taskDBO.setItems(itemDBOs);

        return taskDBO;
    }

    /**
     * Initializes an ItemDBO from an EditorItemTO and creator ID.
     * @param editorItemTO the transfer object containing item details
     * @param creatorId the ID of the user creating the item
     * @return the initialized ItemDBO
     */
    public ItemDBO initializeItemDBOFrom(EditorItemTO editorItemTO, Long creatorId) {
        ItemDBO itemDBO = new ItemDBO();
        itemDBO.setName(editorItemTO.getName());
        itemDBO.setDescription(editorItemTO.getDescription());
        itemDBO.setTimeRequired(editorItemTO.getTimeRequired());
        itemDBO.setCreatedBy(creatorId);
        itemDBO.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return itemDBO;
    }

    /**
     * Updates an existing OrderDBO from an EditorOrderTO and updater ID.
     * Orderings of workflows, tasks, and items are set based on their order in the transfer objects.
     * @param target the existing OrderDBO to update
     * @param source the transfer object containing updated order details
     * @param updaterId the ID of the user updating the order
     * @return the updated OrderDBO
     */
    public OrderDBO updateOrderDBOFrom(OrderDBO target, EditorOrderTO source, Long updaterId) {
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setOrderNumber(source.getOrderNumber());
        target.setUpdatedBy(updaterId);
        target.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        this.updateWorkflowsFrom(target.getWorkflows(), source.getWorkflows(), updaterId);
        target.setTotalTimeRequired(this.calculateTotalTimeRequired(source));

        return target;
    }

    /**
     * Updates a list of WorkflowDBOs from a list of EditorWorkflowTOs and updater ID.
     * @param targetWorkflows the list of existing WorkflowDBOs to update
     * @param sourceWorkflows the list of transfer objects containing updated workflow details
     * @param updaterId the ID of the user updating the workflows
     */
    public void updateWorkflowsFrom(List<WorkflowDBO> targetWorkflows, List<EditorWorkflowTO> sourceWorkflows, Long updaterId) {
        Map<Long, WorkflowDBO> targetMap = targetWorkflows.stream().collect(Collectors.toMap(WorkflowDBO::getId, Function.identity()));

        // The 'orderingWorkflowIndex' is used to define the order of the workflows within order
        Integer orderingWorkflowIndex = 0;
        for (EditorWorkflowTO editorWorkflowTO : sourceWorkflows) {
            WorkflowDBO workflowDBO = targetMap.get(editorWorkflowTO.getId());

            if (workflowDBO != null) {
                // Update existing workflow
                this.copyWorkflowDboFrom(workflowDBO, editorWorkflowTO, updaterId);
                targetMap.remove(editorWorkflowTO.getId());

                // Set the ordering index for the existing workflow
                workflowDBO.setOrderingIndex(orderingWorkflowIndex++);
            } else {
                // Add new workflow
                Long creatorId = updaterId;
                WorkflowDBO newWorkflowDBO = this.initializeWorkflowDBOFrom(editorWorkflowTO, creatorId);
                targetWorkflows.add(newWorkflowDBO);

                // Set the ordering index for the new workflow
                newWorkflowDBO.setOrderingIndex(orderingWorkflowIndex++);
            }
        }

        // Remove workflows no longer present in the source
        targetWorkflows.removeIf(workflow -> targetMap.containsKey(workflow.getId()));
    }

    /**
     * Updates a list of TaskDBOs from a list of EditorTaskTOs and updater ID.
     * @param targetTasks the list of existing TaskDBOs to update
     * @param sourceTasks the list of transfer objects containing updated task details
     * @param updaterId the ID of the user updating the tasks
     */
    public void updateTasksFrom(List<TaskDBO> targetTasks, List<EditorTaskTO> sourceTasks, Long updaterId) {
        Map<Long, TaskDBO> targetMap = targetTasks.stream().collect(Collectors.toMap(TaskDBO::getId, Function.identity()));

        // The 'orderingTaskIndex' is used to define the order of the tasks within workflow
        Integer orderingTaskIndex = 0;
        for (EditorTaskTO editorTaskTO : sourceTasks) {
            TaskDBO taskDBO = targetMap.get(editorTaskTO.getId());

            if (taskDBO != null) {
                // Update existing task
                this.copyTaskDboFrom(taskDBO, editorTaskTO, updaterId);
                targetMap.remove(editorTaskTO.getId());

                // Set the ordering index for the existing task
                taskDBO.setOrderingIndex(orderingTaskIndex++);
            } else {
                // Add new task
                Long creatorId = updaterId;
                TaskDBO newTaskDBO = this.initializeTaskDBOFrom(editorTaskTO, creatorId);
                targetTasks.add(newTaskDBO);

                // Set the ordering index for the new task
                newTaskDBO.setOrderingIndex(orderingTaskIndex++);
            }
        }

        // Remove tasks no longer present in the source
        targetTasks.removeIf(task -> targetMap.containsKey(task.getId()));
    }

    /**
     * Updates a list of ItemDBOs from a list of EditorItemTOs and updater ID.
     * @param targetItems the list of existing ItemDBOs to update
     * @param sourceItems the list of transfer objects containing updated item details
     * @param updaterId the ID of the user updating the items
     */
    public void updateItemsFrom(List<ItemDBO> targetItems, List<EditorItemTO> sourceItems, Long updaterId) {
        Map<Long, ItemDBO> targetMap = targetItems.stream().collect(Collectors.toMap(ItemDBO::getId, Function.identity()));

        // The 'orderingItemIndex' is used to define the order of the items within task
        Integer orderingItemIndex = 0;
        for (EditorItemTO editorItemTO : sourceItems) {
            ItemDBO itemDBO = targetMap.get(editorItemTO.getId());

            if (itemDBO != null) {
                // Update existing item
                this.copyItemDboFrom(itemDBO, editorItemTO, updaterId);
                targetMap.remove(editorItemTO.getId());

                // Set the ordering index for the existing item
                itemDBO.setOrderingIndex(orderingItemIndex++);
            } else {
                // Add new item
                Long creatorId = updaterId;
                ItemDBO newItemDBO = this.initializeItemDBOFrom(editorItemTO, creatorId);
                targetItems.add(newItemDBO);

                // Set the ordering index for the new item
                newItemDBO.setOrderingIndex(orderingItemIndex++);
            }
        }

        // Remove items no longer present in the source
        targetItems.removeIf(item -> targetMap.containsKey(item.getId()));
    }

    /**
     * Copies properties from an EditorWorkflowTO to an existing WorkflowDBO.
     * @param target the existing WorkflowDBO to update
     * @param source the transfer object containing updated workflow details
     * @param updaterId the ID of the user updating the workflow
     * @return the updated WorkflowDBO
     */
    private WorkflowDBO copyWorkflowDboFrom(WorkflowDBO target, EditorWorkflowTO source, Long updaterId) {
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setUpdatedBy(updaterId);
        target.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        this.updateTasksFrom(target.getTasks(), source.getTasks(), updaterId);
        return target;
    }

    /**
     * Copies properties from an EditorTaskTO to an existing TaskDBO.
     * @param target the existing TaskDBO to update
     * @param source the transfer object containing updated task details
     * @param updaterId the ID of the user updating the task
     * @return the updated TaskDBO
     */
    private TaskDBO copyTaskDboFrom(TaskDBO target, EditorTaskTO source, Long updaterId) {
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setUpdatedBy(updaterId);
        target.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        this.updateItemsFrom(target.getItems(), source.getItems(), updaterId);

        return target;
    }

    /**
     * Copies properties from an EditorItemTO to an existing ItemDBO.
     * @param target the existing ItemDBO to update
     * @param source the transfer object containing updated item details
     * @param updaterId the ID of the user updating the item
     * @return the updated ItemDBO
     */
    private ItemDBO copyItemDboFrom(ItemDBO target, EditorItemTO source, Long updaterId) {
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setTimeRequired(source.getTimeRequired());
        target.setUpdatedBy(updaterId);
        target.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        return target;
    }

    /**
     * Calculates the total time required for an order based on its workflows, tasks, and items.
     * @param editorOrderTO the transfer object containing order details
     * @return the total time required for the order
     */
    private Integer calculateTotalTimeRequired(EditorOrderTO editorOrderTO) {
        return editorOrderTO.getWorkflows().stream()
                .flatMap(workflow -> workflow.getTasks().stream())
                .flatMap(task -> task.getItems().stream())
                .map(EditorItemTO::getTimeRequired)
                .filter(Objects::nonNull) // Filter out null values
                .mapToInt(Integer::intValue)
                .sum();
    }

}
