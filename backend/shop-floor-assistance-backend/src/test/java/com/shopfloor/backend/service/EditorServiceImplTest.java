package com.shopfloor.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopfloor.backend.api.transferobjects.*;
import com.shopfloor.backend.database.exceptions.*;
import com.shopfloor.backend.services.EditorServiceImpl;
import com.shopfloor.backend.database.repositories.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class EditorServiceImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EditorServiceImpl editorService;

    @Autowired
    private OrderRepository orderRepository;

    @AfterEach
    public void tearDown() {
        // Clear the repository after each test
        orderRepository.deleteAll();
    }

    @Test
    public void when_AddOrder_Then_Success() throws Exception {

        String authorizationHeader = this.createAuthorizationHeaderFrom("editor", "editor");

        OrderTO newOrderTO = this.createCompleteOrderTO("W001");

        Long newOrderId = editorService.addOrder(newOrderTO, authorizationHeader).getId();

        OrderTO resultOrderTO = editorService.getOrder(newOrderId);

        // Verify results
        assertTrue(this.orderRepository.existsById(resultOrderTO.getId()));
        assertOrdersEqual(newOrderTO, resultOrderTO);
    }

    @Test
    public void when_AddOrder_WithNullOrderNumber_Then_ThrowsMissingOrderNumberException() throws Exception{

        String authorizationHeader = this.createAuthorizationHeaderFrom("editor", "editor");
        OrderTO orderWithNullOrderNumber = this.createCompleteOrderTO(null);

        assertThrows(MissingOrderNumberException.class, () -> {
            editorService.addOrder(orderWithNullOrderNumber, authorizationHeader);
        });
    }

    @Test
    public void when_AddOrder_WithNullOrder_Then_ThrowsMissingOrderException() throws Exception{
        String authorizationHeader = this.createAuthorizationHeaderFrom("editor", "editor");
        OrderTO nullOrder = null;

        assertThrows(MissingOrderException.class, () -> {
            editorService.addOrder(nullOrder, authorizationHeader);
        });
    }

    @Test
    public void when_AddOrder_WithNullAuthorizationHeader_Then_ThrowsMissingAuthorizationHeaderException() throws Exception{
        String nullAuthorizationHeader = null;
        OrderTO order = this.createCompleteOrderTO("W001");

        assertThrows(MissingAuthorizationHeaderException.class, () -> {
            editorService.addOrder(order, nullAuthorizationHeader);
        });
    }

    @Test
    public void when_AddOrder_WithDuplicatedOrderNumber_Then_ThrowsDuplicatedOrderException() throws Exception {
        String authorizationHeader = this.createAuthorizationHeaderFrom("editor", "editor");
        OrderTO existingOrder = this.createCompleteOrderTO("W001");

        // Add the existing order to the repository
        editorService.addOrder(existingOrder, authorizationHeader);

        OrderTO newOrderWithSameOrderNumber = this.createCompleteOrderTO("W001");

        // Act and Assert
        assertThrows(DuplicatedOrderException.class, () -> {
            editorService.addOrder(newOrderWithSameOrderNumber, authorizationHeader);
        });
    }

    @Test
    public void when_UpdateOrder_Then_Success() throws Exception {
        // Saving an order
        String authorizationHeader = this.createAuthorizationHeaderFrom("editor", "editor");
        OrderTO existingOrderTO = this.createCompleteOrderTO("W001");
        existingOrderTO = editorService.addOrder(existingOrderTO, authorizationHeader);

        // Updating the saved order from user perspective
        existingOrderTO.setName("New name");
        existingOrderTO.setDescription("New description");

        // Update workflows: updating existing workflows and adding a new one
        List<WorkflowTO> updatedWorkflows = new ArrayList<>();

        // Preserve all workflows except the last one
        List<WorkflowTO> existingWorkflows = existingOrderTO.getWorkflows();
        for (int i = 0; i < existingWorkflows.size(); i++) {
            WorkflowTO existingWorkflow = existingWorkflows.get(i);

            // Update all workflows except the last one
            if (i < existingWorkflows.size() - 1) {
                existingWorkflow.setName("Updated Workflow Name " + i);
                existingWorkflow.setDescription("Updated Workflow Description " + i);

                // Update tasks in the existing workflow
                List<TaskTO> updatedTasks = new ArrayList<>();
                List<TaskTO> existingTasks = existingWorkflow.getTasks();
                for (int j = 0; j < existingTasks.size(); j++) {
                    TaskTO task = existingTasks.get(j);

                    // Update all tasks except the last one
                    if (j < existingTasks.size() - 1) {
                        task.setName("Updated Task Name " + j);
                        task.setDescription("Updated Task Description " + j);

                        // Update items in the existing task
                        List<ItemTO> updatedItems = new ArrayList<>();
                        List<ItemTO> existingItems = task.getItems();
                        for (int k = 0; k < existingItems.size(); k++) {
                            ItemTO item = existingItems.get(k);

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
        WorkflowTO newWorkflow = new WorkflowTO();
        newWorkflow.setName("Completely New Workflow");
        newWorkflow.setDescription("Description for the completely new workflow");

        // Creating tasks for the new workflow
        List<TaskTO> newTasks = new ArrayList<>();
        for (int i = 1; i <= 2; i++) { // Add 2 new tasks
            TaskTO newTask = new TaskTO();
            newTask.setName("New Task " + i);
            newTask.setDescription("Description for new task " + i);

            // Creating items for the new task
            List<ItemTO> newItems = new ArrayList<>();
            for (int j = 1; j <= 2; j++) { // Add 2 new items
                ItemTO newItem = new ItemTO("New Item " + i + j, "Description for item " + i + j, 10 * j);
                newItems.add(newItem);
            }

            newTask.setItems(newItems);
            newTasks.add(newTask);
        }

        newWorkflow.setTasks(newTasks);
        updatedWorkflows.add(newWorkflow); // Add the new workflow

        // Set updated workflows to the order
        existingOrderTO.setWorkflows(updatedWorkflows);

        // Saving the updated order
        Long orderId = existingOrderTO.getId();
        existingOrderTO = editorService.updateOrder(orderId, existingOrderTO, authorizationHeader);

        // Comparing
        assertOrdersEqual(this.editorService.getOrder(orderId), existingOrderTO);
    }


    @Test
    public void when_UpdateOrder_WithNullOrderNumber_Then_ThrowsMissingOrderNumberException() throws Exception{
        String authorizationHeader = this.createAuthorizationHeaderFrom("editor", "editor");
        OrderTO existingOrderTO = this.createCompleteOrderTO("W001");
        existingOrderTO = this.editorService.addOrder(existingOrderTO, authorizationHeader);

        OrderTO modifiedOrder = existingOrderTO;
        modifiedOrder.setOrderNumber(null);

        // Act and Assert
        assertThrows(MissingOrderNumberException.class, () -> {
            editorService.updateOrder(modifiedOrder.getId(), modifiedOrder, authorizationHeader);
        });

    }

    @Test
    public void when_UpdateOrder_WithNullOrderId_Then_ThrowsMissingOrderIdException() throws Exception{
        String authorizationHeader = this.createAuthorizationHeaderFrom("editor", "editor");
        OrderTO existingOrderTO = this.createCompleteOrderTO("W001");
        existingOrderTO = this.editorService.addOrder(existingOrderTO, authorizationHeader);

        OrderTO modifiedOrder = existingOrderTO;

        // Act and Assert
        assertThrows(MissingOrderIdException.class, () -> {
            editorService.updateOrder(null, modifiedOrder, authorizationHeader);
        });

    }

    @Test
    public void when_UpdateOrder_WithNullOrder_Then_ThrowsMissingOrderException() throws Exception{
        String authorizationHeader = this.createAuthorizationHeaderFrom("editor", "editor");
        OrderTO existingOrderTO = this.createCompleteOrderTO("W001");
        existingOrderTO = this.editorService.addOrder(existingOrderTO, authorizationHeader);

        OrderTO modifiedOrder = existingOrderTO;

        // Act and Assert
        assertThrows(MissingOrderException.class, () -> {
            editorService.updateOrder(modifiedOrder.getId(), null, authorizationHeader);
        });

    }

    @Test
    public void when_UpdateOrder_WithNullAuthorizationHeader_Then_ThrowsMissingAuthorizationHeaderException() throws Exception{
        String authorizationHeader = this.createAuthorizationHeaderFrom("editor", "editor");
        OrderTO existingOrderTO = this.createCompleteOrderTO("W001");
        existingOrderTO = this.editorService.addOrder(existingOrderTO, authorizationHeader);

        OrderTO modifiedOrder = existingOrderTO;

        // Act and Assert
        assertThrows(MissingAuthorizationHeaderException.class, () -> {
            editorService.updateOrder(modifiedOrder.getId(), modifiedOrder, null);
        });

    }

    @Test
    public void when_UpdateOrder_WithNonExistentOrderId_Then_ThrowsOrderNotFoundException() throws Exception{
        String authorizationHeader = this.createAuthorizationHeaderFrom("editor", "editor");
        OrderTO existingOrderTO = this.createCompleteOrderTO("W001");
        existingOrderTO = this.editorService.addOrder(existingOrderTO, authorizationHeader);

        OrderTO modifiedOrder = existingOrderTO;
        modifiedOrder.setId(999L);

        // Act and Assert
        assertThrows(OrderNotFoundException.class, () -> {
            editorService.updateOrder(modifiedOrder.getId(), modifiedOrder, authorizationHeader);
        });

    }

    @Test
    public void when_UpdateOrder_WithExistingOrderNumber_Then_ThrowsDuplicatedOrderException() throws Exception{
        //Creating and saving first order
        String authorizationHeader = this.createAuthorizationHeaderFrom("editor", "editor");
        OrderTO existingOrderOne = this.createCompleteOrderTO("W001");
        this.editorService.addOrder(existingOrderOne, authorizationHeader);

        //Creating and saving second order
        OrderTO existingOrderTwo = this.createCompleteOrderTO("W002");
        existingOrderTwo = this.editorService.addOrder(existingOrderTwo, authorizationHeader);

        //Updating second order with already existing order number
        OrderTO orderTwoModified = existingOrderTwo;
        orderTwoModified.setOrderNumber("W001");

        // Act and Assert
        assertThrows(DuplicatedOrderException.class, () -> {
            editorService.updateOrder(orderTwoModified.getId(), orderTwoModified, authorizationHeader);
        });

    }

    @Test
    public void when_DeleteOrder_Then_Success() throws Exception {
        // Arrange
        String authorizationHeader = this.createAuthorizationHeaderFrom("editor", "editor");
        OrderTO existingOrderTO = this.createCompleteOrderTO("W001");
        existingOrderTO = this.editorService.addOrder(existingOrderTO, authorizationHeader);

        Long orderId = existingOrderTO.getId(); // Get the ID of the saved order

        // Act
        editorService.deleteOrder(orderId); // Call the delete method

        assertFalse(orderRepository.existsById(orderId));
    }

    @Test
    public void when_DeleteOrder_WithNullOrderId_Then_ThrowsMissingOrderIdException() throws Exception{
        // Act and Assert
        assertThrows(MissingOrderIdException.class, () -> {
            editorService.deleteOrder(null);
        });
    }

    @Test
    public void when_DeleteOrder_WithNonExistentOrderId_Then_ThrowsOrderNotFoundException() throws Exception {
        // Arrange
        Long nonExistentOrderId = 999L; // Use an ID that is known not to exist

        // Act and Assert
        assertThrows(OrderNotFoundException.class, () -> {
            editorService.deleteOrder(nonExistentOrderId); // Attempt to delete a non-existent order
        });
    }

    @Test
    public void when_GetOrder_Then_Success() throws Exception {
        String authorizationHeader = this.createAuthorizationHeaderFrom("editor", "editor");
        OrderTO existingOrderTO = this.createCompleteOrderTO("W001");
        existingOrderTO = this.editorService.addOrder(existingOrderTO, authorizationHeader);
        Long orderId = existingOrderTO.getId();

        OrderTO fetchedOrder = this.editorService.getOrder(orderId);
        this.assertOrdersEqual(fetchedOrder, existingOrderTO);
    }

    @Test
    public void when_GetOrder_WithNonExistentOrderId_Then_ThrowsOrderNotFoundException() throws Exception {
        Long nonExistentOrderId = 999L; // Use an ID that is known not to exist

        // Act and Assert
        assertThrows(OrderNotFoundException.class, () -> {
            editorService.getOrder(nonExistentOrderId); // Attempt to delete a non-existent order
        });
    }

    @Test
    public void when_GetOrder_WithNullOrderId_Then_ThrowsMissingOrderIdException() throws Exception{
        assertThrows(MissingOrderIdException.class, () -> {
            editorService.getOrder(null); // Attempt to delete a non-existent order
        });
    }

    @Test
    public void when_GetOrders_Then_Success() throws Exception {
        // Arrange
        String authorizationHeader = this.createAuthorizationHeaderFrom("editor", "editor");

        // Create and save orders
        Long orderOneId =  this.editorService.addOrder(this.createCompleteOrderTO("W0001"), authorizationHeader).getId();
        Long orderTwoId =  this.editorService.addOrder(this.createCompleteOrderTO("W0002"), authorizationHeader).getId();
        Long orderThreeId =  this.editorService.addOrder(this.createCompleteOrderTO("W0003"), authorizationHeader).getId();
        Long orderFourId =  this.editorService.addOrder(this.createCompleteOrderTO("W0004"), authorizationHeader).getId();

        OrderTO orderOne = this.editorService.getOrder(orderOneId);
        OrderTO orderTwo = this.editorService.getOrder(orderTwoId);
        OrderTO orderThree = this.editorService.getOrder(orderThreeId);
        OrderTO orderFour = this.editorService.getOrder(orderFourId);

        // Act
        List<OrderTO> allOrderTOs = this.editorService.getAllOrders();

        // Assert
        assertEquals(4, allOrderTOs.size()); // Ensure we have 4 orders returned

        // Compare each order using the helper method
        assertOrdersEqual(orderOne, allOrderTOs.get(0));
        assertOrdersEqual(orderTwo, allOrderTOs.get(1));
        assertOrdersEqual(orderThree, allOrderTOs.get(2));
        assertOrdersEqual(orderFour, allOrderTOs.get(3));
    }

    /**
     * Create a JWT from username and password.
     */
    private String createAuthorizationHeaderFrom(String username, String password) throws Exception {
        LoginUserRequestTO request = new LoginUserRequestTO();
        request.setUsername(username);
        request.setPassword(password);

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return "Bearer " + objectMapper.readTree(response).get("token").asText();
    }

    /**
     * Create a fully populated OrderTO object for testing.
     */
    private OrderTO createCompleteOrderTO(String orderNumber) {
        OrderTO orderTO = new OrderTO();
        orderTO.setOrderNumber(orderNumber);
        orderTO.setName("AAA");
        orderTO.setDescription("Aspirin max");

        // Creating Workflow 1: Change Over
        WorkflowTO changeOverWorkflow = new WorkflowTO();
        changeOverWorkflow.setName("Change Over");
        changeOverWorkflow.setDescription("Transition machine settings for a new product batch");

        // Creating Task 1 for Change Over: Cleaning
        TaskTO cleaningTask = new TaskTO();
        cleaningTask.setName("Cleaning");
        cleaningTask.setDescription("Clean machine surfaces to maintain hygiene standards");

        cleaningTask.setItems(Arrays.asList(
                new ItemTO("Remove Tablets", "Safely remove any remaining tablets from the machine", 25),
                new ItemTO("Remove Leaflets", "Clear out any unused leaflets from previous production", 10),
                new ItemTO("Remove Cartons", "Remove any leftover cartons from the machine to prevent mix-ups", 20)
        ));

        // Creating Task 2 for Change Over: Mounting
        TaskTO mountingTask = new TaskTO();
        mountingTask.setName("Mounting");
        mountingTask.setDescription("Mounting");

        mountingTask.setItems(Arrays.asList(
                new ItemTO("Check Heating Plates", "Ensure heating plates are properly functioning and aligned", 10),
                new ItemTO("Change Forming Layout", "Adjust the forming layout to match the new product requirements", 60),
                new ItemTO("Change Sealing Roll", "Replace or adjust the sealing roll for the new product", 45),
                new ItemTO("Set manual parameters", "Update machine settings according to product specifications", null)
        ));

        // Adding Tasks to Change Over Workflow
        changeOverWorkflow.setTasks(Arrays.asList(cleaningTask, mountingTask));

        // Creating Workflow 2: Commissioning
        WorkflowTO commissioningWorkflow = new WorkflowTO();
        commissioningWorkflow.setName("Commissioning");
        commissioningWorkflow.setDescription("Prepare the machine for production");

        // Creating Task 1 for Commissioning: Thread in forming film
        TaskTO threadFilmTask = new TaskTO();
        threadFilmTask.setName("Thread in forming film");
        threadFilmTask.setDescription("Feed the forming film into the machine");

        threadFilmTask.setItems(Arrays.asList(
                new ItemTO("Thread forming film to unwinding", "Guide the forming film to the unwinding section", 40),
                new ItemTO("Activate 'manual unwinding'", "Start manual unwinding through the HMI", 1),
                new ItemTO("Thread forming film to filling section", "Direct the forming film to the filling section", 50)
        ));

        // Adding Tasks to Commissioning Workflow
        commissioningWorkflow.setTasks(Arrays.asList(threadFilmTask));

        // Adding both workflows to the Order
        orderTO.setWorkflows(Arrays.asList(changeOverWorkflow, commissioningWorkflow));

        return orderTO;
    }

    /**
     * Asserts that two OrderTO objects are equal, including all nested properties.
     */
    private void assertOrdersEqual(OrderTO expected, OrderTO actual) {

        if(expected.getId() != null && actual.getId() != null) {
            assertEquals(expected.getId(), actual.getId());
        }

        assertEquals(expected.getOrderNumber(), actual.getOrderNumber());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());

        // Compare workflows
        assertEquals(expected.getWorkflows().size(), actual.getWorkflows().size());
        for (int i = 0; i < expected.getWorkflows().size(); i++) {
            assertWorkflowsEqual(expected.getWorkflows().get(i), actual.getWorkflows().get(i));
        }
    }

    /**
     * Asserts that two WorkflowTO objects are equal, including all nested tasks.
     */
    private void assertWorkflowsEqual(WorkflowTO expected, WorkflowTO actual) {

        if(expected.getId() != null && actual.getId() != null) {
            assertEquals(expected.getId(), actual.getId());
        }

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());

        // Compare tasks
        assertEquals(expected.getTasks().size(), actual.getTasks().size());
        for (int i = 0; i < expected.getTasks().size(); i++) {
            assertTasksEqual(expected.getTasks().get(i), actual.getTasks().get(i));
        }
    }

    /**
     * Asserts that two TaskTO objects are equal, including all nested items.
     */
    private void assertTasksEqual(TaskTO expected, TaskTO actual) {

        if(expected.getId() != null && actual.getId() != null) {
            assertEquals(expected.getId(), actual.getId());
        }

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());

        // Compare items
        assertEquals(expected.getItems().size(), actual.getItems().size());
        for (int i = 0; i < expected.getItems().size(); i++) {
            assertItemsEqual(expected.getItems().get(i), actual.getItems().get(i));
        }
    }

    /**
     * Asserts that two ItemTO objects are equal.
     */
    private void assertItemsEqual(ItemTO expected, ItemTO actual) {

        if(expected.getId() != null && actual.getId() != null) {
            assertEquals(expected.getId(), actual.getId());
        }

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getTimeRequired(), actual.getTimeRequired());
    }
}
