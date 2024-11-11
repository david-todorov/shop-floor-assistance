package com.shopfloor.backend.services;

import com.shopfloor.backend.api.transferobjects.editors.EditorEquipmentTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorOrderTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorProductTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorWorkflowTO;

import java.util.List;

/**
 * This is where all needed public methods should be declared
 * Think what EditorController would need and declare it here
 * Be generic not concrete
 * Rely on actions not implementations
 * Keep the number of methods low as possible and if
 * something does not feel right,
 * probably should be private and not here
 * Have fun
 */
public interface EditorService {

    /**
     * ORDERS
     */
    List<EditorOrderTO> getAllOrders();

    EditorOrderTO addOrder(EditorOrderTO newEditorOrderTO);

    EditorOrderTO updateOrder(Long orderId, EditorOrderTO editorOrderTO);

    EditorOrderTO getOrder(Long orderId);

    void deleteOrder(Long orderId);

    /**
     * PRODUCTS
     */
    List<EditorProductTO> getAllProducts();

    EditorProductTO addProduct(EditorProductTO newEditorProductTO);

    EditorProductTO updateProduct(Long productId, EditorProductTO editorProductTO);

    EditorProductTO getProduct(Long productId);

    void deleteProduct(Long productId);


    /**
     * EQIPMENT
     */
    List<EditorEquipmentTO> getAllEquipment();

    EditorEquipmentTO addEquipment(EditorEquipmentTO newEditorEquipmentTO);

    EditorEquipmentTO updateEquipment(Long equipmentId, EditorEquipmentTO editorEquipmentTO);

    EditorEquipmentTO getEquipment(Long equipmentId);

    void deleteEquipment(Long equipmentId);

    /**
     * SUGGESTIONS
     */
    List<EditorEquipmentTO> getEquipmentSuggestions(int numberOfEquipments);

    List<EditorProductTO> getProductsSuggestions(int numberOfProducts);

    List<EditorWorkflowTO> getWorkflowSuggestions(EditorProductTO productAfter);
}
