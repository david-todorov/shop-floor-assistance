package com.shopfloor.backend.database.mappers;

import com.shopfloor.backend.api.transferobjects.editors.*;
import com.shopfloor.backend.database.objects.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class serves as copier and mapper for existing orders.
 * It maps the "basic" properties on an OrderDBO from EditorOrderTO.
 * Sets the updater id and timestamp for update.
 * IMPORTANT:
 * - It returns new OrderDBO only if:
 *   - The new entity is supposed to be created with the help of "DBOInitializerMapper".
 *   - Creator id is actually the updater id in that case.
 * - Otherwise, it updates existing orders, but only the "basic" properties.
 *   - This ensures the integrity of the database.
 */
@Component
public class DBOUpdaterMapper {
    private final DBOInitializerMapper dboInitializerMapper;

    @Autowired
    public DBOUpdaterMapper(DBOInitializerMapper dboInitializerMapper) {
        this.dboInitializerMapper = dboInitializerMapper;
    }

    public EquipmentDBO copyEquipmentDboFrom(EquipmentDBO target, EditorEquipmentTO source, Long updaterId) {
        target.setEquipmentNumber(source.getEquipmentNumber());
        target.setName(source.getName());
        target.setType(source.getType());
        target.setDescription(source.getDescription());
        target.setUpdatedBy(updaterId);
        target.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return target;
    }

    public ProductDBO copyProductDboFrom(ProductDBO target, EditorProductTO source, Long updaterId) {
        target.setProductNumber(source.getProductNumber());
        target.setName(source.getName());
        target.setType(source.getType());
        target.setCountry(source.getCountry());
        target.setPackageSize(source.getPackageSize());
        target.setPackageType(source.getPackageType());
        target.setLanguage(source.getLanguage());
        target.setDescription(source.getDescription());
        target.setUpdatedBy(updaterId);
        target.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return target;
    }

    public OrderDBO copyOrderDboFrom(OrderDBO target, EditorOrderTO source, Long updaterId) {
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setOrderNumber(source.getOrderNumber());
        target.setUpdatedBy(updaterId);
        target.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        updateWorkflows(target.getWorkflows(), source.getWorkflows(), updaterId);
        return target;
    }

    private void updateWorkflows(List<WorkflowDBO> targetWorkflows, List<EditorWorkflowTO> sourceWorkflows, Long updaterId) {
        Map<Long, WorkflowDBO> targetMap = targetWorkflows.stream()
                .collect(Collectors.toMap(WorkflowDBO::getId, Function.identity()));

        // The 'orderingWorkflowIndex' is used to define the order of the workflows within order
        Integer orderingWorkflowIndex = 0;
        for (EditorWorkflowTO editorWorkflowTO : sourceWorkflows) {
            WorkflowDBO workflowDBO = targetMap.get(editorWorkflowTO.getId());

            if (workflowDBO != null) {
                // Update existing workflow
                copyWorkflowDboFrom(workflowDBO, editorWorkflowTO, updaterId);
                targetMap.remove(editorWorkflowTO.getId());

                // Set the ordering index for the existing workflow
                workflowDBO.setOrderingIndex(orderingWorkflowIndex++);
            } else {
                // Add new workflow
                WorkflowDBO newWorkflowDBO = dboInitializerMapper.toWorkflowDBO(editorWorkflowTO, updaterId);
                targetWorkflows.add(newWorkflowDBO);

                // Set the ordering index for the new workflow
                newWorkflowDBO.setOrderingIndex(orderingWorkflowIndex++);
            }
        }

        // Remove workflows no longer present in the source
        targetWorkflows.removeIf(workflow -> targetMap.containsKey(workflow.getId()));
    }

    public WorkflowDBO copyWorkflowDboFrom(WorkflowDBO target, EditorWorkflowTO source, Long updaterId) {
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setUpdatedBy(updaterId);
        target.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        updateTasks(target.getTasks(), source.getTasks(), updaterId);
        return target;
    }

    private void updateTasks(List<TaskDBO> targetTasks, List<EditorTaskTO> sourceTasks, Long updaterId) {
        Map<Long, TaskDBO> targetMap = targetTasks.stream()
                .collect(Collectors.toMap(TaskDBO::getId, Function.identity()));

        // The 'orderingTaskIndex' is used to define the order of the tasks within workflow
        Integer orderingTaskIndex = 0;
        for (EditorTaskTO editorTaskTO : sourceTasks) {
            TaskDBO taskDBO = targetMap.get(editorTaskTO.getId());

            if (taskDBO != null) {
                // Update existing task
                copyTaskDboFrom(taskDBO, editorTaskTO, updaterId);
                targetMap.remove(editorTaskTO.getId());

                // Set the ordering index for the existing task
                taskDBO.setOrderingIndex(orderingTaskIndex++);
            } else {
                // Add new task
                TaskDBO newTaskDBO = dboInitializerMapper.toTaskDBO(editorTaskTO, updaterId);
                targetTasks.add(newTaskDBO);

                // Set the ordering index for the new task
                newTaskDBO.setOrderingIndex(orderingTaskIndex++);
            }
        }

        // Remove tasks no longer present in the source
        targetTasks.removeIf(task -> targetMap.containsKey(task.getId()));
    }

    public TaskDBO copyTaskDboFrom(TaskDBO target, EditorTaskTO source, Long updaterId) {
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setUpdatedBy(updaterId);
        target.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        updateItems(target.getItems(), source.getItems(), updaterId);
        return target;
    }

    private void updateItems(List<ItemDBO> targetItems, List<EditorItemTO> sourceItems, Long updaterId) {
        Map<Long, ItemDBO> targetMap = targetItems.stream()
                .collect(Collectors.toMap(ItemDBO::getId, Function.identity()));

        // The 'orderingItemIndex' is used to define the order of the items within task
        Integer orderingItemIndex = 0;
        for (EditorItemTO editorItemTO : sourceItems) {
            ItemDBO itemDBO = targetMap.get(editorItemTO.getId());

            if (itemDBO != null) {
                // Update existing item
                copyItemDboFrom(itemDBO, editorItemTO, updaterId);
                targetMap.remove(editorItemTO.getId());

                // Set the ordering index for the existing item
                itemDBO.setOrderingIndex(orderingItemIndex++);
            } else {
                // Add new item
                ItemDBO newItemDBO = dboInitializerMapper.toItemDBO(editorItemTO, updaterId);
                targetItems.add(newItemDBO);

                // Set the ordering index for the new item
                newItemDBO.setOrderingIndex(orderingItemIndex++);
            }
        }

        // Remove items no longer present in the source
        targetItems.removeIf(item -> targetMap.containsKey(item.getId()));
    }

    public ItemDBO copyItemDboFrom(ItemDBO target, EditorItemTO source, Long updaterId) {
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setTimeRequired(source.getTimeRequired());
        target.setUpdatedBy(updaterId);
        target.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return target;
    }

    public ExecutionDBO finishExecution(ExecutionDBO executionDBO, Long finisherId) {
        executionDBO.setFinishedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        executionDBO.setFinishedBy(finisherId);
        return executionDBO;
    }

    public ExecutionDBO abortExecution(ExecutionDBO executionDBO) {
        executionDBO.setAborted(true);
        return executionDBO;
    }
}
