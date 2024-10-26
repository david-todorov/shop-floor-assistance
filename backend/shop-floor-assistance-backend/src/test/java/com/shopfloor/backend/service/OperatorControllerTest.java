package com.shopfloor.backend.service;

import com.shopfloor.backend.api.transferobjects.editors.EditorOrderTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorProductTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorOrderTO;
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


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class OperatorControllerTest {

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

    @Test
    public void when_GetOperatorOrders_Then_OK() throws Exception{
        //Save five orders to the database
        // Get the authorization header for the authenticated user
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
        List<OperatorOrderTO> actualOrders = this.apiHelper.getOperatorAllOrdersGET(authorizationHeader, 200);

        // Compare each element of the lists using the helper method
        for (int i = 0; i < expectedOrders.size(); i++) {
            orderHelper.assertEditorAndOperatorOrdersEqual(expectedOrders.get(i), actualOrders.get(i));
        }
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

        OperatorOrderTO actual = apiHelper.getOperatorOrderGET(expected.getId(), authorizationHeader, 200);

        orderHelper.assertEditorAndOperatorOrdersEqual(expected, actual);
    }

    @Test
    public void when_GetOrder_WithNonExistingId_Then_NotFound() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        apiHelper.getOperatorOrderGET(999L, authorizationHeader, 404);
    }

}
