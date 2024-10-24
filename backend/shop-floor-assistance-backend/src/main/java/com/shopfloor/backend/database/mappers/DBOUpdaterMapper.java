package com.shopfloor.backend.database.mappers;

import com.shopfloor.backend.api.transferobjects.editors.EditorItemTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorOrderTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorTaskTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorWorkflowTO;
import com.shopfloor.backend.database.objects.ItemDBO;
import com.shopfloor.backend.database.objects.OrderDBO;
import com.shopfloor.backend.database.objects.TaskDBO;
import com.shopfloor.backend.database.objects.WorkflowDBO;
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
 * It maps the "basic" properties on an OrderDBO from EditorOrderTO
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

        for (EditorWorkflowTO editorWorkflowTO : sourceWorkflows) {
            WorkflowDBO workflowDBO = targetMap.get(editorWorkflowTO.getId());

            if (workflowDBO != null) {
                // Update existing workflow
                copyWorkflowDboFrom(workflowDBO, editorWorkflowTO, updaterId);
                targetMap.remove(editorWorkflowTO.getId());
            } else {
                // Add new workflow
                WorkflowDBO newWorkflowDBO = dboInitializerMapper.toWorkflowDBO(editorWorkflowTO, updaterId);
                targetWorkflows.add(newWorkflowDBO);
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

        for (EditorTaskTO editorTaskTO : sourceTasks) {
            TaskDBO taskDBO = targetMap.get(editorTaskTO.getId());

            if (taskDBO != null) {
                // Update existing task
                copyTaskDboFrom(taskDBO, editorTaskTO, updaterId);
                targetMap.remove(editorTaskTO.getId());
            } else {
                // Add new task
                TaskDBO newTaskDBO = dboInitializerMapper.toTaskDBO(editorTaskTO, updaterId);
                targetTasks.add(newTaskDBO);
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

        for (EditorItemTO editorItemTO : sourceItems) {
            ItemDBO itemDBO = targetMap.get(editorItemTO.getId());

            if (itemDBO != null) {
                // Update existing item
                copyItemDboFrom(itemDBO, editorItemTO, updaterId);
                targetMap.remove(editorItemTO.getId());
            } else {
                // Add new item
                ItemDBO newItemDBO = dboInitializerMapper.toItemDBO(editorItemTO, updaterId);
                targetItems.add(newItemDBO);
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

}
