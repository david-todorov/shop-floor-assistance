package com.shopfloor.backend.service;

import com.shopfloor.backend.api.transferobjects.editors.EditorItemTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorOrderTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorTaskTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorWorkflowTO;
import com.shopfloor.backend.database.repositories.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class EditorControllerTest {

    @Autowired
    private ApiHelper apiHelper;

    @Autowired
    private OrderHelper orderHelper;

    @Autowired
    private OrderRepository orderRepository;


    @AfterEach
    public void tearDown() {
        // Clear the repository after each test
        orderRepository.deleteAll();
    }

    @Test
    public void when_GetEditorOrders_Then_OK() throws Exception {
        //Prepare 5 orders and save the response
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorOrderTO orderOne = apiHelper.createEditorOrderPOST(OrderHelper.buildCompleteEditorOrderTO("W0001"), authorizationHeader, 201);
        EditorOrderTO orderTwo = apiHelper.createEditorOrderPOST(OrderHelper.buildCompleteEditorOrderTO("W0002"), authorizationHeader, 201);
        EditorOrderTO orderThree = apiHelper.createEditorOrderPOST(OrderHelper.buildCompleteEditorOrderTO("W0003"), authorizationHeader, 201);
        EditorOrderTO orderFour = apiHelper.createEditorOrderPOST(OrderHelper.buildCompleteEditorOrderTO("W0004"), authorizationHeader, 201);
        EditorOrderTO orderFive = apiHelper.createEditorOrderPOST(OrderHelper.buildCompleteEditorOrderTO("W0005"), authorizationHeader, 201);

        List<EditorOrderTO> expectedOrders = new ArrayList<>();
        expectedOrders.add(orderOne);
        expectedOrders.add(orderTwo);
        expectedOrders.add(orderThree);
        expectedOrders.add(orderFour);
        expectedOrders.add(orderFive);

        //Get all orders
        List<EditorOrderTO> actualOrders = this.apiHelper.getEditorAllOrdersGET(authorizationHeader, 200);

        // Compare each element of the lists using the helper method
        for (int i = 0; i < expectedOrders.size(); i++) {
            orderHelper.assertEditorOrdersEqual(expectedOrders.get(i), actualOrders.get(i));
        }
    }

    @Test
    public void when_AddOrder_Then_Created() throws Exception {
        // Create a fully populated EditorOrderTO object for testing
        EditorOrderTO newOrder = OrderHelper.buildCompleteEditorOrderTO("W0001");

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Make POST request and save the response
        EditorOrderTO response = apiHelper.createEditorOrderPOST(newOrder, authorizationHeader, 201);


        // Verify the order is created by asserting the created order matches the new order
        EditorOrderTO actual = apiHelper.getEditorOrderGET(response.getId(), authorizationHeader, 200);
        orderHelper.assertEditorOrdersEqual(actual, response);
    }

    @Test
    public void when_AddOrder_WithNegativeTimeRequired_Then_BadRequest() throws Exception {

        //Creating new order with negative time required
        EditorOrderTO oderWithNegativeTimeRequired = OrderHelper.buildCompleteEditorOrderTO("W0001");
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        oderWithNegativeTimeRequired.getWorkflows().get(0).getTasks().get(0).getItems().get(0).setTimeRequired(-1);

        apiHelper.createEditorOrderPOST(oderWithNegativeTimeRequired, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithExistingOrderNumber_Then_Conflict() throws Exception {
        // Create a fully populated EditorOrderTO object for testing
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        apiHelper.createEditorOrderPOST(OrderHelper.buildCompleteEditorOrderTO("W0001"), authorizationHeader, 201);

        //Create new order with order number which already exists
        EditorOrderTO newOrderWithSameOrderNumber = OrderHelper.buildCompleteEditorOrderTO("W0001");

        //Make Post request and expect conflict
        apiHelper.createEditorOrderPOST(newOrderWithSameOrderNumber, authorizationHeader, 409);
    }

    @Test
    public void when_AddOrder_WithNullOrder_Then_BadRequest() throws Exception {
        // Create an EditorOrderTO object with a null order
        EditorOrderTO newNullOrder = null;

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        // Perform the POST request with null as order
        apiHelper.createEditorOrderPOST(newNullOrder, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullOrderNumber_Then_BadRequest() throws Exception {
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        // Create an EditorOrderTO object with a null order number
        EditorOrderTO newOrderWithNullNumber = OrderHelper.buildCompleteEditorOrderTO(null);

        //Make Post request with order with null order number
        apiHelper.createEditorOrderPOST(newOrderWithNullNumber, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithEmptyOrderNumber_Then_BadRequest() throws Exception {
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        // Create an EditorOrderTO object with empty order number
        EditorOrderTO newOrderWithNullNumber = OrderHelper.buildCompleteEditorOrderTO("");

        //Make Post request with order with null order number
        apiHelper.createEditorOrderPOST(newOrderWithNullNumber, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullOrderName_Then_BadRequest() throws Exception {
        // Create an EditorOrderTO object with a null name
        EditorOrderTO newOrderWithNullName = OrderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithNullName.setName(null);

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Make Post request with order with null order name
        apiHelper.createEditorOrderPOST(newOrderWithNullName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithEmptyOrderName_Then_BadRequest() throws Exception {
        // Create an EditorOrderTO object with empty name
        EditorOrderTO newOrderWithNullName = OrderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithNullName.setName("");

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Make Post request with order with null order name
        apiHelper.createEditorOrderPOST(newOrderWithNullName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullWorkflowName_Then_BadRequest() throws Exception {
        // Create an EditorOrderTO object with a null workflow name
        EditorOrderTO newOrderWithNullWorkflowName = OrderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithNullWorkflowName.getWorkflows().get(0).setName(null);

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        apiHelper.createEditorOrderPOST(newOrderWithNullWorkflowName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithEmptyWorkflowName_Then_BadRequest() throws Exception {
        // Create an EditorOrderTO object with empty workflow name
        EditorOrderTO newOrderWithNullWorkflowName = OrderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithNullWorkflowName.getWorkflows().get(0).setName("");

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        apiHelper.createEditorOrderPOST(newOrderWithNullWorkflowName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullTaskName_Then_BadRequest() throws Exception {
        // Create an EditorOrderTO object with a null task name
        EditorOrderTO newOrderWithNullTaskName = OrderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithNullTaskName.getWorkflows().get(0)
                .getTasks().get(0)
                .setName(null);

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        apiHelper.createEditorOrderPOST(newOrderWithNullTaskName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithEmptyTaskName_Then_BadRequest() throws Exception {
        // Create an EditorOrderTO object with empty task name
        EditorOrderTO newOrderWithNullTaskName = OrderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithNullTaskName.getWorkflows().get(0)
                .getTasks().get(0)
                .setName("");

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        apiHelper.createEditorOrderPOST(newOrderWithNullTaskName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullItemName_Then_BadRequest() throws Exception {
        // Create an EditorOrderTO object with a null item name
        EditorOrderTO newOrderWithNullItemName = OrderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithNullItemName.getWorkflows().get(0)
                .getTasks().get(0)
                .getItems().get(0)
                .setName(null);

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        apiHelper.createEditorOrderPOST(newOrderWithNullItemName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithEmptyItemName_Then_BadRequest() throws Exception {
        // Create an EditorOrderTO object with a null item name
        EditorOrderTO newOrderWithNullItemName = OrderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithNullItemName.getWorkflows().get(0)
                .getTasks().get(0)
                .getItems().get(0)
                .setName("");

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        apiHelper.createEditorOrderPOST(newOrderWithNullItemName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullWorklowList_Then_BadRequest() throws Exception {
        // Create an EditorOrderTO object with a null workflow list
        EditorOrderTO newOrderWithNullWorkflowList = OrderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithNullWorkflowList.setWorkflows(null);

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        apiHelper.createEditorOrderPOST(newOrderWithNullWorkflowList, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullWorklowEntity_Then_BadRequest() throws Exception {
        // Create an EditorOrderTO object with a null workflow entity
        EditorOrderTO newOrderWithNullWorkflowList = OrderHelper.buildCompleteEditorOrderTO("W0001");
       newOrderWithNullWorkflowList
               .getWorkflows()
               .add(null);

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        apiHelper.createEditorOrderPOST(newOrderWithNullWorkflowList, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullTaskEntity_Then_BadRequest() throws Exception {
        // Create an EditorOrderTO object with a null task entity
        EditorOrderTO newOrderWithNullWorkflowList = OrderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithNullWorkflowList
                .getWorkflows().get(0)
                .getTasks()
                .add(null);

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        apiHelper.createEditorOrderPOST(newOrderWithNullWorkflowList, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullItemEntity_Then_BadRequest() throws Exception {
        // Create an EditorOrderTO object with a null item entity
        EditorOrderTO newOrderWithNullWorkflowList = OrderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithNullWorkflowList.
                getWorkflows().get(0)
                .getTasks()
                .add(null);

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        apiHelper.createEditorOrderPOST(newOrderWithNullWorkflowList, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_Then_OK() throws Exception {
        //Preparing existing Order
        EditorOrderTO expected = OrderHelper.buildCompleteEditorOrderTO("W0001");
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        expected = apiHelper.createEditorOrderPOST(expected, authorizationHeader, 201);

        //Changing order completely from user perspective
        this.alteringOrderCompletely(expected);

        //Updating the order
        Long orderId = expected.getId();
        expected = apiHelper.updateEditorOrderPUT(orderId, expected, authorizationHeader, 200);

        //Getting the actual updated order
        EditorOrderTO actual = apiHelper.getEditorOrderGET(orderId, authorizationHeader, 200);

        // Comparing
        orderHelper.assertEditorOrdersEqual(actual, expected);
    }

    @Test
    public void when_UpdateOrder_WithNegativeTimeRequired_Then_BadRequest() throws Exception {
        //Saving and order
        EditorOrderTO orderWithNullNumber = OrderHelper.buildCompleteEditorOrderTO("W0001");
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        orderWithNullNumber = apiHelper.createEditorOrderPOST(orderWithNullNumber, authorizationHeader, 201);

        //Updating with null values
        orderWithNullNumber.setOrderNumber(null);
        apiHelper.updateEditorOrderPUT(orderWithNullNumber.getId(), orderWithNullNumber, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullOrderNumber_Then_BadRequest() throws Exception {
        //Saving and order
        EditorOrderTO oderWithNegativeTimeRequired = OrderHelper.buildCompleteEditorOrderTO("W0001");
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        oderWithNegativeTimeRequired = apiHelper.createEditorOrderPOST(oderWithNegativeTimeRequired, authorizationHeader, 201);

        //Updating with negative values
        oderWithNegativeTimeRequired.getWorkflows().get(0).getTasks().get(0).getItems().get(0).setTimeRequired(-1);
        apiHelper.updateEditorOrderPUT(oderWithNegativeTimeRequired.getId(), oderWithNegativeTimeRequired, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullName_Then_BadRequest() throws Exception {
        //Saving the order
        EditorOrderTO orderWithNullNumber = OrderHelper.buildCompleteEditorOrderTO("W0001");
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        orderWithNullNumber = apiHelper.createEditorOrderPOST(orderWithNullNumber, authorizationHeader, 201);

        //Updating with null values
        orderWithNullNumber.setName(null);
        apiHelper.updateEditorOrderPUT(orderWithNullNumber.getId(), orderWithNullNumber, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullWorkflowName_Then_BadRequest() throws Exception {
        //Saving the order
        EditorOrderTO orderWithNullWorkflowName = OrderHelper.buildCompleteEditorOrderTO("W0001");
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        orderWithNullWorkflowName = apiHelper.createEditorOrderPOST(orderWithNullWorkflowName, authorizationHeader, 201);

        //Updating with null values
        orderWithNullWorkflowName.getWorkflows().get(0).setName(null);
        apiHelper.updateEditorOrderPUT(orderWithNullWorkflowName.getId(), orderWithNullWorkflowName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullTaskName_Then_BadRequest() throws Exception {
        //Saving the order
        EditorOrderTO orderWithNullTaskName = OrderHelper.buildCompleteEditorOrderTO("W0001");
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        orderWithNullTaskName = apiHelper.createEditorOrderPOST(orderWithNullTaskName, authorizationHeader, 201);

        //Updating the order with null values
        orderWithNullTaskName.getWorkflows().get(0).getTasks().get(0).setName(null);
        apiHelper.updateEditorOrderPUT(orderWithNullTaskName.getId(), orderWithNullTaskName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullItemName_Then_BadRequest() throws Exception {
        //Saving the order
        EditorOrderTO orderWithNullItemName = OrderHelper.buildCompleteEditorOrderTO("W0001");
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        orderWithNullItemName = apiHelper.createEditorOrderPOST(orderWithNullItemName, authorizationHeader, 201);

        //Updating the order with null values
        orderWithNullItemName.getWorkflows().get(0).getTasks().get(0).getItems().get(0).setName(null);
        apiHelper.updateEditorOrderPUT(orderWithNullItemName.getId(), orderWithNullItemName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullWorkflowList_Then_BadRequest() throws Exception {
        //Saving the order
        EditorOrderTO orderWithNullItemName = OrderHelper.buildCompleteEditorOrderTO("W0001");
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        orderWithNullItemName = apiHelper.createEditorOrderPOST(orderWithNullItemName, authorizationHeader, 201);

        //Updating the order with null values
        orderWithNullItemName.setWorkflows(null);
        apiHelper.updateEditorOrderPUT(orderWithNullItemName.getId(), orderWithNullItemName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullWorkflowEntity_Then_BadRequest() throws Exception {
        //Saving the order
        EditorOrderTO orderWithNullItemName = OrderHelper.buildCompleteEditorOrderTO("W0001");
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        orderWithNullItemName = apiHelper.createEditorOrderPOST(orderWithNullItemName, authorizationHeader, 201);

        //Updating the order with null values
        orderWithNullItemName.getWorkflows().add(null);
        apiHelper.updateEditorOrderPUT(orderWithNullItemName.getId(), orderWithNullItemName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullTaskList_Then_BadRequest() throws Exception {
        //Saving the order
        EditorOrderTO orderWithNullItemName = OrderHelper.buildCompleteEditorOrderTO("W0001");
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        orderWithNullItemName = apiHelper.createEditorOrderPOST(orderWithNullItemName, authorizationHeader, 201);

        //Updating the order with null values
        orderWithNullItemName.getWorkflows().get(0).setTasks(null);
        apiHelper.updateEditorOrderPUT(orderWithNullItemName.getId(), orderWithNullItemName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullTaskEntity_Then_BadRequest() throws Exception {
        //Saving the order
        EditorOrderTO orderWithNullItemName = OrderHelper.buildCompleteEditorOrderTO("W0001");
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        orderWithNullItemName = apiHelper.createEditorOrderPOST(orderWithNullItemName, authorizationHeader, 201);

        //Updating the order with null values
        orderWithNullItemName.getWorkflows().get(0).getTasks().add(null);
        apiHelper.updateEditorOrderPUT(orderWithNullItemName.getId(), orderWithNullItemName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullItemEntity_Then_BadRequest() throws Exception {
        //Saving the order
        EditorOrderTO orderWithNullItemName = OrderHelper.buildCompleteEditorOrderTO("W0001");
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        orderWithNullItemName = apiHelper.createEditorOrderPOST(orderWithNullItemName, authorizationHeader, 201);

        //Updating the order with null values
        orderWithNullItemName.getWorkflows().get(0).getTasks().get(0).getItems().add(null);
        apiHelper.updateEditorOrderPUT(orderWithNullItemName.getId(), orderWithNullItemName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNotExistingOrder_Then_NotFound() throws Exception {
        EditorOrderTO orderWithNonExistingId = OrderHelper.buildCompleteEditorOrderTO("W0001");
        orderWithNonExistingId.setId(999L);
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        apiHelper.updateEditorOrderPUT(orderWithNonExistingId.getId(), orderWithNonExistingId, authorizationHeader, 404);
    }

    @Test
    public void when_UpdateOrder_WithExistingOrderNumber_Then_Conflict() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        apiHelper.createEditorOrderPOST(OrderHelper.buildCompleteEditorOrderTO("W0001"), authorizationHeader, 201);


        EditorOrderTO toBeUpdated = OrderHelper.buildCompleteEditorOrderTO("W0002");
        toBeUpdated = apiHelper.createEditorOrderPOST(toBeUpdated, authorizationHeader, 201);

        toBeUpdated.setOrderNumber("W0001");
        apiHelper.updateEditorOrderPUT(toBeUpdated.getId(), toBeUpdated, authorizationHeader, 409);
    }

    @Test
    public void when_DeleteOrder_WithNonExistingId_Then_NotFound() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        apiHelper.deleteEditorOrderDELETE(999L, authorizationHeader, 404);
    }

    @Test
    public void when_DeleteOrder_Then_NoContent() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorOrderTO toBeDeleted = OrderHelper.buildCompleteEditorOrderTO("W0001");
        toBeDeleted = apiHelper.createEditorOrderPOST(toBeDeleted, authorizationHeader, 201);

        Long toDeleteId = toBeDeleted.getId();
        assertTrue(this.orderRepository.existsById(toDeleteId));

        apiHelper.deleteEditorOrderDELETE(toDeleteId, authorizationHeader, 204);

        assertFalse(orderRepository.existsById(toBeDeleted.getId()));
    }

    @Test
    public void when_GetOrder_Then_OK() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorOrderTO expected = OrderHelper.buildCompleteEditorOrderTO("W0001");
        expected = apiHelper.createEditorOrderPOST(expected, authorizationHeader, 201);

        EditorOrderTO actual = apiHelper.getEditorOrderGET(expected.getId(), authorizationHeader, 200);

        orderHelper.assertEditorOrdersEqual(expected, actual);
    }

    @Test
    public void when_GetOrder_WithNonExistingId_Then_NotFound() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        apiHelper.getEditorOrderGET(999L, authorizationHeader, 404);
    }


    private void alteringOrderCompletely(EditorOrderTO toAlter){

        toAlter.setName("New name");
        toAlter.setDescription("New description");

        // Update workflows: updating existing workflows and adding a new one
        List<EditorWorkflowTO> updatedWorkflows = new ArrayList<>();

        // Preserve all workflows except the last one
        List<EditorWorkflowTO> existingWorkflows = toAlter.getWorkflows();
        for (int i = 0; i < existingWorkflows.size(); i++) {
            EditorWorkflowTO existingWorkflow = existingWorkflows.get(i);

            // Update all workflows except the last one
            if (i < existingWorkflows.size() - 1) {
                existingWorkflow.setName("Updated Workflow Name " + i);
                existingWorkflow.setDescription("Updated Workflow Description " + i);

                // Update tasks in the existing workflow
                List<EditorTaskTO> updatedTasks = new ArrayList<>();
                List<EditorTaskTO> existingTasks = existingWorkflow.getTasks();
                for (int j = 0; j < existingTasks.size(); j++) {
                    EditorTaskTO task = existingTasks.get(j);

                    // Update all tasks except the last one
                    if (j < existingTasks.size() - 1) {
                        task.setName("Updated Task Name " + j);
                        task.setDescription("Updated Task Description " + j);

                        // Update items in the existing task
                        List<EditorItemTO> updatedItems = new ArrayList<>();
                        List<EditorItemTO> existingItems = task.getItems();
                        for (int k = 0; k < existingItems.size(); k++) {
                            EditorItemTO item = existingItems.get(k);

                            // Update all items except the last one
                            if (k < existingItems.size() - 1) {
                                item.setName("Updated Item Name " + k);
                                item.setDescription("Updated Item Description " + k);
                                updatedItems.add(item); // Keep updated item
                            }
                        }

                        // Remove the last item
                        if (!existingItems.isEmpty()) {
                            existingItems.remove(existingItems.size() - 1);
                        }

                        task.setItems(updatedItems); // Set updated items
                        updatedTasks.add(task); // Keep updated task
                    }
                }

                // Remove the last task
                if (!existingTasks.isEmpty()) {
                    existingTasks.remove(existingTasks.size() - 1);
                }

                existingWorkflow.setTasks(updatedTasks); // Set updated tasks
                updatedWorkflows.add(existingWorkflow); // Keep updated workflow
            }
        }

        // Remove the last workflow
        if (!existingWorkflows.isEmpty()) {
            existingWorkflows.remove(existingWorkflows.size() - 1);
        }

        // Adding a completely new workflow
        EditorWorkflowTO newWorkflow = new EditorWorkflowTO();
        newWorkflow.setName("Completely New Workflow");
        newWorkflow.setDescription("Description for the completely new workflow");

        // Creating tasks for the new workflow
        List<EditorTaskTO> newTasks = new ArrayList<>();
        for (int i = 1; i <= 2; i++) { // Add 2 new tasks
            EditorTaskTO newTask = new EditorTaskTO();
            newTask.setName("New Task " + i);
            newTask.setDescription("Description for new task " + i);

            // Creating items for the new task
            List<EditorItemTO> newItems = new ArrayList<>();
            for (int j = 1; j <= 2; j++) { // Add 2 new items
                EditorItemTO newItem = new EditorItemTO("New Item " + i + j, "Description for item " + i + j, 10 * j);
                newItems.add(newItem);
            }

            newTask.setItems(newItems);
            newTasks.add(newTask);
        }

        newWorkflow.setTasks(newTasks);
        updatedWorkflows.add(newWorkflow); // Add the new workflow

        // Set updated workflows to the order
        toAlter.setWorkflows(updatedWorkflows);
    }
}
