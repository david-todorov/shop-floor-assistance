package com.shopfloor.backend.service;

import com.shopfloor.backend.api.transferobjects.editors.*;
import com.shopfloor.backend.database.repositories.EquipmentRepository;
import com.shopfloor.backend.database.repositories.OrderRepository;
import com.shopfloor.backend.database.repositories.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private ProductHelper productHelper;

    @Autowired
    private EquipmentHelper equipmentHelper;

    @AfterEach
    public void tearDown() {

        orderRepository.deleteAll();
        productRepository.deleteAll();
        equipmentRepository.deleteAll();
    }

    /**
     *ORDERS
     */
    @Test
    public void when_GetEditorOrders_Then_OK() throws Exception {

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        this.populateProductAndEquipment();


        EditorOrderTO orderOne = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        EditorOrderTO orderTwo = this.orderHelper.buildCompleteEditorOrderTO("W0002");
        EditorOrderTO orderThree = this.orderHelper.buildCompleteEditorOrderTO("W0003");
        EditorOrderTO orderFour = this.orderHelper.buildCompleteEditorOrderTO("W0004");
        EditorOrderTO orderFive = this.orderHelper.buildCompleteEditorOrderTO("W0005");


        this.assignProductAndEquipmentFor(orderOne);
        this.assignProductAndEquipmentFor(orderTwo);
        this.assignProductAndEquipmentFor(orderThree);
        this.assignProductAndEquipmentFor(orderFour);
        this.assignProductAndEquipmentFor(orderFive);

        orderOne = this.apiHelper.createEditorOrderPOST(orderOne, authorizationHeader, 201);
        orderTwo = this.apiHelper.createEditorOrderPOST(orderTwo, authorizationHeader, 201);
        orderThree = this.apiHelper.createEditorOrderPOST(orderThree, authorizationHeader, 201);
        orderFour = this.apiHelper.createEditorOrderPOST(orderFour, authorizationHeader, 201);
        orderFive = this.apiHelper.createEditorOrderPOST(orderFive, authorizationHeader, 201);


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
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        this.populateProductAndEquipment();
        EditorOrderTO newOrder = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(newOrder);


        //Make POST request and save the response
        EditorOrderTO response = apiHelper.createEditorOrderPOST(newOrder, authorizationHeader, 201);


        // Verify the order is created by asserting the created order matches the new order
        EditorOrderTO actual = apiHelper.getEditorOrderGET(response.getId(), authorizationHeader, 200);
        orderHelper.assertEditorOrdersEqual(actual, response);
        equipmentHelper.assertEditorEquipmentsListEqual(response.getEquipment(), actual.getEquipment());
    }

    @Test
    public void when_AddOrder_WithNegativeTimeRequired_Then_BadRequest() throws Exception {

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        this.populateProductAndEquipment();

        //Creating new order with negative time required
        EditorOrderTO oderWithNegativeTimeRequired = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(oderWithNegativeTimeRequired);
        oderWithNegativeTimeRequired.getWorkflows().get(0).getTasks().get(0).getItems().get(0).setTimeRequired(-1);

        apiHelper.createEditorOrderPOST(oderWithNegativeTimeRequired, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithExistingOrderNumber_Then_Conflict() throws Exception {
        this.populateProductAndEquipment();

        // Create a fully populated EditorOrderTO object for testing
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorOrderTO existingOrder = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(existingOrder);
        apiHelper.createEditorOrderPOST(existingOrder, authorizationHeader, 201);

        //Create new order with order number which already exists
        EditorOrderTO newOrderWithSameOrderNumber = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(newOrderWithSameOrderNumber);

        //Make Post request and expect conflict
        apiHelper.createEditorOrderPOST(newOrderWithSameOrderNumber, authorizationHeader, 409);
    }

    @Test
    public void when_AddOrder_WithNullOrder_Then_BadRequest() throws Exception {

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        // Create an EditorOrderTO object with a null order
        EditorOrderTO newNullOrder = null;

        // Perform the POST request with null as order
        apiHelper.createEditorOrderPOST(newNullOrder, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullOrderNumber_Then_BadRequest() throws Exception {

        this.populateProductAndEquipment();

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        // Create an EditorOrderTO object with a null order number
        EditorOrderTO newOrderWithNullNumber = this.orderHelper.buildCompleteEditorOrderTO(null);
        this.assignProductAndEquipmentFor(newOrderWithNullNumber);

        //Make Post request with order with null order number
        apiHelper.createEditorOrderPOST(newOrderWithNullNumber, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithEmptyOrderNumber_Then_BadRequest() throws Exception {

        this.populateProductAndEquipment();

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        // Create an EditorOrderTO object with empty order number
        EditorOrderTO newOrderWithNullNumber = this.orderHelper.buildCompleteEditorOrderTO("");
        this.assignProductAndEquipmentFor(newOrderWithNullNumber);

        //Make Post request with order with null order number
        apiHelper.createEditorOrderPOST(newOrderWithNullNumber, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullOrderName_Then_BadRequest() throws Exception {

        this.populateProductAndEquipment();

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        // Create an EditorOrderTO object with empty order number
        EditorOrderTO newOrderWithNullName = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithNullName.setName(null);
        this.assignProductAndEquipmentFor(newOrderWithNullName);


        //Make Post request with order with null order number
        apiHelper.createEditorOrderPOST(newOrderWithNullName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithEmptyOrderName_Then_BadRequest() throws Exception {

        this.populateProductAndEquipment();

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        // Create an EditorOrderTO object with empty name
        EditorOrderTO newOrderWithEmptyName = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithEmptyName.setName("");
        this.assignProductAndEquipmentFor(newOrderWithEmptyName);



        //Make Post request with order with null order name
        apiHelper.createEditorOrderPOST(newOrderWithEmptyName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullWorkflowName_Then_BadRequest() throws Exception {


        this.populateProductAndEquipment();

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        // Create an EditorOrderTO object with empty name
        EditorOrderTO newOrderWithNullWorkflowName = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithNullWorkflowName.getWorkflows().get(0).setName(null);
        this.assignProductAndEquipmentFor(newOrderWithNullWorkflowName);



        //Make Post request with order with null order name
        apiHelper.createEditorOrderPOST(newOrderWithNullWorkflowName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithEmptyWorkflowName_Then_BadRequest() throws Exception {

        this.populateProductAndEquipment();

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        // Create an EditorOrderTO object with empty name
        EditorOrderTO newOrderWithEmptyWorkflowName = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithEmptyWorkflowName.getWorkflows().get(0).setName("");
        this.assignProductAndEquipmentFor(newOrderWithEmptyWorkflowName);



        //Make Post request with order with null order name
        apiHelper.createEditorOrderPOST(newOrderWithEmptyWorkflowName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullTaskName_Then_BadRequest() throws Exception {


        this.populateProductAndEquipment();

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        // Create an EditorOrderTO object with empty name
        EditorOrderTO newOrderWithNullTaskName = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithNullTaskName.getWorkflows().get(0).getTasks().get(0).setName(null);
        this.assignProductAndEquipmentFor(newOrderWithNullTaskName);



        //Make Post request with order with null order name
        apiHelper.createEditorOrderPOST(newOrderWithNullTaskName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithEmptyTaskName_Then_BadRequest() throws Exception {

        this.populateProductAndEquipment();

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        // Create an EditorOrderTO object with empty name
        EditorOrderTO newOrderWithEmptyTaskName = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithEmptyTaskName.getWorkflows().get(0).getTasks().get(0).setName("");
        this.assignProductAndEquipmentFor(newOrderWithEmptyTaskName);



        //Make Post request with order with null order name
        apiHelper.createEditorOrderPOST(newOrderWithEmptyTaskName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullItemName_Then_BadRequest() throws Exception {

        this.populateProductAndEquipment();

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        // Create an EditorOrderTO object with empty name
        EditorOrderTO newOrderWithNullItemName = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithNullItemName.getWorkflows().get(0).getTasks().get(0).getItems().get(0).setName(null);
        this.assignProductAndEquipmentFor(newOrderWithNullItemName);



        //Make Post request with order with null order name
        apiHelper.createEditorOrderPOST(newOrderWithNullItemName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithEmptyItemName_Then_BadRequest() throws Exception {

        this.populateProductAndEquipment();

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        // Create an EditorOrderTO object with empty name
        EditorOrderTO newOrderWithEmptyItemName = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithEmptyItemName.getWorkflows().get(0).getTasks().get(0).getItems().get(0).setName("");
        this.assignProductAndEquipmentFor(newOrderWithEmptyItemName);



        //Make Post request with order with null order name
        apiHelper.createEditorOrderPOST(newOrderWithEmptyItemName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullWorklowList_Then_BadRequest() throws Exception {

        this.populateProductAndEquipment();

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        // Create an EditorOrderTO object with empty name
        EditorOrderTO newOrderWithNullWorklowList = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithNullWorklowList.setWorkflows(null);
        this.assignProductAndEquipmentFor(newOrderWithNullWorklowList);



        //Make Post request with order with null order name
        apiHelper.createEditorOrderPOST(newOrderWithNullWorklowList, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullWorklowEntity_Then_BadRequest() throws Exception {

        this.populateProductAndEquipment();

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        // Create an EditorOrderTO object with empty name
        EditorOrderTO newOrderWithNullWorklowEntity = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithNullWorklowEntity.getWorkflows().add(null);
        this.assignProductAndEquipmentFor(newOrderWithNullWorklowEntity);



        //Make Post request with order with null order name
        apiHelper.createEditorOrderPOST(newOrderWithNullWorklowEntity, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullTaskEntity_Then_BadRequest() throws Exception {

        this.populateProductAndEquipment();

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        // Create an EditorOrderTO object with empty name
        EditorOrderTO newOrderWithNullTaskEntity = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithNullTaskEntity.getWorkflows().get(0).getTasks().add(null);
        this.assignProductAndEquipmentFor(newOrderWithNullTaskEntity);



        //Make Post request with order with null order name
        apiHelper.createEditorOrderPOST(newOrderWithNullTaskEntity, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullItemEntity_Then_BadRequest() throws Exception {

        this.populateProductAndEquipment();

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        // Create an EditorOrderTO object with empty name
        EditorOrderTO newOrderWithNullItemEntity = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrderWithNullItemEntity.getWorkflows().get(0).getTasks().get(0).getItems().add(null);
        this.assignProductAndEquipmentFor(newOrderWithNullItemEntity);



        //Make Post request with order with null order name
        apiHelper.createEditorOrderPOST(newOrderWithNullItemEntity, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_Then_OK() throws Exception {

        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        //Preparing existing Order
        EditorOrderTO expected = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(expected);
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
        equipmentHelper.assertEditorEquipmentsListEqual(actual.getEquipment(), actual.getEquipment());
    }

    @Test
    public void when_UpdateOrderWith_AddNewWorkflow_Then_OK() throws Exception {

        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        //Preparing existing Order
        EditorOrderTO expected = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(expected);
        expected = apiHelper.createEditorOrderPOST(expected, authorizationHeader, 201);


        //Changing order completely from user perspective
        this.alteringOrderCompletely(expected);
        // Creating Workflow 1: Change Over
        EditorWorkflowTO changeOverWorkflow = new EditorWorkflowTO();
        changeOverWorkflow.setName("New Workflow");
        changeOverWorkflow.setDescription("Description for new Workflow");
        // Creating Task 1 for Change Over: Cleaning
        EditorTaskTO cleaningTask = new EditorTaskTO();
        cleaningTask.setName("New Task");
        cleaningTask.setDescription("Description for new task");
        cleaningTask.setItems(new ArrayList<>(Arrays.asList(new EditorItemTO("New Item 1", "Safely remove any remaining tablets from the machine", 25), new EditorItemTO("New Item 1", "Clear out any unused leaflets from previous production", 10), new EditorItemTO("New Item 1", "Remove any leftover cartons from the machine to prevent mix-ups", 20))));
        // Creating Task 2 for Change Over: Mounting
        EditorTaskTO mountingTask = new EditorTaskTO();
        mountingTask.setName("Mounting");
        mountingTask.setDescription("Mounting");
        mountingTask.setItems(new ArrayList<>(Arrays.asList(new EditorItemTO("New Item 1", "Ensure heating plates are properly functioning and aligned", 10), new EditorItemTO("New Item 1", "Adjust the forming layout to match the new product requirements", 60), new EditorItemTO("New Item 1", "Replace or adjust the sealing roll for the new product", 45), new EditorItemTO("New Item 1", "Update machine settings according to product specifications", null))));
        expected.getWorkflows().add(changeOverWorkflow);


        //Updating the order
        Long orderId = expected.getId();
        expected = apiHelper.updateEditorOrderPUT(orderId, expected, authorizationHeader, 200);

        //Getting the actual updated order
        EditorOrderTO actual = apiHelper.getEditorOrderGET(orderId, authorizationHeader, 200);

        // Comparing
        orderHelper.assertEditorOrdersEqual(actual, expected);
        equipmentHelper.assertEditorEquipmentsListEqual(actual.getEquipment(), actual.getEquipment());
    }

    @Test
    public void when_UpdateOrderWith_RemoveNewWorkflow_Then_OK() throws Exception {

        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        //Preparing existing Order
        EditorOrderTO expected = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(expected);
        expected = apiHelper.createEditorOrderPOST(expected, authorizationHeader, 201);

        expected.getWorkflows().remove(0);


        //Updating the order
        Long orderId = expected.getId();
        expected = apiHelper.updateEditorOrderPUT(orderId, expected, authorizationHeader, 200);

        //Getting the actual updated order
        EditorOrderTO actual = apiHelper.getEditorOrderGET(orderId, authorizationHeader, 200);

        // Comparing
        orderHelper.assertEditorOrdersEqual(actual, expected);
        equipmentHelper.assertEditorEquipmentsListEqual(actual.getEquipment(), actual.getEquipment());
    }

    @Test
    public void when_UpdateOrderWith_AddNewTask_Then_OK() throws Exception {

        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        //Preparing existing Order
        EditorOrderTO expected = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(expected);
        expected = apiHelper.createEditorOrderPOST(expected, authorizationHeader, 201);


        //Changing order completely from user perspective
        this.alteringOrderCompletely(expected);

        // Creating Task 1 for Change Over: Cleaning
        EditorTaskTO cleaningTask = new EditorTaskTO();
        cleaningTask.setName("New Task");
        cleaningTask.setDescription("Description for new task");
        cleaningTask.setItems(new ArrayList<>(Arrays.asList(new EditorItemTO("New Item 1", "Safely remove any remaining tablets from the machine", 25), new EditorItemTO("New Item 1", "Clear out any unused leaflets from previous production", 10), new EditorItemTO("New Item 1", "Remove any leftover cartons from the machine to prevent mix-ups", 20))));
        // Creating Task 2 for Change Over: Mounting
        EditorTaskTO mountingTask = new EditorTaskTO();
        mountingTask.setName("Mounting");
        mountingTask.setDescription("Mounting");
        mountingTask.setItems(new ArrayList<>(Arrays.asList(new EditorItemTO("New Item 1", "Ensure heating plates are properly functioning and aligned", 10), new EditorItemTO("New Item 1", "Adjust the forming layout to match the new product requirements", 60), new EditorItemTO("New Item 1", "Replace or adjust the sealing roll for the new product", 45), new EditorItemTO("New Item 1", "Update machine settings according to product specifications", null))));
        expected.getWorkflows().get(0).getTasks().add(cleaningTask);
        expected.getWorkflows().get(0).getTasks().add(mountingTask);


        //Updating the order
        Long orderId = expected.getId();
        expected = apiHelper.updateEditorOrderPUT(orderId, expected, authorizationHeader, 200);

        //Getting the actual updated order
        EditorOrderTO actual = apiHelper.getEditorOrderGET(orderId, authorizationHeader, 200);

        // Comparing
        orderHelper.assertEditorOrdersEqual(actual, expected);
        equipmentHelper.assertEditorEquipmentsListEqual(actual.getEquipment(), actual.getEquipment());
    }

    @Test
    public void when_UpdateOrderWith_RemoveTask_Then_OK() throws Exception {

        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        //Preparing existing Order
        EditorOrderTO expected = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(expected);
        expected = apiHelper.createEditorOrderPOST(expected, authorizationHeader, 201);


        //Changing order completely from user perspective
        this.alteringOrderCompletely(expected);

        expected.getWorkflows().get(0).getTasks().remove(0);


        //Updating the order
        Long orderId = expected.getId();
        expected = apiHelper.updateEditorOrderPUT(orderId, expected, authorizationHeader, 200);

        //Getting the actual updated order
        EditorOrderTO actual = apiHelper.getEditorOrderGET(orderId, authorizationHeader, 200);

        // Comparing
        orderHelper.assertEditorOrdersEqual(actual, expected);
        equipmentHelper.assertEditorEquipmentsListEqual(actual.getEquipment(), actual.getEquipment());
    }

    @Test
    public void when_UpdateOrderWith_AddNewItemTask_Then_OK() throws Exception {

        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        //Preparing existing Order
        EditorOrderTO expected = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(expected);
        expected = apiHelper.createEditorOrderPOST(expected, authorizationHeader, 201);


        //Changing order completely from user perspective
        this.alteringOrderCompletely(expected);

        expected.getWorkflows().get(0).getTasks().get(0).getItems().add(new EditorItemTO("New Item 1", "Ensure heating plates are properly functioning and aligned", 10));


        //Updating the order
        Long orderId = expected.getId();
        expected = apiHelper.updateEditorOrderPUT(orderId, expected, authorizationHeader, 200);

        //Getting the actual updated order
        EditorOrderTO actual = apiHelper.getEditorOrderGET(orderId, authorizationHeader, 200);

        // Comparing
        orderHelper.assertEditorOrdersEqual(actual, expected);
        equipmentHelper.assertEditorEquipmentsListEqual(actual.getEquipment(), actual.getEquipment());
    }

    @Test
    public void when_UpdateOrderWith_RemoveItemTask_Then_OK() throws Exception {

        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        //Preparing existing Order
        EditorOrderTO expected = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(expected);
        expected = apiHelper.createEditorOrderPOST(expected, authorizationHeader, 201);


        //Changing order completely from user perspective
        this.alteringOrderCompletely(expected);

        expected.getWorkflows().get(0).getTasks().get(0).getItems().remove(0);


        //Updating the order
        Long orderId = expected.getId();
        expected = apiHelper.updateEditorOrderPUT(orderId, expected, authorizationHeader, 200);

        //Getting the actual updated order
        EditorOrderTO actual = apiHelper.getEditorOrderGET(orderId, authorizationHeader, 200);

        // Comparing
        orderHelper.assertEditorOrdersEqual(actual, expected);
        equipmentHelper.assertEditorEquipmentsListEqual(actual.getEquipment(), actual.getEquipment());
    }

    @Test
    public void when_UpdateOrder_WithNegativeTimeRequired_Then_BadRequest() throws Exception {

        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        //Preparing existing Order
        EditorOrderTO updatedOrderWithNegativeTimeRequired = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(updatedOrderWithNegativeTimeRequired);
        updatedOrderWithNegativeTimeRequired = apiHelper.createEditorOrderPOST(updatedOrderWithNegativeTimeRequired, authorizationHeader, 201);

        //Setting with negative time
        updatedOrderWithNegativeTimeRequired.getWorkflows().get(0).getTasks().get(0).getItems().get(0).setTimeRequired(-1);


        //Expecting exception
        apiHelper.updateEditorOrderPUT(updatedOrderWithNegativeTimeRequired.getId(), updatedOrderWithNegativeTimeRequired, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullOrderNumber_Then_BadRequest() throws Exception {

        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        //Preparing existing Order
        EditorOrderTO updatedOrderWithNullOrderNumber = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(updatedOrderWithNullOrderNumber);
        updatedOrderWithNullOrderNumber = apiHelper.createEditorOrderPOST(updatedOrderWithNullOrderNumber, authorizationHeader, 201);

        //Setting with null
        updatedOrderWithNullOrderNumber.setOrderNumber(null);


        //Expecting exception
        apiHelper.updateEditorOrderPUT(updatedOrderWithNullOrderNumber.getId(), updatedOrderWithNullOrderNumber, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullName_Then_BadRequest() throws Exception {

        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        //Preparing existing Order
        EditorOrderTO updatedOrderWithNullName = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(updatedOrderWithNullName);
        updatedOrderWithNullName = apiHelper.createEditorOrderPOST(updatedOrderWithNullName, authorizationHeader, 201);

        //Setting with null
        updatedOrderWithNullName.setName(null);


        //Expecting exception
        apiHelper.updateEditorOrderPUT(updatedOrderWithNullName.getId(), updatedOrderWithNullName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullWorkflowName_Then_BadRequest() throws Exception {


        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        //Preparing existing Order
        EditorOrderTO updatedOrderWithNullWorkflowName = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(updatedOrderWithNullWorkflowName);
        updatedOrderWithNullWorkflowName = apiHelper.createEditorOrderPOST(updatedOrderWithNullWorkflowName, authorizationHeader, 201);

        //Setting with null
        updatedOrderWithNullWorkflowName.getWorkflows().get(0).setName(null);


        //Expecting exception
        apiHelper.updateEditorOrderPUT(updatedOrderWithNullWorkflowName.getId(), updatedOrderWithNullWorkflowName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullTaskName_Then_BadRequest() throws Exception {

        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        //Preparing existing Order
        EditorOrderTO updatedOrderWithNullTaskName = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(updatedOrderWithNullTaskName);
        updatedOrderWithNullTaskName = apiHelper.createEditorOrderPOST(updatedOrderWithNullTaskName, authorizationHeader, 201);

        //Setting with null
        updatedOrderWithNullTaskName.getWorkflows().get(0).getTasks().get(0).setName(null);


        //Expecting exception
        apiHelper.updateEditorOrderPUT(updatedOrderWithNullTaskName.getId(), updatedOrderWithNullTaskName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullItemName_Then_BadRequest() throws Exception {

        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        //Preparing existing Order
        EditorOrderTO updatedOrderWithNullItemName = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(updatedOrderWithNullItemName);
        updatedOrderWithNullItemName = apiHelper.createEditorOrderPOST(updatedOrderWithNullItemName, authorizationHeader, 201);

        //Setting with null
        updatedOrderWithNullItemName.getWorkflows().get(0).getTasks().get(0).getItems().get(0).setName(null);


        //Expecting exception
        apiHelper.updateEditorOrderPUT(updatedOrderWithNullItemName.getId(), updatedOrderWithNullItemName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullWorkflowList_Then_BadRequest() throws Exception {

        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        //Preparing existing Order
        EditorOrderTO updatedOrderWithNullWorkflowList = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(updatedOrderWithNullWorkflowList);
        updatedOrderWithNullWorkflowList = apiHelper.createEditorOrderPOST(updatedOrderWithNullWorkflowList, authorizationHeader, 201);

        //Setting with null
        updatedOrderWithNullWorkflowList.setWorkflows(null);


        //Expecting exception
        apiHelper.updateEditorOrderPUT(updatedOrderWithNullWorkflowList.getId(), updatedOrderWithNullWorkflowList, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullWorkflowEntity_Then_BadRequest() throws Exception {

        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        //Preparing existing Order
        EditorOrderTO updatedOrderWithNullWorkflowEntity = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(updatedOrderWithNullWorkflowEntity);
        updatedOrderWithNullWorkflowEntity = apiHelper.createEditorOrderPOST(updatedOrderWithNullWorkflowEntity, authorizationHeader, 201);

        //Setting with null
        updatedOrderWithNullWorkflowEntity.getWorkflows().add(null);


        //Expecting exception
        apiHelper.updateEditorOrderPUT(updatedOrderWithNullWorkflowEntity.getId(), updatedOrderWithNullWorkflowEntity, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullTaskList_Then_BadRequest() throws Exception {

        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        //Preparing existing Order
        EditorOrderTO updatedOrderWithNullTaskList = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(updatedOrderWithNullTaskList);
        updatedOrderWithNullTaskList = apiHelper.createEditorOrderPOST(updatedOrderWithNullTaskList, authorizationHeader, 201);

        //Setting with null
        updatedOrderWithNullTaskList.getWorkflows().get(0).setTasks(null);


        //Expecting exception
        apiHelper.updateEditorOrderPUT(updatedOrderWithNullTaskList.getId(), updatedOrderWithNullTaskList, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullTaskEntity_Then_BadRequest() throws Exception {
        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        //Preparing existing Order
        EditorOrderTO updatedOrderWithNullTaskEntity = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(updatedOrderWithNullTaskEntity);
        updatedOrderWithNullTaskEntity = apiHelper.createEditorOrderPOST(updatedOrderWithNullTaskEntity, authorizationHeader, 201);

        //Setting with null
        updatedOrderWithNullTaskEntity.getWorkflows().get(0).getTasks().add(null);


        //Expecting exception
        apiHelper.updateEditorOrderPUT(updatedOrderWithNullTaskEntity.getId(), updatedOrderWithNullTaskEntity, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullItemEntity_Then_BadRequest() throws Exception {
        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        //Preparing existing Order
        EditorOrderTO updatedOrderWithNullItemEntity = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(updatedOrderWithNullItemEntity);
        updatedOrderWithNullItemEntity = apiHelper.createEditorOrderPOST(updatedOrderWithNullItemEntity, authorizationHeader, 201);

        //Setting with null
        updatedOrderWithNullItemEntity.getWorkflows().get(0).getTasks().get(0).getItems().add(null);


        //Expecting exception
        apiHelper.updateEditorOrderPUT(updatedOrderWithNullItemEntity.getId(), updatedOrderWithNullItemEntity, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNotExistingOrder_Then_NotFound() throws Exception {
        this.populateProductAndEquipment();
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        EditorOrderTO orderWithNonExistingId = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(orderWithNonExistingId);
        orderWithNonExistingId.setId(999L);

        apiHelper.updateEditorOrderPUT(orderWithNonExistingId.getId(), orderWithNonExistingId, authorizationHeader, 404);
    }

    @Test
    public void when_UpdateOrder_WithExistingOrderNumber_Then_Conflict() throws Exception {
        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        EditorOrderTO existingOrderNumber = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(existingOrderNumber);
        apiHelper.createEditorOrderPOST(existingOrderNumber, authorizationHeader, 201);


        EditorOrderTO toBeUpdated = this.orderHelper.buildCompleteEditorOrderTO("W0002");
        this.assignProductAndEquipmentFor(toBeUpdated);
        toBeUpdated = apiHelper.createEditorOrderPOST(toBeUpdated, authorizationHeader, 201);

        toBeUpdated.setOrderNumber("W0001");
        apiHelper.updateEditorOrderPUT(toBeUpdated.getId(), toBeUpdated, authorizationHeader, 409);
    }

    @Test
    public void when_UpdateOrder_WithValidProductAfterAndBeforeAndEquipment_Then_OK() throws Exception {
        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        ArrayList<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO toBeUpdated = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        toBeUpdated.setProductAfter(productAfter);
        toBeUpdated.setProductBefore(productBefore);
        toBeUpdated.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        toBeUpdated = this.apiHelper.createEditorOrderPOST(toBeUpdated, authorizationHeader, 201);
        alteringOrderCompletely(toBeUpdated);


        toBeUpdated = this.apiHelper.updateEditorOrderPUT(toBeUpdated.getId(), toBeUpdated, authorizationHeader, 200);
        EditorOrderTO actual = this.apiHelper.getEditorOrderGET(toBeUpdated.getId(), authorizationHeader, 200);
        this.orderHelper.assertEditorOrdersEqual(toBeUpdated, actual);
        equipmentHelper.assertEditorEquipmentsListEqual(actual.getEquipment(), toBeUpdated.getEquipment());
    }

    @Test
    public void when_Update_WithValidProductAfterAndNullProductBeforeAndEquipment_Then_OK() throws Exception {

        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        ArrayList<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO toBeUpdated = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        toBeUpdated.setProductAfter(productAfter);
        toBeUpdated.setProductBefore(productBefore);
        toBeUpdated.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        toBeUpdated = this.apiHelper.createEditorOrderPOST(toBeUpdated, authorizationHeader, 201);
        alteringOrderCompletely(toBeUpdated);

        toBeUpdated.setProductBefore(null);

        toBeUpdated = this.apiHelper.updateEditorOrderPUT(toBeUpdated.getId(), toBeUpdated, authorizationHeader, 200);
        EditorOrderTO actual = this.apiHelper.getEditorOrderGET(toBeUpdated.getId(), authorizationHeader, 200);
        this.orderHelper.assertEditorOrdersEqual(toBeUpdated, actual);
        equipmentHelper.assertEditorEquipmentsListEqual(actual.getEquipment(), toBeUpdated.getEquipment());
    }

    @Test
    public void when_UpdateOrder_WithNullProductAfterAndValidProductBeforeAndValidEquipment_Then_OK() throws Exception {

        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        ArrayList<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO toBeUpdated = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        toBeUpdated.setProductAfter(productAfter);
        toBeUpdated.setProductBefore(productBefore);
        toBeUpdated.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        toBeUpdated = this.apiHelper.createEditorOrderPOST(toBeUpdated, authorizationHeader, 201);
        alteringOrderCompletely(toBeUpdated);

        toBeUpdated.setProductAfter(null);

        toBeUpdated = this.apiHelper.updateEditorOrderPUT(toBeUpdated.getId(), toBeUpdated, authorizationHeader, 200);
    }

    @Test
    public void when_UpdateOrder_WithNonExistingProductAfter_Then_NotFound() throws Exception {

        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        ArrayList<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO toBeUpdated = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        toBeUpdated.setProductAfter(productAfter);
        toBeUpdated.setProductBefore(productBefore);
        toBeUpdated.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        toBeUpdated = this.apiHelper.createEditorOrderPOST(toBeUpdated, authorizationHeader, 201);
        alteringOrderCompletely(toBeUpdated);

        EditorProductTO nonExistingProduct = this.productHelper.buildCompleteEditorProductTO("Non Existing Product");
        nonExistingProduct.setId(999L);
        toBeUpdated.setProductAfter(nonExistingProduct);

        toBeUpdated = this.apiHelper.updateEditorOrderPUT(toBeUpdated.getId(), toBeUpdated, authorizationHeader, 404);

    }

    @Test
    public void when_UpdateOrder_WithNonExistingProductBefore_Then_NotFound() throws Exception {

        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        ArrayList<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO toBeUpdated = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        toBeUpdated.setProductAfter(productAfter);
        toBeUpdated.setProductBefore(productBefore);
        toBeUpdated.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        toBeUpdated = this.apiHelper.createEditorOrderPOST(toBeUpdated, authorizationHeader, 201);
        alteringOrderCompletely(toBeUpdated);

        EditorProductTO nonExistingProduct = this.productHelper.buildCompleteEditorProductTO("Non Existing Product");
        nonExistingProduct.setId(999L);
        toBeUpdated.setProductBefore(nonExistingProduct);

        toBeUpdated = this.apiHelper.updateEditorOrderPUT(toBeUpdated.getId(), toBeUpdated, authorizationHeader, 404);
    }

    @Test
    public void when_UpdateOrder_WithNonExistingProductBeforeAndProductAfter_Then_NotFound() throws Exception {

        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        ArrayList<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO toBeUpdated = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        toBeUpdated.setProductAfter(productAfter);
        toBeUpdated.setProductBefore(productBefore);
        toBeUpdated.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        toBeUpdated = this.apiHelper.createEditorOrderPOST(toBeUpdated, authorizationHeader, 201);
        alteringOrderCompletely(toBeUpdated);

        EditorProductTO nonExistingProduct = this.productHelper.buildCompleteEditorProductTO("Non Existing Product");
        nonExistingProduct.setId(999L);
        toBeUpdated.setProductBefore(nonExistingProduct);
        toBeUpdated.setProductAfter(nonExistingProduct);

        toBeUpdated = this.apiHelper.updateEditorOrderPUT(toBeUpdated.getId(), toBeUpdated, authorizationHeader, 404);
    }

    @Test
    public void when_UpdateOrder_WithNullEquipmentList_Then_BadRequest() throws Exception {


        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        ArrayList<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO toBeUpdated = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        toBeUpdated.setProductAfter(productAfter);
        toBeUpdated.setProductBefore(productBefore);
        toBeUpdated.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        toBeUpdated = this.apiHelper.createEditorOrderPOST(toBeUpdated, authorizationHeader, 201);
        alteringOrderCompletely(toBeUpdated);

        toBeUpdated.setEquipment(null);

        toBeUpdated = this.apiHelper.updateEditorOrderPUT(toBeUpdated.getId(), toBeUpdated, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithEmptyEquipmentList_Then_OK() throws Exception {
        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        ArrayList<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO toBeUpdated = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        toBeUpdated.setProductAfter(productAfter);
        toBeUpdated.setProductBefore(productBefore);
        toBeUpdated.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        toBeUpdated = this.apiHelper.createEditorOrderPOST(toBeUpdated, authorizationHeader, 201);
        alteringOrderCompletely(toBeUpdated);

        toBeUpdated.setEquipment(Collections.emptyList());

        toBeUpdated = this.apiHelper.updateEditorOrderPUT(toBeUpdated.getId(), toBeUpdated, authorizationHeader, 200);
        EditorOrderTO actual = this.apiHelper.getEditorOrderGET(toBeUpdated.getId(), authorizationHeader, 200);
        this.orderHelper.assertEditorOrdersEqual(toBeUpdated, actual);
        equipmentHelper.assertEditorEquipmentsListEqual(actual.getEquipment(), toBeUpdated.getEquipment());
    }

    @Test
    public void when_UpdateOrder_WithNullEquipmentEntity_Then_BadRequest() throws Exception {

        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        ArrayList<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO toBeUpdated = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        toBeUpdated.setProductAfter(productAfter);
        toBeUpdated.setProductBefore(productBefore);
        toBeUpdated.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        toBeUpdated = this.apiHelper.createEditorOrderPOST(toBeUpdated, authorizationHeader, 201);
        alteringOrderCompletely(toBeUpdated);

        toBeUpdated.getEquipment().add(null);

        toBeUpdated = this.apiHelper.updateEditorOrderPUT(toBeUpdated.getId(), toBeUpdated, authorizationHeader, 400);

    }

    @Test
    public void when_UpdateOrder_WithNonExistingEquipmentEntity_Then_NotFound() throws Exception {

        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        ArrayList<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO toBeUpdated = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        toBeUpdated.setProductAfter(productAfter);
        toBeUpdated.setProductBefore(productBefore);
        toBeUpdated.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        toBeUpdated = this.apiHelper.createEditorOrderPOST(toBeUpdated, authorizationHeader, 201);
        alteringOrderCompletely(toBeUpdated);

        EditorEquipmentTO nonExistingEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("NonExisting");
        nonExistingEquipment.setId(999L);
        toBeUpdated.getEquipment().add(nonExistingEquipment);

        toBeUpdated = this.apiHelper.updateEditorOrderPUT(toBeUpdated.getId(), toBeUpdated, authorizationHeader, 404);
    }

    @Test
    public void when_UpdateOrder_WithNewExistingProductAfter_Then_OK() throws Exception {
        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        ArrayList<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO toBeUpdated = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        toBeUpdated.setProductAfter(productAfter);
        toBeUpdated.setProductBefore(productBefore);
        toBeUpdated.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        toBeUpdated = this.apiHelper.createEditorOrderPOST(toBeUpdated, authorizationHeader, 201);
        alteringOrderCompletely(toBeUpdated);

        EditorProductTO newProductAfter = this.productHelper.buildCompleteEditorProductTO("New After");
        newProductAfter = this.saveProductInDatabase(newProductAfter);
        toBeUpdated.setProductAfter(newProductAfter);

        toBeUpdated = this.apiHelper.updateEditorOrderPUT(toBeUpdated.getId(), toBeUpdated, authorizationHeader, 200);
        EditorOrderTO actual = this.apiHelper.getEditorOrderGET(toBeUpdated.getId(), authorizationHeader, 200);
        this.orderHelper.assertEditorOrdersEqual(toBeUpdated, actual);
        equipmentHelper.assertEditorEquipmentsListEqual(actual.getEquipment(), toBeUpdated.getEquipment());
    }

    @Test
    public void when_UpdateOrder_WithNewExistingProductBefore_Then_OK() throws Exception {
        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        ArrayList<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO toBeUpdated = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        toBeUpdated.setProductAfter(productAfter);
        toBeUpdated.setProductBefore(productBefore);
        toBeUpdated.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        toBeUpdated = this.apiHelper.createEditorOrderPOST(toBeUpdated, authorizationHeader, 201);
        alteringOrderCompletely(toBeUpdated);

        EditorProductTO newProductBefore = this.productHelper.buildCompleteEditorProductTO("New Before");
        newProductBefore = this.saveProductInDatabase(newProductBefore);
        toBeUpdated.setProductAfter(newProductBefore);

        toBeUpdated = this.apiHelper.updateEditorOrderPUT(toBeUpdated.getId(), toBeUpdated, authorizationHeader, 200);
        EditorOrderTO actual = this.apiHelper.getEditorOrderGET(toBeUpdated.getId(), authorizationHeader, 200);
        this.orderHelper.assertEditorOrdersEqual(toBeUpdated, actual);
    }

    @Test
    public void when_DeleteOrder_WithNonExistingId_Then_NotFound() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        apiHelper.deleteEditorOrderDELETE(999L, authorizationHeader, 404);
    }

    @Test
    public void when_DeleteOrder_Then_NoContent() throws Exception {
        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        EditorOrderTO toBeDeleted = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(toBeDeleted);
        toBeDeleted = apiHelper.createEditorOrderPOST(toBeDeleted, authorizationHeader, 201);

        Long toDeleteId = toBeDeleted.getId();
        assertTrue(this.orderRepository.existsById(toDeleteId));

        apiHelper.deleteEditorOrderDELETE(toDeleteId, authorizationHeader, 204);

        assertFalse(orderRepository.existsById(toBeDeleted.getId()));
    }

    @Test
    public void when_DeleteOrder_TheReferencesWithProductAfterDeleted_Then_NoContent() throws Exception {

        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        ArrayList<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO toBeDeleted = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        toBeDeleted.setProductAfter(productAfter);
        toBeDeleted.setProductBefore(productBefore);
        toBeDeleted.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        toBeDeleted = this.apiHelper.createEditorOrderPOST(toBeDeleted, authorizationHeader, 201);
        Long orderId = toBeDeleted.getId();
        Long productAfterId = toBeDeleted.getProductAfter().getId();

        productAfter = this.apiHelper.getEditorProductGET(productAfterId, authorizationHeader, 200);
        assertTrue(productAfter.getOrdersAfter().stream()
                .anyMatch(order -> order.getId().equals(orderId)));

        apiHelper.deleteEditorOrderDELETE(toBeDeleted.getId(), authorizationHeader, 204);

        productAfter = this.apiHelper.getEditorProductGET(productAfterId, authorizationHeader, 200);
        assertTrue(productAfter.getOrdersAfter().stream()
                .noneMatch(order -> order.getId().equals(orderId)));

        assertFalse(orderRepository.existsById(orderId));
        assertTrue(productRepository.existsById(productAfterId));

    }

    @Test
    public void when_DeleteOrder_TheReferencesWithProductBeforeDeleted_Then_NoContent() throws Exception {

        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        ArrayList<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO toBeDeleted = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        toBeDeleted.setProductAfter(productAfter);
        toBeDeleted.setProductBefore(productBefore);
        toBeDeleted.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        toBeDeleted = this.apiHelper.createEditorOrderPOST(toBeDeleted, authorizationHeader, 201);
        Long orderId = toBeDeleted.getId();
        Long productBeforeId = toBeDeleted.getProductBefore().getId();

        productAfter = this.apiHelper.getEditorProductGET(productBeforeId, authorizationHeader, 200);
        assertTrue(productAfter.getOrdersBefore().stream()
                .anyMatch(order -> order.getId().equals(orderId)));

        apiHelper.deleteEditorOrderDELETE(toBeDeleted.getId(), authorizationHeader, 204);

        productAfter = this.apiHelper.getEditorProductGET(productBeforeId, authorizationHeader, 200);
        assertTrue(productAfter.getOrdersBefore().stream()
                .noneMatch(order -> order.getId().equals(orderId)));

        assertFalse(orderRepository.existsById(orderId));
        assertTrue(productRepository.existsById(productBeforeId));
    }

    @Test
    public void when_DeleteOrder_TheReferencesWithEquipmentDeleted_Then_NoContent() throws Exception {
        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        List<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO toBeDeleted = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        toBeDeleted.setProductAfter(productAfter);
        toBeDeleted.setProductBefore(productBefore);
        toBeDeleted.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        toBeDeleted = this.apiHelper.createEditorOrderPOST(toBeDeleted, authorizationHeader, 201);
        Long orderId = toBeDeleted.getId();

        equipments = this.apiHelper.getEditorAllEquipmentGET(authorizationHeader, 200);
        equipments.forEach(
                (equipment) -> assertTrue(equipment.getOrders().stream().anyMatch((order) -> order.getId().equals(orderId)))
        );

        apiHelper.deleteEditorOrderDELETE(toBeDeleted.getId(), authorizationHeader, 204);

        equipments = this.apiHelper.getEditorAllEquipmentGET(authorizationHeader, 200);
        equipments.forEach(
                (equipment) -> assertTrue(equipment.getOrders().stream().noneMatch((order) -> order.getId().equals(orderId)))
        );

        assertFalse(orderRepository.existsById(orderId));
        equipments.forEach(
                (equipment) -> assertTrue(this.equipmentRepository.existsById(equipment.getId()))
        );
    }

    @Test
    public void when_GetOrder_Then_OK() throws Exception {
        this.populateProductAndEquipment();
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");


        EditorOrderTO expected = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        this.assignProductAndEquipmentFor(expected);
        expected = apiHelper.createEditorOrderPOST(expected, authorizationHeader, 201);

        EditorOrderTO actual = apiHelper.getEditorOrderGET(expected.getId(), authorizationHeader, 200);

        orderHelper.assertEditorOrdersEqual(expected, actual);
    }

    @Test
    public void when_GetOrder_WithNonExistingId_Then_NotFound() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        apiHelper.getEditorOrderGET(999L, authorizationHeader, 404);
    }

    @Test
    public void when_AddOrder_WithValidProductAfterAndBeforeAndEquipment_Then_Created() throws Exception {
        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        List<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO newOrder = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrder.setProductAfter(productAfter);
        newOrder.setProductBefore(productBefore);
        newOrder.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        newOrder = this.apiHelper.createEditorOrderPOST(newOrder, authorizationHeader, 201);

        EditorOrderTO actual = apiHelper.getEditorOrderGET(newOrder.getId(), authorizationHeader, 200);

        this.orderHelper.assertEditorOrdersEqual(newOrder, actual);
        this.orderRepository.existsById(newOrder.getId());
        this.orderRepository.existsByOrderNumber(newOrder.getOrderNumber());
    }

    @Test
    public void when_AddOrder_WithValidProductAfterAndNullProductBeforeAndEquipment_Then_Created() throws Exception {

        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        List<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO newOrder = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrder.setProductAfter(productAfter);
        newOrder.setProductBefore(null);
        newOrder.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        newOrder = this.apiHelper.createEditorOrderPOST(newOrder, authorizationHeader, 201);

        EditorOrderTO actual = apiHelper.getEditorOrderGET(newOrder.getId(), authorizationHeader, 200);

        this.orderHelper.assertEditorOrdersEqual(newOrder, actual);
    }

    @Test
    public void when_AddOrder_WithNullProductAfterAndValidProductBeforeAndValidEquipment_Then_Created() throws Exception {

        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        List<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO newOrder = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrder.setProductAfter(null);
        newOrder.setProductBefore(productBefore);
        newOrder.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        newOrder = this.apiHelper.createEditorOrderPOST(newOrder, authorizationHeader, 201);
    }

    @Test
    public void when_AddOrder_WithNonExistingProductAfter_Then_NotFound() throws Exception {


        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        List<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO newOrder = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        EditorProductTO nonExistingProductAfter = this.productHelper.buildCompleteEditorProductTO("NonExistingProductAfter");
        nonExistingProductAfter.setId(999L);
        newOrder.setProductAfter(nonExistingProductAfter);
        newOrder.setProductBefore(productBefore);
        newOrder.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        newOrder = this.apiHelper.createEditorOrderPOST(newOrder, authorizationHeader, 404);
    }

    @Test
    public void when_AddOrder_WithNonExistingProductBefore_Then_NotFound() throws Exception {

        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        List<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO newOrder = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        EditorProductTO nonExistingProductBefore = this.productHelper.buildCompleteEditorProductTO("NonExistingProductBefore");
        nonExistingProductBefore.setId(999L);
        newOrder.setProductAfter(productBefore);
        newOrder.setProductBefore(nonExistingProductBefore);
        newOrder.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        newOrder = this.apiHelper.createEditorOrderPOST(newOrder, authorizationHeader, 404);
    }

    @Test
    public void when_AddOrder_WithNullEquipmentList_Then_BadRequest() throws Exception {
        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        List<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO newOrder = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrder.setProductAfter(productAfter);
        newOrder.setProductBefore(productBefore);
        newOrder.setEquipment(null);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        newOrder = this.apiHelper.createEditorOrderPOST(newOrder, authorizationHeader, 400);

    }

    @Test
    public void when_AddOrder_WithEmptyEquipmentList_Then_Created() throws Exception {

        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        List<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO newOrder = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrder.setProductAfter(productAfter);
        newOrder.setProductBefore(productBefore);
        newOrder.setEquipment(Collections.emptyList());

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        newOrder = this.apiHelper.createEditorOrderPOST(newOrder, authorizationHeader, 201);

        EditorOrderTO actual = apiHelper.getEditorOrderGET(newOrder.getId(), authorizationHeader, 200);

        this.orderHelper.assertEditorOrdersEqual(newOrder, actual);
    }

    @Test
    public void when_AddOrder_WithNullEquipmentEntity_Then_BadRequest() throws Exception {

        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        List<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);
        equipments.add(null);

        EditorOrderTO newOrder = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrder.setProductAfter(productAfter);
        newOrder.setProductBefore(productBefore);
        newOrder.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        newOrder = this.apiHelper.createEditorOrderPOST(newOrder, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNonExistingEquipmentEntity_Then_NotFound() throws Exception {

        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        List<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);
        EditorEquipmentTO nonExistingEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("NonExisting");
        nonExistingEquipment.setId(999L);
        equipments.add(nonExistingEquipment);

        EditorOrderTO newOrder = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrder.setProductAfter(productAfter);
        newOrder.setProductBefore(productBefore);
        newOrder.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        newOrder = this.apiHelper.createEditorOrderPOST(newOrder, authorizationHeader, 404);

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

    private void populateProductAndEquipment() throws Exception{
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        EditorProductTO P0001 = this.productHelper.buildCompleteEditorProductTO("P0001");
        EditorEquipmentTO E0001 = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0001");
        EditorEquipmentTO E0002 = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0002");
        EditorEquipmentTO E0003 = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0003");

        P0001 = this.apiHelper.createEditorProductPOST(P0001, authorizationHeader, 201 );
        E0001 = this.apiHelper.createEditorEquipmentPOST(E0001, authorizationHeader, 201 );
        E0002 = this.apiHelper.createEditorEquipmentPOST(E0002, authorizationHeader, 201 );
        E0003 = this.apiHelper.createEditorEquipmentPOST(E0003, authorizationHeader, 201 );

    }

    private void assignProductAndEquipmentFor(EditorOrderTO order) throws Exception{
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        order.setProductAfter(this.apiHelper.getEditorAllProductsGET(authorizationHeader, 200).get(0));
        order.setEquipment(this.apiHelper.getEditorAllEquipmentGET(authorizationHeader, 200));
    }

    private EditorProductTO saveProductInDatabase(EditorProductTO product) throws Exception{
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        return this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201 );
    }

    private EditorEquipmentTO saveEquipmentInDatabase(EditorEquipmentTO equipment) throws Exception{
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        return this.apiHelper.createEditorEquipmentPOST(equipment, authorizationHeader, 201 );
    }

    /**
     * PRODUCTS
     */

    @Test
    public void when_AddProduct_Then_Created() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO newProduct = this.productHelper.buildCompleteEditorProductTO("P0001");

        EditorProductTO response = this.apiHelper.createEditorProductPOST(newProduct, authorizationHeader, 201);
        EditorProductTO actual = this.apiHelper.getEditorProductGET(response.getId(), authorizationHeader, 200);

        this.productHelper.assertEditorProductsEqual(response, actual);
    }

    @Test
    public void when_AddProduct_WithNullProductNumber_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO productWithNullNumber = this.productHelper.buildCompleteEditorProductTO(null);

        this.apiHelper.createEditorProductPOST(productWithNullNumber, authorizationHeader, 400);
    }

    @Test
    public void when_AddProduct_WithEmptyProductNumber_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO productWithEmptyNumber = this.productHelper.buildCompleteEditorProductTO("");

        this.apiHelper.createEditorProductPOST(productWithEmptyNumber, authorizationHeader, 400);
    }

    @Test
    public void when_AddProduct_WithExistingProductNumber_Then_Conflict() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");
        this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201);

        EditorProductTO newProductWithSameNumber = this.productHelper.buildCompleteEditorProductTO("P0001");
        this.apiHelper.createEditorProductPOST(newProductWithSameNumber, authorizationHeader, 409);
    }

    @Test
    public void when_AddProduct_WithNullName_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO productWithNullName = this.productHelper.buildCompleteEditorProductTO("P0001");
        productWithNullName.setName(null);

        this.apiHelper.createEditorProductPOST(productWithNullName, authorizationHeader, 400);
    }

    @Test
    public void when_AddProduct_WithEmptyProductName_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO productWithEmptyName = this.productHelper.buildCompleteEditorProductTO("P0001");
        productWithEmptyName.setName("");

        this.apiHelper.createEditorProductPOST(productWithEmptyName, authorizationHeader, 400);
    }

    @Test
    public void when_AddProduct_WithNullType_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO productWithNullType = this.productHelper.buildCompleteEditorProductTO("P0001");
        productWithNullType.setType(null);

        this.apiHelper.createEditorProductPOST(productWithNullType, authorizationHeader, 400);
    }

    @Test
    public void when_AddProduct_WithEmptyType_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO productWithEmptyType = this.productHelper.buildCompleteEditorProductTO("P0001");
        productWithEmptyType.setType("");

        this.apiHelper.createEditorProductPOST(productWithEmptyType, authorizationHeader, 400);
    }

    @Test
    public void when_AddProduct_WithNullCountry_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO productWithNullCountry = this.productHelper.buildCompleteEditorProductTO("P0001");
        productWithNullCountry.setCountry(null);

        this.apiHelper.createEditorProductPOST(productWithNullCountry, authorizationHeader, 400);
    }

    @Test
    public void when_AddProduct_WithEmptyCountry_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO productWithEmptyCountry = this.productHelper.buildCompleteEditorProductTO("P0001");
        productWithEmptyCountry.setCountry("");

        this.apiHelper.createEditorProductPOST(productWithEmptyCountry, authorizationHeader, 400);
    }

    @Test
    public void when_AddProduct_WithNullPackageSize_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO productWithNullPackageSize = this.productHelper.buildCompleteEditorProductTO("P0001");
        productWithNullPackageSize.setPackageSize(null);

        this.apiHelper.createEditorProductPOST(productWithNullPackageSize, authorizationHeader, 400);
    }

    @Test
    public void when_AddProduct_WithEmptyPackageSize_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO productWithEmptyPackageSize = this.productHelper.buildCompleteEditorProductTO("P0001");
        productWithEmptyPackageSize.setPackageSize("");

        this.apiHelper.createEditorProductPOST(productWithEmptyPackageSize, authorizationHeader, 400);
    }

    @Test
    public void when_AddProduct_WithNullPackageType_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO productWithNullPackageType = this.productHelper.buildCompleteEditorProductTO("P0001");
        productWithNullPackageType.setPackageType(null);

        this.apiHelper.createEditorProductPOST(productWithNullPackageType, authorizationHeader, 400);
    }

    @Test
    public void when_AddProduct_WithEmptyPackageType_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO productWithEmptyPackageType = this.productHelper.buildCompleteEditorProductTO("P0001");
        productWithEmptyPackageType.setPackageType("");

        this.apiHelper.createEditorProductPOST(productWithEmptyPackageType, authorizationHeader, 400);
    }

    @Test
    public void when_AddProduct_WithNullLanguage_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO productWithNullLanguage = this.productHelper.buildCompleteEditorProductTO("P0001");
        productWithNullLanguage.setLanguage(null);

        this.apiHelper.createEditorProductPOST(productWithNullLanguage, authorizationHeader, 400);
    }

    @Test
    public void when_AddProduct_WithEmptyLanguage_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO productWithEmptyLanguage = this.productHelper.buildCompleteEditorProductTO("P0001");
        productWithEmptyLanguage.setLanguage("");

        this.apiHelper.createEditorProductPOST(productWithEmptyLanguage, authorizationHeader, 400);
    }

    @Test
    public void when_AddProduct_WithNullDescription_Then_Created() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO productWithNullDescription = this.productHelper.buildCompleteEditorProductTO("P0001");
        productWithNullDescription.setDescription(null);

        this.apiHelper.createEditorProductPOST(productWithNullDescription, authorizationHeader, 201);
    }

    @Test
    public void when_AddProduct_WithEmptyDescription_Then_Created() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO productWithEmptyDescription = this.productHelper.buildCompleteEditorProductTO("P0001");
        productWithEmptyDescription.setDescription("");

        this.apiHelper.createEditorProductPOST(productWithEmptyDescription, authorizationHeader, 201);
    }

    @Test
    public void when_UpdateProduct_Then_Ok() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");

        EditorProductTO updatedProduct = this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201);

        updatedProduct.setName("Updated");
        updatedProduct.setType("Updated");
        updatedProduct.setCountry("Updated");
        updatedProduct.setPackageSize("Updated");
        updatedProduct.setPackageType("Updated");
        updatedProduct.setLanguage("Updated");
        updatedProduct.setDescription("Updated");
        Long id = updatedProduct.getId();
        updatedProduct = this.apiHelper.updateEditorProductPUT(id, updatedProduct, authorizationHeader, 200);

        EditorProductTO actual = apiHelper.getEditorProductGET(id, authorizationHeader, 200);
        productHelper.assertEditorProductsEqual(updatedProduct, actual);
    }

    @Test
    public void when_UpdateProduct_WithNullProductNumber_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");

        EditorProductTO updatedProductWithNullNumber = this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201);

        updatedProductWithNullNumber.setProductNumber(null);
        Long id = updatedProductWithNullNumber.getId();
        this.apiHelper.updateEditorProductPUT(id, updatedProductWithNullNumber, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateProduct_WithEmptyProductNumber_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");

        EditorProductTO updatedProductWithEmptyNumber = this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201);

        updatedProductWithEmptyNumber.setProductNumber("");
        Long id = updatedProductWithEmptyNumber.getId();
        this.apiHelper.updateEditorProductPUT(id, updatedProductWithEmptyNumber, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateProduct_WithExistingProductNumber_Then_Conflict() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");
        EditorProductTO secondExistingProduct = this.productHelper.buildCompleteEditorProductTO("P0002");
        this.apiHelper.createEditorProductPOST(secondExistingProduct, authorizationHeader, 201);

        EditorProductTO updatedProductWithEmptyNumber = this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201);

        updatedProductWithEmptyNumber.setProductNumber("P0002");
        Long id = updatedProductWithEmptyNumber.getId();
        this.apiHelper.updateEditorProductPUT(id, updatedProductWithEmptyNumber, authorizationHeader, 409);
    }

    @Test
    public void when_UpdateProduct_WithNullName_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");

        EditorProductTO updatedProductWithNullName = this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201);

        updatedProductWithNullName.setName(null);
        Long id = updatedProductWithNullName.getId();
        this.apiHelper.updateEditorProductPUT(id, updatedProductWithNullName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateProduct_WithEmptyName_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");

        EditorProductTO updatedProductWithEmptyName = this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201);

        updatedProductWithEmptyName.setName("");
        Long id = updatedProductWithEmptyName.getId();
        this.apiHelper.updateEditorProductPUT(id, updatedProductWithEmptyName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateProduct_WithNullType_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");

        EditorProductTO updatedProductWithNullType = this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201);

        updatedProductWithNullType.setType(null);
        Long id = updatedProductWithNullType.getId();
        this.apiHelper.updateEditorProductPUT(id, updatedProductWithNullType, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateProduct_WithEmptyType_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");

        EditorProductTO updatedProductWithEmptyType = this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201);

        updatedProductWithEmptyType.setType("");
        Long id = updatedProductWithEmptyType.getId();
        this.apiHelper.updateEditorProductPUT(id, updatedProductWithEmptyType, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateProduct_WithNullCountry_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");

        EditorProductTO updatedProductWithNullCountry = this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201);

        updatedProductWithNullCountry.setCountry(null);
        Long id = updatedProductWithNullCountry.getId();
        this.apiHelper.updateEditorProductPUT(id, updatedProductWithNullCountry, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateProduct_WithEmptyCountry_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");

        EditorProductTO updatedProductWithEmptyCountry = this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201);

        updatedProductWithEmptyCountry.setCountry("");
        Long id = updatedProductWithEmptyCountry.getId();
        this.apiHelper.updateEditorProductPUT(id, updatedProductWithEmptyCountry, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateProduct_WithNullPackageSize_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");

        EditorProductTO updatedProductWithNullPackageSize = this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201);

        updatedProductWithNullPackageSize.setType(null);
        Long id = updatedProductWithNullPackageSize.getId();
        this.apiHelper.updateEditorProductPUT(id, updatedProductWithNullPackageSize, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateProduct_WithEmptyPackageSize_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");

        EditorProductTO updatedProductWithEmptyPackageSize = this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201);

        updatedProductWithEmptyPackageSize.setType("");
        Long id = updatedProductWithEmptyPackageSize.getId();
        this.apiHelper.updateEditorProductPUT(id, updatedProductWithEmptyPackageSize, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateProduct_WithNullPackageType_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");

        EditorProductTO updatedProductWithNullPackageType = this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201);

        updatedProductWithNullPackageType.setPackageType(null);
        Long id = updatedProductWithNullPackageType.getId();
        this.apiHelper.updateEditorProductPUT(id, updatedProductWithNullPackageType, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateProduct_WithEmptyPackageType_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");

        EditorProductTO updatedProductWithEmptyPackageType = this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201);

        updatedProductWithEmptyPackageType.setPackageType("");
        Long id = updatedProductWithEmptyPackageType.getId();
        this.apiHelper.updateEditorProductPUT(id, updatedProductWithEmptyPackageType, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateProduct_WithNullLanguage_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");

        EditorProductTO updatedProductWithNullLanguage = this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201);

        updatedProductWithNullLanguage.setLanguage(null);
        Long id = updatedProductWithNullLanguage.getId();
        this.apiHelper.updateEditorProductPUT(id, updatedProductWithNullLanguage, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateProduct_WithEmptyLanguage_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");

        EditorProductTO updatedProductWithEmptyType = this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201);

        updatedProductWithEmptyType.setLanguage("");
        Long id = updatedProductWithEmptyType.getId();
        this.apiHelper.updateEditorProductPUT(id, updatedProductWithEmptyType, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateProduct_WithNullDescription_Then_Ok() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");

        EditorProductTO updatedProductWithNullDescription = this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201);

        updatedProductWithNullDescription.setDescription(null);
        Long id = updatedProductWithNullDescription.getId();
        this.apiHelper.updateEditorProductPUT(id, updatedProductWithNullDescription, authorizationHeader, 200);
    }

    @Test
    public void when_UpdateProduct_WithEmptyDescription_Then_Ok() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");

        EditorProductTO updatedProductWithEmptyDescription = this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201);

        updatedProductWithEmptyDescription.setDescription("");
        Long id = updatedProductWithEmptyDescription.getId();
        this.apiHelper.updateEditorProductPUT(id, updatedProductWithEmptyDescription, authorizationHeader, 200);
    }

    @Test
    public void when_DeleteProduct_Then_Ok() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");
        Long id = this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201).getId();
        this.apiHelper.deleteEditorProductDELETE(id, authorizationHeader, 204);
        assertFalse(this.productRepository.existsById(id));
    }

    @Test
    public void when_DeleteProductWithNonExistingId_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        this.apiHelper.deleteEditorProductDELETE(999L, authorizationHeader, 404);
    }

    @Test
    public void when_DeleteProductTheReferencesWithOrdersAsBeforeDeleted_Then_NoContent() throws Exception {
        EditorProductTO toBeDeleted = this.productHelper.buildCompleteEditorProductTO("ToBeDeleted");
        toBeDeleted = this.saveProductInDatabase(toBeDeleted);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        List<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO firstEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("First");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        firstEquipment = this.saveEquipmentInDatabase(firstEquipment);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);
        Long toBeDeletedId = toBeDeleted.getId();

        equipments.add(firstEquipment);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO newOrder = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrder.setProductAfter(toBeDeleted);
        newOrder.setProductBefore(productBefore);
        newOrder.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        newOrder = this.apiHelper.createEditorOrderPOST(newOrder, authorizationHeader, 201);
        Long orderId = newOrder.getId();

        toBeDeleted = this.apiHelper.getEditorProductGET(toBeDeletedId, authorizationHeader, 200);

        apiHelper.deleteEditorProductDELETE(toBeDeletedId, authorizationHeader, 204);
        assertFalse(this.productRepository.existsById(toBeDeletedId));
        newOrder = this.apiHelper.getEditorOrderGET(orderId, authorizationHeader, 200);

        assertNull(newOrder.getProductAfter());
    }

    @Test
    public void when_GetProduct_Then_Ok() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO existingProduct = this.productHelper.buildCompleteEditorProductTO("P0001");
        existingProduct = this.apiHelper.createEditorProductPOST(existingProduct, authorizationHeader, 201);

        EditorProductTO actual = this.apiHelper.getEditorProductGET(existingProduct.getId(), authorizationHeader, 200);

        productHelper.assertEditorProductsEqual(existingProduct, actual);
    }

    @Test
    public void when_GetProductWithNonExistingId_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        this.apiHelper.getEditorProductGET(999L, authorizationHeader, 404);
    }

    @Test
    public void when_AddEquipment_Then_Created() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorEquipmentTO equipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0001");

        EditorEquipmentTO response = this.apiHelper.createEditorEquipmentPOST(equipment, authorizationHeader, 201);
        EditorEquipmentTO actual = this.apiHelper.getEditorEquipmentGET(response.getId(), authorizationHeader, 200);

        this.equipmentHelper.assertEditorEquipmentEqual(response, actual);
    }

    @Test
    public void when_AddEquipment_WithNullEquipmentNumber_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorEquipmentTO equipmentWithNullNumber = this.equipmentHelper.buildCompleteEditorEquipmentTO(null);

        this.apiHelper.createEditorEquipmentPOST(equipmentWithNullNumber, authorizationHeader, 400);
    }

    @Test
    public void when_AddEquipment_WithEmptyEquipmentNumber_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorEquipmentTO equipmentWithEmptyName = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0001");
        equipmentWithEmptyName.setName("");

        this.apiHelper.createEditorEquipmentPOST(equipmentWithEmptyName, authorizationHeader, 400);
    }

    @Test
    public void when_AddEquipment_WithEmptyType_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorEquipmentTO equipmentWithEmptyType = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0001");
        equipmentWithEmptyType.setType("");

        this.apiHelper.createEditorEquipmentPOST(equipmentWithEmptyType, authorizationHeader, 400);
    }

    @Test
    public void when_AddEquipment_WithNullDescription_Then_Created() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorEquipmentTO equipmentWithNullDescription = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0001");
        equipmentWithNullDescription.setDescription(null);

        this.apiHelper.createEditorEquipmentPOST(equipmentWithNullDescription, authorizationHeader, 201);
    }

    @Test
    public void when_AddEquipment_WithEmptyDescription_Then_Created() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorEquipmentTO equipmentWithEmptyDescription = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0001");
        equipmentWithEmptyDescription.setDescription("");

        this.apiHelper.createEditorEquipmentPOST(equipmentWithEmptyDescription, authorizationHeader, 201);
    }

    @Test
    public void when_AddEquipment_WithExistingEquipmentNumber_Then_Conflict() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorEquipmentTO existingEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0001");
        this.apiHelper.createEditorEquipmentPOST(existingEquipment, authorizationHeader, 201);

        EditorEquipmentTO newEquipmentWithSameNumber = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0001");
        this.apiHelper.createEditorEquipmentPOST(newEquipmentWithSameNumber, authorizationHeader, 409);
    }

    @Test
    public void when_UpdateEquipment_Then_Ok() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorEquipmentTO existingEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0001");

        EditorEquipmentTO updatedEquipment = this.apiHelper.createEditorEquipmentPOST(existingEquipment, authorizationHeader, 201);

        updatedEquipment.setName("Updated");
        updatedEquipment.setType("Updated");
        updatedEquipment.setDescription("Updated");
        Long id = updatedEquipment.getId();
        updatedEquipment = this.apiHelper.updateEditorEquipmentPUT(id, updatedEquipment, authorizationHeader, 200);

        EditorEquipmentTO actual = apiHelper.getEditorEquipmentGET(id, authorizationHeader, 200);
        equipmentHelper.assertEditorEquipmentEqual(updatedEquipment, actual);
    }

    @Test
    public void when_UpdateEquipment_WithNullEquipmentNumber_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorEquipmentTO existingEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0001");

        EditorEquipmentTO updatedEquipmentWithNullNumber = this.apiHelper.createEditorEquipmentPOST(existingEquipment, authorizationHeader, 201);

        updatedEquipmentWithNullNumber.setEquipmentNumber(null);
        Long id = updatedEquipmentWithNullNumber.getId();
        this.apiHelper.updateEditorEquipmentPUT(id, updatedEquipmentWithNullNumber, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateEquipment_WithEmptyEquipmentNumber_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorEquipmentTO existingEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0001");

        EditorEquipmentTO updatedEquipmentWithEmptyNumber = this.apiHelper.createEditorEquipmentPOST(existingEquipment, authorizationHeader, 201);

        updatedEquipmentWithEmptyNumber.setEquipmentNumber("");
        Long id = updatedEquipmentWithEmptyNumber.getId();
        this.apiHelper.updateEditorEquipmentPUT(id, updatedEquipmentWithEmptyNumber, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateEquipment_WithNullName_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorEquipmentTO existingEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0001");

        EditorEquipmentTO updatedEquipmentWithNullName = this.apiHelper.createEditorEquipmentPOST(existingEquipment, authorizationHeader, 201);

        updatedEquipmentWithNullName.setName(null);
        Long id = updatedEquipmentWithNullName.getId();
        this.apiHelper.updateEditorEquipmentPUT(id, updatedEquipmentWithNullName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateEquipment_WithEmptyName_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorEquipmentTO existingEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0001");

        EditorEquipmentTO updatedEquipmentWithEmptyName = this.apiHelper.createEditorEquipmentPOST(existingEquipment, authorizationHeader, 201);

        updatedEquipmentWithEmptyName.setName("");
        Long id = updatedEquipmentWithEmptyName.getId();
        this.apiHelper.updateEditorEquipmentPUT(id, updatedEquipmentWithEmptyName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateEquipment_WithNullType_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorEquipmentTO existingEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0001");

        EditorEquipmentTO updatedEquipmentWithNullType = this.apiHelper.createEditorEquipmentPOST(existingEquipment, authorizationHeader, 201);

        updatedEquipmentWithNullType.setType(null);
        Long id = updatedEquipmentWithNullType.getId();
        this.apiHelper.updateEditorEquipmentPUT(id, updatedEquipmentWithNullType, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateEquipment_WithEmptyType_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorEquipmentTO existingEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0001");

        EditorEquipmentTO updatedEquipmentWithEmptyType = this.apiHelper.createEditorEquipmentPOST(existingEquipment, authorizationHeader, 201);

        updatedEquipmentWithEmptyType.setType("");
        Long id = updatedEquipmentWithEmptyType.getId();
        this.apiHelper.updateEditorEquipmentPUT(id, updatedEquipmentWithEmptyType, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateEquipment_WithExistingEquipmentNumber_Then_Conflict() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorEquipmentTO existingEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0001");
        EditorEquipmentTO secondExistingEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0002");
        this.apiHelper.createEditorEquipmentPOST(secondExistingEquipment, authorizationHeader, 201);

        EditorEquipmentTO updatedEquipmentWithSameNumber = this.apiHelper.createEditorEquipmentPOST(existingEquipment, authorizationHeader, 201);

        updatedEquipmentWithSameNumber.setEquipmentNumber("E0002");
        Long id = updatedEquipmentWithSameNumber.getId();
        this.apiHelper.updateEditorEquipmentPUT(id, updatedEquipmentWithSameNumber, authorizationHeader, 409);
    }

    @Test
    public void when_DeleteEquipment_Then_Ok() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorEquipmentTO existingEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0001");
        Long id = this.apiHelper.createEditorEquipmentPOST(existingEquipment, authorizationHeader, 201).getId();
        this.apiHelper.deleteEditorEquipmentDELETE(id, authorizationHeader, 204);
        assertFalse(this.equipmentRepository.existsById(id));
    }

    @Test
    public void when_DeleteEquipmentWithNonExistingId_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        this.apiHelper.deleteEditorEquipmentDELETE(999L, authorizationHeader, 404);
    }

    @Test
    public void when_DeleteEquipmentTheReferencesWithOrders_Then_NoContent() throws Exception {
        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("After");
        productAfter = this.saveProductInDatabase(productAfter);
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("Before");
        productBefore = this.saveProductInDatabase(productBefore);

        List<EditorEquipmentTO> equipments = new ArrayList<EditorEquipmentTO>();
        EditorEquipmentTO toBeDeleted = this.equipmentHelper.buildCompleteEditorEquipmentTO("toBeDeleted");
        EditorEquipmentTO secondEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Second");
        EditorEquipmentTO thirdEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("Third");
        toBeDeleted = this.saveEquipmentInDatabase(toBeDeleted);
        secondEquipment = this.saveEquipmentInDatabase(secondEquipment);
        thirdEquipment = this.saveEquipmentInDatabase(thirdEquipment);
        Long toBeDeletedId = toBeDeleted.getId();

        equipments.add(toBeDeleted);
        equipments.add(secondEquipment);
        equipments.add(thirdEquipment);

        EditorOrderTO newOrder = this.orderHelper.buildCompleteEditorOrderTO("W0001");
        newOrder.setProductAfter(productAfter);
        newOrder.setProductBefore(productBefore);
        newOrder.setEquipment(equipments);

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        newOrder = this.apiHelper.createEditorOrderPOST(newOrder, authorizationHeader, 201);
        Long orderId = newOrder.getId();

        equipments = this.apiHelper.getEditorAllEquipmentGET(authorizationHeader, 200);
        equipments.forEach(
                (equipment) -> assertTrue(equipment.getOrders().stream().anyMatch((order) -> order.getId().equals(orderId)))
        );

        apiHelper.deleteEditorEquipmentDELETE(toBeDeletedId, authorizationHeader, 204);
        assertFalse(this.equipmentRepository.existsById(toBeDeletedId));
        newOrder = this.apiHelper.getEditorOrderGET(orderId, authorizationHeader, 200);

        newOrder.getEquipment().forEach(
                (equipment) -> assertNotEquals(equipment.getId(), toBeDeletedId)
        );

    }

    @Test
    public void when_GetEquipment_Then_Ok() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorEquipmentTO existingEquipment = this.equipmentHelper.buildCompleteEditorEquipmentTO("E0001");
        existingEquipment = this.apiHelper.createEditorEquipmentPOST(existingEquipment, authorizationHeader, 201);

        EditorEquipmentTO actual = this.apiHelper.getEditorEquipmentGET(existingEquipment.getId(), authorizationHeader, 200);

        equipmentHelper.assertEditorEquipmentEqual(existingEquipment, actual);
    }

    @Test
    public void when_GetEquipmentWithNonExistingId_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        this.apiHelper.getEditorEquipmentGET(999L, authorizationHeader, 404);
    }

}
