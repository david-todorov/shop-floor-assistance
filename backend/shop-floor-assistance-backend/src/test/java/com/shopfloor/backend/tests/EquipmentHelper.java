package com.shopfloor.backend.tests;

import com.shopfloor.backend.api.transferobjects.editors.EditorEquipmentTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorOrderTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Helper class for building and comparing EditorEquipmentTO objects.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Component
public class EquipmentHelper {

    /**
     * Builds a complete EditorEquipmentTO object with predefined values.
     *
     * @param equipmentNumber the equipment number
     * @return the complete EditorEquipmentTO object
     */
    public EditorEquipmentTO buildCompleteEditorEquipmentTO(String equipmentNumber){
        EditorEquipmentTO editorEquipment = new EditorEquipmentTO();
        editorEquipment.setEquipmentNumber(equipmentNumber);
        editorEquipment.setName("BEC");
        editorEquipment.setType("500_123");
        editorEquipment.setDescription("High end Uhlmann packaging Machine Type BEC500");
        editorEquipment.setOrders(new ArrayList<EditorOrderTO>());

        return editorEquipment;
    }

    /**
     * Builds a list of complete EditorEquipmentTO objects with predefined values.
     *
     * @return the list of complete EditorEquipmentTO objects
     */
    public ArrayList<EditorEquipmentTO> buildCompleteEditorEquipmentTOs(){
        ArrayList<EditorEquipmentTO> equipments = new ArrayList<>();
        equipments.add(buildCompleteEditorEquipmentTO("E0001"));
        equipments.add(buildCompleteEditorEquipmentTO("E0002"));
        equipments.add(buildCompleteEditorEquipmentTO("E0003"));
        equipments.add(buildCompleteEditorEquipmentTO("E0004"));

        return equipments;
    }

    /**
     * Asserts that two lists of EditorEquipmentTO objects are equal.
     *
     * @param expected the expected list of EditorEquipmentTO objects
     * @param actual the actual list of EditorEquipmentTO objects
     */
    public void assertEditorEquipmentsListEqual(List<EditorEquipmentTO> expected, List<EditorEquipmentTO> actual){
        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            assertEditorEquipmentEqual(expected.get(i), actual.get(i));
        }
    }

    /**
     * Asserts that two EditorEquipmentTO objects are equal.
     *
     * @param expected the expected EditorEquipmentTO object
     * @param actual the actual EditorEquipmentTO object
     */
    public void assertEditorEquipmentEqual(EditorEquipmentTO expected, EditorEquipmentTO actual){
        if(expected.getId() != null && actual.getId() != null) {
            assertEquals(expected.getId(), actual.getId());
        }

        assertEquals(expected.getEquipmentNumber(), actual.getEquipmentNumber());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getType(), actual.getType());

        assertEquals(expected.getOrders().size(), actual.getOrders().size());
        for (int i = 0; i < actual.getOrders().size(); i++) {
            assertEditorProductsOrdersEqual(expected.getOrders().get(i), actual.getOrders().get(i));
        }

        assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
        assertEquals(expected.getCreatedBy(), actual.getCreatedBy());
        assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());
        assertEquals(expected.getUpdatedBy(), actual.getUpdatedBy());
    }

    /**
     * Asserts that two EditorOrderTO objects are equal.
     *
     * @param expected the expected EditorOrderTO object
     * @param actual the actual EditorOrderTO object
     */
    private void assertEditorProductsOrdersEqual(EditorOrderTO expected, EditorOrderTO actual){
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
    }
}
