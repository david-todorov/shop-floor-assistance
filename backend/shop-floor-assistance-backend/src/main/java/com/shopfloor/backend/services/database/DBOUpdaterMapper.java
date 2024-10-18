package com.shopfloor.backend.services.database;

import com.shopfloor.backend.api.transferobjects.ItemTO;
import com.shopfloor.backend.api.transferobjects.OrderTO;
import com.shopfloor.backend.api.transferobjects.TaskTO;
import com.shopfloor.backend.api.transferobjects.WorkflowTO;
import com.shopfloor.backend.services.database.objects.ItemDBO;
import com.shopfloor.backend.services.database.objects.OrderDBO;
import com.shopfloor.backend.services.database.objects.TaskDBO;
import com.shopfloor.backend.services.database.objects.WorkflowDBO;
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
 * This class serve as copier and mapper for existing orders
 * It maps the "basic" properties on an OrderDBO from OrderTO
 * Sets the updater id and timestamp for update
 *                    IMPORTANT
 * It returns new OrderDBO only if
 * - The new entity is supposed to be created with the help of "DBOInitializerMapper"
 * - Creator id is actually the updater id in that case
 * Otherwise
 * - Updates existing order, but only the "basic" properties
 * - This ensures the integrity of the database
 */
@Component
public class DBOUpdaterMapper {
    private final DBOInitializerMapper dboInitializerMapper;

    @Autowired
    public DBOUpdaterMapper(DBOInitializerMapper dboInitializerMapper) {
        this.dboInitializerMapper = dboInitializerMapper;
    }

    public OrderDBO copyOrderDboFrom(OrderDBO target, OrderTO source, Long updaterId) {
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setOrderNumber(source.getOrderNumber());
        target.setUpdatedBy(updaterId);
        target.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        updateWorkflows(target.getWorkflows(), source.getWorkflows(), updaterId);
        return target;
    }

    private void updateWorkflows(List<WorkflowDBO> targetWorkflows, List<WorkflowTO> sourceWorkflows, Long updaterId) {
        Map<Long, WorkflowDBO> targetMap = targetWorkflows.stream()
                .collect(Collectors.toMap(WorkflowDBO::getId, Function.identity()));

        for (WorkflowTO workflowTO : sourceWorkflows) {
            WorkflowDBO workflowDBO = targetMap.get(workflowTO.getId());

            if (workflowDBO != null) {
                // Update existing workflow
                copyWorkflowDboFrom(workflowDBO, workflowTO, updaterId);
                targetMap.remove(workflowTO.getId());
            } else {
                // Add new workflow
                WorkflowDBO newWorkflowDBO = dboInitializerMapper.toWorkflowDBO(workflowTO, updaterId);
                targetWorkflows.add(newWorkflowDBO);
            }
        }

        // Remove workflows no longer present in the source
        targetWorkflows.removeIf(workflow -> targetMap.containsKey(workflow.getId()));
    }

    public WorkflowDBO copyWorkflowDboFrom(WorkflowDBO target, WorkflowTO source, Long updaterId) {
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setUpdatedBy(updaterId);
        target.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        updateTasks(target.getTasks(), source.getTasks(), updaterId);
        return target;
    }

    private void updateTasks(List<TaskDBO> targetTasks, List<TaskTO> sourceTasks, Long updaterId) {
        Map<Long, TaskDBO> targetMap = targetTasks.stream()
                .collect(Collectors.toMap(TaskDBO::getId, Function.identity()));

        for (TaskTO taskTO : sourceTasks) {
            TaskDBO taskDBO = targetMap.get(taskTO.getId());

            if (taskDBO != null) {
                // Update existing task
                copyTaskDboFrom(taskDBO, taskTO, updaterId);
                targetMap.remove(taskTO.getId());
            } else {
                // Add new task
                TaskDBO newTaskDBO = dboInitializerMapper.toTaskDBO(taskTO, updaterId);
                targetTasks.add(newTaskDBO);
            }
        }

        // Remove tasks no longer present in the source
        targetTasks.removeIf(task -> targetMap.containsKey(task.getId()));
    }

    public TaskDBO copyTaskDboFrom(TaskDBO target, TaskTO source, Long updaterId) {
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setUpdatedBy(updaterId);
        target.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        updateItems(target.getItems(), source.getItems(), updaterId);
        return target;
    }

    private void updateItems(List<ItemDBO> targetItems, List<ItemTO> sourceItems, Long updaterId) {
        Map<Long, ItemDBO> targetMap = targetItems.stream()
                .collect(Collectors.toMap(ItemDBO::getId, Function.identity()));

        for (ItemTO itemTO : sourceItems) {
            ItemDBO itemDBO = targetMap.get(itemTO.getId());

            if (itemDBO != null) {
                // Update existing item
                copyItemDboFrom(itemDBO, itemTO, updaterId);
                targetMap.remove(itemTO.getId());
            } else {
                // Add new item
                ItemDBO newItemDBO = dboInitializerMapper.toItemDBO(itemTO, updaterId);
                targetItems.add(newItemDBO);
            }
        }

        // Remove items no longer present in the source
        targetItems.removeIf(item -> targetMap.containsKey(item.getId()));
    }

    public ItemDBO copyItemDboFrom(ItemDBO target, ItemTO source, Long updaterId) {
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setTimeRequired(source.getTimeRequired());
        target.setUpdatedBy(updaterId);
        target.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return target;
    }

}
