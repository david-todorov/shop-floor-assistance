package com.shopfloor.backend.service;

import com.shopfloor.backend.api.transferobjects.editors.EditorItemTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorOrderTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorTaskTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorWorkflowTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorItemTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorOrderTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorTaskTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorWorkflowTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Component
public class OrderHelper {

    public static EditorOrderTO buildCompleteEditorOrderTO(String orderNumber) {
        EditorOrderTO editorOrderTO = new EditorOrderTO();
        editorOrderTO.setOrderNumber(orderNumber);
        editorOrderTO.setName("AAA");
        editorOrderTO.setDescription("Aspirin max");

        // Initialize workflows as a mutable ArrayList
        editorOrderTO.setWorkflows(new ArrayList<>());

        // Creating Workflow 1: Change Over
        EditorWorkflowTO changeOverWorkflow = new EditorWorkflowTO();
        changeOverWorkflow.setName("Change Over");
        changeOverWorkflow.setDescription("Transition machine settings for a new product batch");

        // Creating Task 1 for Change Over: Cleaning
        EditorTaskTO cleaningTask = new EditorTaskTO();
        cleaningTask.setName("Cleaning");
        cleaningTask.setDescription("Clean machine surfaces to maintain hygiene standards");

        cleaningTask.setItems(new ArrayList<>(Arrays.asList(new EditorItemTO("Remove Tablets", "Safely remove any remaining tablets from the machine", 25), new EditorItemTO("Remove Leaflets", "Clear out any unused leaflets from previous production", 10), new EditorItemTO("Remove Cartons", "Remove any leftover cartons from the machine to prevent mix-ups", 20))));

        // Creating Task 2 for Change Over: Mounting
        EditorTaskTO mountingTask = new EditorTaskTO();
        mountingTask.setName("Mounting");
        mountingTask.setDescription("Mounting");

        mountingTask.setItems(new ArrayList<>(Arrays.asList(new EditorItemTO("Check Heating Plates", "Ensure heating plates are properly functioning and aligned", 10), new EditorItemTO("Change Forming Layout", "Adjust the forming layout to match the new product requirements", 60), new EditorItemTO("Change Sealing Roll", "Replace or adjust the sealing roll for the new product", 45), new EditorItemTO("Set manual parameters", "Update machine settings according to product specifications", null))));

        // Adding Tasks to Change Over Workflow
        changeOverWorkflow.setTasks(new ArrayList<>(Arrays.asList(cleaningTask, mountingTask)));

        // Creating Workflow 2: Commissioning
        EditorWorkflowTO commissioningWorkflow = new EditorWorkflowTO();
        commissioningWorkflow.setName("Commissioning");
        commissioningWorkflow.setDescription("Prepare the machine for production");

        // Creating Task 1 for Commissioning: Thread in forming film
        EditorTaskTO threadFilmTask = new EditorTaskTO();
        threadFilmTask.setName("Thread in forming film");
        threadFilmTask.setDescription("Feed the forming film into the machine");

        threadFilmTask.setItems(new ArrayList<>(Arrays.asList(new EditorItemTO("Thread forming film to unwinding", "Guide the forming film to the unwinding section", 40), new EditorItemTO("Activate 'manual unwinding'", "Start manual unwinding through the HMI", 1), new EditorItemTO("Thread forming film to filling section", "Direct the forming film to the filling section", 50))));

        // Adding Tasks to Commissioning Workflow
        commissioningWorkflow.setTasks(new ArrayList<>(Arrays.asList(threadFilmTask)));

        // Adding both workflows to the Order
        editorOrderTO.getWorkflows().add(changeOverWorkflow);
        editorOrderTO.getWorkflows().add(commissioningWorkflow);

        return editorOrderTO;
    }

    public void assertEditorOrdersEqual(EditorOrderTO expected, EditorOrderTO actual) {

        if(expected.getId() != null && actual.getId() != null) {
            assertEquals(expected.getId(), actual.getId());
        }

        assertEquals(expected.getOrderNumber(), actual.getOrderNumber());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());

        assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
        assertEquals(expected.getCreatedBy(), actual.getCreatedBy());
        assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());
        assertEquals(expected.getUpdatedBy(), actual.getUpdatedBy());

        // Compare workflows
        assertEquals(expected.getWorkflows().size(), actual.getWorkflows().size());
        for (int i = 0; i < expected.getWorkflows().size(); i++) {
            assertEditorWorkflowsEqual(expected.getWorkflows().get(i), actual.getWorkflows().get(i));
        }
    }

    private void assertEditorWorkflowsEqual(EditorWorkflowTO expected, EditorWorkflowTO actual) {

        if(expected.getId() != null && actual.getId() != null) {
            assertEquals(expected.getId(), actual.getId());
        }

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());

        assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
        assertEquals(expected.getCreatedBy(), actual.getCreatedBy());
        assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());
        assertEquals(expected.getUpdatedBy(), actual.getUpdatedBy());

        // Compare tasks
        assertEquals(expected.getTasks().size(), actual.getTasks().size());
        for (int i = 0; i < expected.getTasks().size(); i++) {
            assertEditorTasksEqual(expected.getTasks().get(i), actual.getTasks().get(i));
        }
    }

    private void assertEditorTasksEqual(EditorTaskTO expected, EditorTaskTO actual) {

        if(expected.getId() != null && actual.getId() != null) {
            assertEquals(expected.getId(), actual.getId());
        }

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());

        assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
        assertEquals(expected.getCreatedBy(), actual.getCreatedBy());
        assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());
        assertEquals(expected.getUpdatedBy(), actual.getUpdatedBy());

        // Compare items
        assertEquals(expected.getItems().size(), actual.getItems().size());
        for (int i = 0; i < expected.getItems().size(); i++) {
            assertEditorItemsEqual(expected.getItems().get(i), actual.getItems().get(i));
        }
    }

    private void assertEditorItemsEqual(EditorItemTO expected, EditorItemTO actual) {

        if(expected.getId() != null && actual.getId() != null) {
            assertEquals(expected.getId(), actual.getId());
        }

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getTimeRequired(), actual.getTimeRequired());

        assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
        assertEquals(expected.getCreatedBy(), actual.getCreatedBy());
        assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());
        assertEquals(expected.getUpdatedBy(), actual.getUpdatedBy());
    }

    public void assertEditorAndOperatorOrdersEqual(EditorOrderTO expected, OperatorOrderTO actual) {

        if(expected.getId() != null && actual.getId() != null) {
            assertEquals(expected.getId(), actual.getId());
        }

        assertEquals(expected.getOrderNumber(), actual.getOrderNumber());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());

        assertEquals(expected.getWorkflows().size(), actual.getWorkflows().size());
        for (int i = 0; i < expected.getWorkflows().size(); i++) {
            assertEditorAndOperatorWorkflowsEqual(expected.getWorkflows().get(i), actual.getWorkflows().get(i));
        }
    }

    private void assertEditorAndOperatorWorkflowsEqual(EditorWorkflowTO expected, OperatorWorkflowTO actual) {

        if(expected.getId() != null && actual.getId() != null) {
            assertEquals(expected.getId(), actual.getId());
        }

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());

        assertEquals(expected.getTasks().size(), actual.getTasks().size());
        for (int i = 0; i < expected.getTasks().size(); i++) {
            assertEditorAndOperatorTasksEqual(expected.getTasks().get(i), actual.getTasks().get(i));
        }
    }

    private void assertEditorAndOperatorTasksEqual(EditorTaskTO expected, OperatorTaskTO actual) {

        if(expected.getId() != null && actual.getId() != null) {
            assertEquals(expected.getId(), actual.getId());
        }

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());

        // Compare items
        assertEquals(expected.getItems().size(), actual.getItems().size());
        for (int i = 0; i < expected.getItems().size(); i++) {
            assertEditorAndOperatorItemsEqual(expected.getItems().get(i), actual.getItems().get(i));
        }
    }

    private void assertEditorAndOperatorItemsEqual(EditorItemTO expected, OperatorItemTO actual) {

        if(expected.getId() != null && actual.getId() != null) {
            assertEquals(expected.getId(), actual.getId());
        }

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getTimeRequired(), actual.getTimeRequired());
    }

}
