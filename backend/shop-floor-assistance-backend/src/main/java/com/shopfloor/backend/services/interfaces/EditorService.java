package com.shopfloor.backend.services.interfaces;

import com.shopfloor.backend.api.transferobjects.editors.*;

import java.util.List;

/**
 * EditorService is an interface for the EditorController.
 * It provides methods to perform editor operations.
 * @author David Todorov (https://github.com/david-todorov)
 */
public interface EditorService {

    /**
     * Retrieves all orders.
     * @return a list of all orders.
     */
    List<EditorOrderTO> getAllOrders();

    /**
     * Adds a new order.
     * @param newEditorOrderTO the new order to add.
     * @return the added order.
     */
    EditorOrderTO addOrder(EditorOrderTO newEditorOrderTO);

    /**
     * Updates an existing order.
     * @param orderId the ID of the order to update.
     * @param editorOrderTO the updated order information.
     * @return the updated order.
     */
    EditorOrderTO updateOrder(Long orderId, EditorOrderTO editorOrderTO);

    /**
     * Retrieves an order by its ID.
     * @param orderId the ID of the order to retrieve.
     * @return the retrieved order.
     */
    EditorOrderTO getOrder(Long orderId);

    /**
     * Deletes an order by its ID.
     * @param orderId the ID of the order to delete.
     */
    void deleteOrder(Long orderId);

    /**
     * Retrieves all products.
     * @return a list of all products.
     */
    List<EditorProductTO> getAllProducts();

    /**
     * Adds a new product.
     * @param newEditorProductTO the new product to add.
     * @return the added product.
     */
    EditorProductTO addProduct(EditorProductTO newEditorProductTO);

    /**
     * Updates an existing product.
     * @param productId the ID of the product to update.
     * @param editorProductTO the updated product information.
     * @return the updated product.
     */
    EditorProductTO updateProduct(Long productId, EditorProductTO editorProductTO);

    /**
     * Retrieves a product by its ID.
     * @param productId the ID of the product to retrieve.
     * @return the retrieved product.
     */
    EditorProductTO getProduct(Long productId);

    /**
     * Deletes a product by its ID.
     * @param productId the ID of the product to delete.
     */
    void deleteProduct(Long productId);

    /**
     * Retrieves all equipment.
     * @return a list of all equipment.
     */
    List<EditorEquipmentTO> getAllEquipment();

    /**
     * Adds new equipment.
     * @param newEditorEquipmentTO the new equipment to add.
     * @return the added equipment.
     */
    EditorEquipmentTO addEquipment(EditorEquipmentTO newEditorEquipmentTO);

    /**
     * Updates existing equipment.
     * @param equipmentId the ID of the equipment to update.
     * @param editorEquipmentTO the updated equipment information.
     * @return the updated equipment.
     */
    EditorEquipmentTO updateEquipment(Long equipmentId, EditorEquipmentTO editorEquipmentTO);

    /**
     * Retrieves equipment by its ID.
     * @param equipmentId the ID of the equipment to retrieve.
     * @return the retrieved equipment.
     */
    EditorEquipmentTO getEquipment(Long equipmentId);

    /**
     * Deletes equipment by its ID.
     * @param equipmentId the ID of the equipment to delete.
     */
    void deleteEquipment(Long equipmentId);

    /**
     * Retrieves equipment suggestions.
     * @param numberOfEquipments the number of equipment suggestions to retrieve.
     * @return a list of equipment suggestions.
     */
    List<EditorEquipmentTO> getEquipmentSuggestions(int numberOfEquipments);

    /**
     * Retrieves product suggestions.
     * @param numberOfProducts the number of product suggestions to retrieve.
     * @return a list of product suggestions.
     */
    List<EditorProductTO> getProductsSuggestions(int numberOfProducts);

    /**
     * Retrieves workflow suggestions based on the product after.
     * @param productAfter the product after which to base the workflow suggestions.
     * @return a list of workflow suggestions.
     */
    List<EditorWorkflowTO> getWorkflowsSuggestions(EditorProductTO productAfter);

    /**
     * Retrieves task suggestions based on the product after.
     * @param productAfter the product after which to base the task suggestions.
     * @return a list of task suggestions.
     */
    List<EditorTaskTO> getTasksSuggestions(EditorProductTO productAfter);

    /**
     * Retrieves item suggestions based on the product after.
     * @param productAfter the product after which to base the item suggestions.
     * @return a list of item suggestions.
     */
    List<EditorItemTO> getItemsSuggestions(EditorProductTO productAfter);
}

