package com.shopfloor.backend.service;

import com.shopfloor.backend.api.transferobjects.editors.*;
import com.shopfloor.backend.database.repositories.OrderRepository;
import com.shopfloor.backend.database.repositories.ProductRepository;
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

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductHelper productHelper;

    @AfterEach
    public void tearDown() {
        // Clear the repository after each test
        orderRepository.deleteAll();
        productRepository.deleteAll();
    }

    /**
     *ORDERS
     */
    @Test
    public void when_GetEditorOrders_Then_OK() throws Exception {

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        EditorOrderTO orderOne = apiHelper.createEditorOrderPOST(this.orderHelper.buildCompleteEditorOrderTO("W0001", product), authorizationHeader, 201);
        EditorOrderTO orderTwo = apiHelper.createEditorOrderPOST(this.orderHelper.buildCompleteEditorOrderTO("W0002", product), authorizationHeader, 201);
        EditorOrderTO orderThree = apiHelper.createEditorOrderPOST(this.orderHelper.buildCompleteEditorOrderTO("W0003", product), authorizationHeader, 201);
        EditorOrderTO orderFour = apiHelper.createEditorOrderPOST(this.orderHelper.buildCompleteEditorOrderTO("W0004", product), authorizationHeader, 201);
        EditorOrderTO orderFive = apiHelper.createEditorOrderPOST(this.orderHelper.buildCompleteEditorOrderTO("W0005", product), authorizationHeader, 201);

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

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);
        EditorOrderTO newOrder = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);


        //Make POST request and save the response
        EditorOrderTO response = apiHelper.createEditorOrderPOST(newOrder, authorizationHeader, 201);


        // Verify the order is created by asserting the created order matches the new order
        EditorOrderTO actual = apiHelper.getEditorOrderGET(response.getId(), authorizationHeader, 200);
        orderHelper.assertEditorOrdersEqual(actual, response);
    }

    @Test
    public void when_AddOrder_WithNegativeTimeRequired_Then_BadRequest() throws Exception {

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        //Creating new order with negative time required
        EditorOrderTO oderWithNegativeTimeRequired = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        oderWithNegativeTimeRequired.getWorkflows().get(0).getTasks().get(0).getItems().get(0).setTimeRequired(-1);

        apiHelper.createEditorOrderPOST(oderWithNegativeTimeRequired, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithExistingOrderNumber_Then_Conflict() throws Exception {
        // Create a fully populated EditorOrderTO object for testing
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);
        apiHelper.createEditorOrderPOST(this.orderHelper.buildCompleteEditorOrderTO("W0001", product), authorizationHeader, 201);

        //Create new order with order number which already exists
        EditorOrderTO newOrderWithSameOrderNumber = this.orderHelper.buildCompleteEditorOrderTO("W0001",product);

        //Make Post request and expect conflict
        apiHelper.createEditorOrderPOST(newOrderWithSameOrderNumber, authorizationHeader, 409);
    }

    @Test
    public void when_AddOrder_WithNullOrder_Then_BadRequest() throws Exception {

        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        // Create an EditorOrderTO object with a null order
        EditorOrderTO newNullOrder = null;

        // Get the authorization header for the authenticated user

        // Perform the POST request with null as order
        apiHelper.createEditorOrderPOST(newNullOrder, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullOrderNumber_Then_BadRequest() throws Exception {
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        // Create an EditorOrderTO object with a null order number
        EditorOrderTO newOrderWithNullNumber = this.orderHelper.buildCompleteEditorOrderTO(null, product);

        //Make Post request with order with null order number
        apiHelper.createEditorOrderPOST(newOrderWithNullNumber, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithEmptyOrderNumber_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        // Create an EditorOrderTO object with empty order number
        EditorOrderTO newOrderWithNullNumber = this.orderHelper.buildCompleteEditorOrderTO("", product);

        //Make Post request with order with null order number
        apiHelper.createEditorOrderPOST(newOrderWithNullNumber, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullOrderName_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);
        // Create an EditorOrderTO object with a null name
        EditorOrderTO newOrderWithNullName = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        newOrderWithNullName.setName(null);


        //Make Post request with order with null order name
        apiHelper.createEditorOrderPOST(newOrderWithNullName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithEmptyOrderName_Then_BadRequest() throws Exception {

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        // Create an EditorOrderTO object with empty name
        EditorOrderTO newOrderWithNullName = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        newOrderWithNullName.setName("");



        //Make Post request with order with null order name
        apiHelper.createEditorOrderPOST(newOrderWithNullName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullWorkflowName_Then_BadRequest() throws Exception {

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        // Create an EditorOrderTO object with a null workflow name
        EditorOrderTO newOrderWithNullWorkflowName = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        newOrderWithNullWorkflowName.getWorkflows().get(0).setName(null);


        apiHelper.createEditorOrderPOST(newOrderWithNullWorkflowName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithEmptyWorkflowName_Then_BadRequest() throws Exception {

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        // Create an EditorOrderTO object with empty workflow name
        EditorOrderTO newOrderWithNullWorkflowName = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        newOrderWithNullWorkflowName.getWorkflows().get(0).setName("");


        apiHelper.createEditorOrderPOST(newOrderWithNullWorkflowName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullTaskName_Then_BadRequest() throws Exception {

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        // Create an EditorOrderTO object with a null task name
        EditorOrderTO newOrderWithNullTaskName = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        newOrderWithNullTaskName.getWorkflows().get(0)
                .getTasks().get(0)
                .setName(null);

        apiHelper.createEditorOrderPOST(newOrderWithNullTaskName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithEmptyTaskName_Then_BadRequest() throws Exception {

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        // Create an EditorOrderTO object with empty task name
        EditorOrderTO newOrderWithNullTaskName = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        newOrderWithNullTaskName.getWorkflows().get(0)
                .getTasks().get(0)
                .setName("");

        apiHelper.createEditorOrderPOST(newOrderWithNullTaskName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullItemName_Then_BadRequest() throws Exception {

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        // Create an EditorOrderTO object with a null item name
        EditorOrderTO newOrderWithNullItemName = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        newOrderWithNullItemName.getWorkflows().get(0)
                .getTasks().get(0)
                .getItems().get(0)
                .setName(null);

        apiHelper.createEditorOrderPOST(newOrderWithNullItemName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithEmptyItemName_Then_BadRequest() throws Exception {

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        // Create an EditorOrderTO object with a null item name
        EditorOrderTO newOrderWithNullItemName = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        newOrderWithNullItemName.getWorkflows().get(0)
                .getTasks().get(0)
                .getItems().get(0)
                .setName("");

        apiHelper.createEditorOrderPOST(newOrderWithNullItemName, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullWorklowList_Then_BadRequest() throws Exception {

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        // Create an EditorOrderTO object with a null workflow list
        EditorOrderTO newOrderWithNullWorkflowList = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        newOrderWithNullWorkflowList.setWorkflows(null);

        apiHelper.createEditorOrderPOST(newOrderWithNullWorkflowList, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullWorklowEntity_Then_BadRequest() throws Exception {

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        // Create an EditorOrderTO object with a null workflow entity
        EditorOrderTO newOrderWithNullWorkflowList = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
       newOrderWithNullWorkflowList
               .getWorkflows()
               .add(null);


        apiHelper.createEditorOrderPOST(newOrderWithNullWorkflowList, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullTaskEntity_Then_BadRequest() throws Exception {

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        // Create an EditorOrderTO object with a null task entity
        EditorOrderTO newOrderWithNullWorkflowList = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        newOrderWithNullWorkflowList
                .getWorkflows().get(0)
                .getTasks()
                .add(null);

        apiHelper.createEditorOrderPOST(newOrderWithNullWorkflowList, authorizationHeader, 400);
    }

    @Test
    public void when_AddOrder_WithNullItemEntity_Then_BadRequest() throws Exception {

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        // Create an EditorOrderTO object with a null item entity
        EditorOrderTO newOrderWithNullWorkflowList = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        newOrderWithNullWorkflowList.
                getWorkflows().get(0)
                .getTasks()
                .add(null);

        apiHelper.createEditorOrderPOST(newOrderWithNullWorkflowList, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_Then_OK() throws Exception {

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        //Preparing existing Order
        EditorOrderTO expected = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
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

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        //Saving and order
        EditorOrderTO orderWithNullNumber = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        orderWithNullNumber = apiHelper.createEditorOrderPOST(orderWithNullNumber, authorizationHeader, 201);

        //Updating with null values
        orderWithNullNumber.setOrderNumber(null);
        apiHelper.updateEditorOrderPUT(orderWithNullNumber.getId(), orderWithNullNumber, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullOrderNumber_Then_BadRequest() throws Exception {

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        //Saving and order
        EditorOrderTO oderWithNegativeTimeRequired = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        oderWithNegativeTimeRequired = apiHelper.createEditorOrderPOST(oderWithNegativeTimeRequired, authorizationHeader, 201);

        //Updating with negative values
        oderWithNegativeTimeRequired.getWorkflows().get(0).getTasks().get(0).getItems().get(0).setTimeRequired(-1);
        apiHelper.updateEditorOrderPUT(oderWithNegativeTimeRequired.getId(), oderWithNegativeTimeRequired, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullName_Then_BadRequest() throws Exception {

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        //Saving the order
        EditorOrderTO orderWithNullNumber = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        orderWithNullNumber = apiHelper.createEditorOrderPOST(orderWithNullNumber, authorizationHeader, 201);

        //Updating with null values
        orderWithNullNumber.setName(null);
        apiHelper.updateEditorOrderPUT(orderWithNullNumber.getId(), orderWithNullNumber, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullWorkflowName_Then_BadRequest() throws Exception {

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        //Saving the order
        EditorOrderTO orderWithNullWorkflowName = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        orderWithNullWorkflowName = apiHelper.createEditorOrderPOST(orderWithNullWorkflowName, authorizationHeader, 201);

        //Updating with null values
        orderWithNullWorkflowName.getWorkflows().get(0).setName(null);
        apiHelper.updateEditorOrderPUT(orderWithNullWorkflowName.getId(), orderWithNullWorkflowName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullTaskName_Then_BadRequest() throws Exception {

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        //Saving the order
        EditorOrderTO orderWithNullTaskName = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        orderWithNullTaskName = apiHelper.createEditorOrderPOST(orderWithNullTaskName, authorizationHeader, 201);

        //Updating the order with null values
        orderWithNullTaskName.getWorkflows().get(0).getTasks().get(0).setName(null);
        apiHelper.updateEditorOrderPUT(orderWithNullTaskName.getId(), orderWithNullTaskName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullItemName_Then_BadRequest() throws Exception {

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        //Saving the order
        EditorOrderTO orderWithNullItemName = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        orderWithNullItemName = apiHelper.createEditorOrderPOST(orderWithNullItemName, authorizationHeader, 201);

        //Updating the order with null values
        orderWithNullItemName.getWorkflows().get(0).getTasks().get(0).getItems().get(0).setName(null);
        apiHelper.updateEditorOrderPUT(orderWithNullItemName.getId(), orderWithNullItemName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullWorkflowList_Then_BadRequest() throws Exception {

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        //Saving the order
        EditorOrderTO orderWithNullItemName = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        orderWithNullItemName = apiHelper.createEditorOrderPOST(orderWithNullItemName, authorizationHeader, 201);

        //Updating the order with null values
        orderWithNullItemName.setWorkflows(null);
        apiHelper.updateEditorOrderPUT(orderWithNullItemName.getId(), orderWithNullItemName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullWorkflowEntity_Then_BadRequest() throws Exception {

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        //Saving the order
        EditorOrderTO orderWithNullItemName = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        orderWithNullItemName = apiHelper.createEditorOrderPOST(orderWithNullItemName, authorizationHeader, 201);

        //Updating the order with null values
        orderWithNullItemName.getWorkflows().add(null);
        apiHelper.updateEditorOrderPUT(orderWithNullItemName.getId(), orderWithNullItemName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullTaskList_Then_BadRequest() throws Exception {

        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        //Saving the order
        EditorOrderTO orderWithNullItemName = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        orderWithNullItemName = apiHelper.createEditorOrderPOST(orderWithNullItemName, authorizationHeader, 201);

        //Updating the order with null values
        orderWithNullItemName.getWorkflows().get(0).setTasks(null);
        apiHelper.updateEditorOrderPUT(orderWithNullItemName.getId(), orderWithNullItemName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullTaskEntity_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        //Building the order
        EditorOrderTO orderWithNullItemName = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        orderWithNullItemName = apiHelper.createEditorOrderPOST(orderWithNullItemName, authorizationHeader, 201);

        //Updating the order with null values
        orderWithNullItemName.getWorkflows().get(0).getTasks().add(null);
        apiHelper.updateEditorOrderPUT(orderWithNullItemName.getId(), orderWithNullItemName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNullItemEntity_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        //Saving the order
        EditorOrderTO orderWithNullItemName = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        orderWithNullItemName = apiHelper.createEditorOrderPOST(orderWithNullItemName, authorizationHeader, 201);

        //Updating the order with null values
        orderWithNullItemName.getWorkflows().get(0).getTasks().get(0).getItems().add(null);
        apiHelper.updateEditorOrderPUT(orderWithNullItemName.getId(), orderWithNullItemName, authorizationHeader, 400);
    }

    @Test
    public void when_UpdateOrder_WithNotExistingOrder_Then_NotFound() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        EditorOrderTO orderWithNonExistingId = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        orderWithNonExistingId.setId(999L);
        apiHelper.updateEditorOrderPUT(orderWithNonExistingId.getId(), orderWithNonExistingId, authorizationHeader, 404);
    }

    @Test
    public void when_UpdateOrder_WithExistingOrderNumber_Then_Conflict() throws Exception {
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        apiHelper.createEditorOrderPOST(this.orderHelper.buildCompleteEditorOrderTO("W0001", product), authorizationHeader, 201);


        EditorOrderTO toBeUpdated = this.orderHelper.buildCompleteEditorOrderTO("W0002", product);
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
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        EditorOrderTO toBeDeleted = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
        toBeDeleted = apiHelper.createEditorOrderPOST(toBeDeleted, authorizationHeader, 201);

        Long toDeleteId = toBeDeleted.getId();
        assertTrue(this.orderRepository.existsById(toDeleteId));

        apiHelper.deleteEditorOrderDELETE(toDeleteId, authorizationHeader, 204);

        assertFalse(orderRepository.existsById(toBeDeleted.getId()));
    }

    @Test
    public void when_GetOrder_Then_OK() throws Exception {
        // Get the authorization header for the authenticated user
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");

        //Saving a product
        EditorProductTO product = this.productHelper.buildCompleteEditorProductTO("P0001");
        product = this.apiHelper.createEditorProductPOST(product, authorizationHeader, 201);

        EditorOrderTO expected = this.orderHelper.buildCompleteEditorOrderTO("W0001", product);
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
    public void when_AddProduct_WithNullOrderList_Then_BadRequest() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO productWithNullLanguage = this.productHelper.buildCompleteEditorProductTO("P0001");
        productWithNullLanguage.setOrders(null);

        this.apiHelper.createEditorProductPOST(productWithNullLanguage, authorizationHeader, 400);
    }

    @Test
    public void when_AddProduct_WithEmptyOrderList_Then_Created() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorProductTO productWithEmptyLanguage = this.productHelper.buildCompleteEditorProductTO("P0001");
        productWithEmptyLanguage.setOrders(new ArrayList<EditorOrderTO>());

        this.apiHelper.createEditorProductPOST(productWithEmptyLanguage, authorizationHeader, 201);
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

}
