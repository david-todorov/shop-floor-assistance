package com.shopfloor.backend.tests;

import com.shopfloor.backend.api.transferobjects.editors.EditorEquipmentTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorOrderTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorProductTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorExecutionTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorForecastTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorOrderTO;
import com.shopfloor.backend.database.repositories.EquipmentRepository;
import com.shopfloor.backend.database.repositories.ExecutionRepository;
import com.shopfloor.backend.database.repositories.OrderRepository;
import com.shopfloor.backend.database.repositories.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the operator controller.
 *
 * This class contains tests for various operator scenarios, including
 * login with valid and invalid credentials, and access control for different
 * user roles.
 * @author David Todorov (https://github.com/david-todorov)
 */
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class OperatorControllerTest {

    /**
     * Helper for API-related operations.
     */
    @Autowired
    private ApiHelper apiHelper;

    /**
     * Helper for order-related operations.
     */
    @Autowired
    private OrderHelper orderHelper;

    /**
     * Repository for managing order data.
     */
    @Autowired
    private OrderRepository orderRepository;

    /**
     * Repository for managing product data.
     */
    @Autowired
    private ProductRepository productRepository;

    /**
     * Repository for managing equipment data.
     */
    @Autowired
    private EquipmentRepository equipmentRepository;

    /**
     * Repository for managing execution data.
     */
    @Autowired
    private ExecutionRepository executionRepository;

    /**
     * Helper for product-related operations.
     */
    @Autowired
    private ProductHelper productHelper;

    /**
     * Helper for equipment-related operations.
     */
    @Autowired
    private EquipmentHelper equipmentHelper;

    /**
     * Clears the database after each test.
     * NOTE: The order of deletion is important to avoid foreign key constraint violations.
     * It bypasses the service layer and directly deletes the data from the database.
     * On service layer, the references to the respective entities are deleted first.
     * Use this method carefully.
     */
    @AfterEach
    public void tearDown() {

        executionRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        equipmentRepository.deleteAll();
    }


    @Test
    public void when_GetOperatorOrder_Then_OK() throws Exception {

        String editorHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorOrderTO order = orderHelper.buildCompleteEditorOrderTO("W001");
        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("P001");
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("P002");
        productAfter = this.apiHelper.createEditorProductPOST(productAfter, editorHeader, 201);
        productBefore = this.apiHelper.createEditorProductPOST(productBefore, editorHeader, 201);

        EditorEquipmentTO equipment1 = this.equipmentHelper.buildCompleteEditorEquipmentTO("E001");
        equipment1 = this.apiHelper.createEditorEquipmentPOST(equipment1, editorHeader, 201);
        EditorEquipmentTO equipment2 = this.equipmentHelper.buildCompleteEditorEquipmentTO("E002");
        equipment2 = this.apiHelper.createEditorEquipmentPOST(equipment2, editorHeader, 201);
        EditorEquipmentTO equipment3 = this.equipmentHelper.buildCompleteEditorEquipmentTO("E003");
        equipment3 = this.apiHelper.createEditorEquipmentPOST(equipment3, editorHeader, 201);

        order.setProductAfter(productAfter);
        order.setProductBefore(productBefore);
        order.getEquipment().add(equipment1);
        order.getEquipment().add(equipment2);
        order.getEquipment().add(equipment3);

        order = this.apiHelper.createEditorOrderPOST(order, editorHeader, 201);
        String operatorHeader = this.apiHelper.createAuthorizationHeaderFrom("operator","operator");
        OperatorOrderTO operatorOrderTO = this.apiHelper.getOperatorOrderGET(order.getId(), operatorHeader, 200);

    }

    @Test
    public void when_GetOperatorOrder_With_NonExistingId_Then_NotFound() throws Exception {

        String operatorHeader = this.apiHelper.createAuthorizationHeaderFrom("operator","operator");
        OperatorOrderTO operatorOrderTO = this.apiHelper.getOperatorOrderGET(999L, operatorHeader, 404);

    }

    @Test
    public void when_GetOperatorForecast_Then_OK() throws Exception {

        String editorHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorOrderTO order = orderHelper.buildCompleteEditorOrderTO("W001");
        order = this.apiHelper.createEditorOrderPOST(order, editorHeader, 201);

        String operatorHeader = this.apiHelper.createAuthorizationHeaderFrom("operator","operator");
        OperatorForecastTO operatorForecastTO = this.apiHelper.getOperatorForecastGET(order.getId(), operatorHeader, 200);

    }

    @Test
    public void when_GetOperatorForecast_WithNonExistingID_Then_NotFound() throws Exception {

        String operatorHeader = this.apiHelper.createAuthorizationHeaderFrom("operator","operator");
        OperatorForecastTO operatorForecastTO = this.apiHelper.getOperatorForecastGET(999L, operatorHeader, 404);

    }

    @Test
    public void when_StartOperatorOrder_Then_OK() throws Exception {

        String editorHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorOrderTO order = orderHelper.buildCompleteEditorOrderTO("W001");
        order = this.apiHelper.createEditorOrderPOST(order, editorHeader, 201);

        String operatorHeader = this.apiHelper.createAuthorizationHeaderFrom("operator","operator");
        OperatorExecutionTO operatorExecutionTO = this.apiHelper.startOperatorOrderPOST(order.getId(), operatorHeader, 201);

        assertFalse(operatorExecutionTO.getAborted());
        assertNotNull(operatorExecutionTO.getStartedAt());
        assertNull(operatorExecutionTO.getFinishedAt());
    }

    @Test
    public void when_StartOperatorOrder_With_NonExisting_Id_Then_NotFound() throws Exception {


        String operatorHeader = this.apiHelper.createAuthorizationHeaderFrom("operator","operator");
        OperatorExecutionTO operatorExecutionTO = this.apiHelper.startOperatorOrderPOST(999L, operatorHeader, 404);

    }

    @Test
    public void when_FinishOperatorOrder_Then_OK() throws Exception {

        String editorHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorOrderTO order = orderHelper.buildCompleteEditorOrderTO("W001");
        order = this.apiHelper.createEditorOrderPOST(order, editorHeader, 201);

        String operatorHeader = this.apiHelper.createAuthorizationHeaderFrom("operator","operator");
        OperatorExecutionTO operatorExecutionTO = this.apiHelper.startOperatorOrderPOST(order.getId(), operatorHeader, 201);

        assertFalse(operatorExecutionTO.getAborted());
        assertNotNull(operatorExecutionTO.getStartedAt());
        assertNull(operatorExecutionTO.getFinishedAt());

        operatorExecutionTO = this.apiHelper.finishOperatorOrderPUT(operatorExecutionTO.getId(), operatorHeader, 201);
        assertFalse(operatorExecutionTO.getAborted());
        assertNotNull(operatorExecutionTO.getStartedAt());
        assertNotNull(operatorExecutionTO.getFinishedAt());
    }

    @Test
    public void when_FinishOperatorOrder_With_NonExisting_Id_Then_NotFound() throws Exception {


        String operatorHeader = this.apiHelper.createAuthorizationHeaderFrom("operator","operator");
        OperatorExecutionTO operatorExecutionTO = this.apiHelper.finishOperatorOrderPUT(999L, operatorHeader, 404);

    }

    @Test
    public void when_AbortOperatorOrder_Then_OK() throws Exception {

        String editorHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorOrderTO order = orderHelper.buildCompleteEditorOrderTO("W001");
        order = this.apiHelper.createEditorOrderPOST(order, editorHeader, 201);

        String operatorHeader = this.apiHelper.createAuthorizationHeaderFrom("operator","operator");
        OperatorExecutionTO operatorExecutionTO = this.apiHelper.startOperatorOrderPOST(order.getId(), operatorHeader, 201);

        assertFalse(operatorExecutionTO.getAborted());
        assertNotNull(operatorExecutionTO.getStartedAt());
        assertNull(operatorExecutionTO.getFinishedAt());

        operatorExecutionTO = this.apiHelper.abortOperatorOrderPUT(operatorExecutionTO.getId(), operatorHeader, 201);
        assertTrue(operatorExecutionTO.getAborted());
        assertNotNull(operatorExecutionTO.getStartedAt());
        assertNull(operatorExecutionTO.getFinishedAt());
    }

    @Test
    public void when_AbortOperatorOrder_With_NonExisting_Id_Then_NotFound() throws Exception {


        String operatorHeader = this.apiHelper.createAuthorizationHeaderFrom("operator","operator");
        OperatorExecutionTO operatorExecutionTO = this.apiHelper.abortOperatorOrderPUT(999L, operatorHeader, 404);

    }

    @Test
    public void when_OrderIsDeleted_Executions_Persists() throws Exception{
        String editorHeader = this.apiHelper.createAuthorizationHeaderFrom("editor","editor");
        EditorOrderTO order = orderHelper.buildCompleteEditorOrderTO("W001");
        EditorProductTO productAfter = this.productHelper.buildCompleteEditorProductTO("P001");
        EditorProductTO productBefore = this.productHelper.buildCompleteEditorProductTO("P002");
        productAfter = this.apiHelper.createEditorProductPOST(productAfter, editorHeader, 201);
        productBefore = this.apiHelper.createEditorProductPOST(productBefore, editorHeader, 201);

        EditorEquipmentTO equipment1 = this.equipmentHelper.buildCompleteEditorEquipmentTO("E001");
        equipment1 = this.apiHelper.createEditorEquipmentPOST(equipment1, editorHeader, 201);
        EditorEquipmentTO equipment2 = this.equipmentHelper.buildCompleteEditorEquipmentTO("E002");
        equipment2 = this.apiHelper.createEditorEquipmentPOST(equipment2, editorHeader, 201);
        EditorEquipmentTO equipment3 = this.equipmentHelper.buildCompleteEditorEquipmentTO("E003");
        equipment3 = this.apiHelper.createEditorEquipmentPOST(equipment3, editorHeader, 201);

        order.setProductAfter(productAfter);
        order.setProductBefore(productBefore);
        order.getEquipment().add(equipment1);
        order.getEquipment().add(equipment2);
        order.getEquipment().add(equipment3);

        order = this.apiHelper.createEditorOrderPOST(order, editorHeader, 201);

        String operatorHeader = this.apiHelper.createAuthorizationHeaderFrom("operator","operator");
        OperatorExecutionTO operatorExecutionTO = this.apiHelper.startOperatorOrderPOST(order.getId(), operatorHeader, 201);
        operatorExecutionTO = this.apiHelper.finishOperatorOrderPUT(operatorExecutionTO.getId(), operatorHeader, 201);

        this.apiHelper.deleteEditorOrderDELETE(order.getId(), editorHeader, 204);
        this.executionRepository.existsById(operatorExecutionTO.getId());
    }

}
