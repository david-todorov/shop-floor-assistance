package com.shopfloor.backend.service;

import com.shopfloor.backend.api.transferobjects.editors.EditorOrderTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorOrderTO;
import com.shopfloor.backend.database.repositories.OrderRepository;
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

    @AfterEach
    public void tearDown() {
        // Clear the repository after each test
        orderRepository.deleteAll();
    }

    @Test
    public void when_GetOperatorOrders_Then_OK() throws Exception{
        //Save five orders to the database
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
        List<OperatorOrderTO> actualOrders = this.apiHelper.getOperatorAllOrdersGET(authorizationHeader, 200);

        // Compare each element of the lists using the helper method
        for (int i = 0; i < expectedOrders.size(); i++) {
            orderHelper.assertEditorAndOperatorOrdersEqual(expectedOrders.get(i), actualOrders.get(i));
        }
    }

    @Test
    public void when_GetOrder_Then_OK() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorOrderTO expected = OrderHelper.buildCompleteEditorOrderTO("W0001");
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
