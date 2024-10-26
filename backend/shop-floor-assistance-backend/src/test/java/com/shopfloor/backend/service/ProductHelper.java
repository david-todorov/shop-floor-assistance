package com.shopfloor.backend.service;

import com.shopfloor.backend.api.transferobjects.editors.EditorOrderTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorProductTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Component
public class ProductHelper {

    //TODO
    public EditorProductTO buildCompleteEditorProductTO(String productNumber){
        EditorProductTO editorProduct = new EditorProductTO();

        // Hardcoded values
        editorProduct.setProductNumber(productNumber);
        editorProduct.setName("Aspirin");
        editorProduct.setType("XYZ");
        editorProduct.setCountry("Germany");
        editorProduct.setPackageSize("8x2");
        editorProduct.setPackageType("Blister");
        editorProduct.setLanguage("German language");
        editorProduct.setDescription("Pain reliever used to reduce fever and relieve minor aches and pains.");
        // Initialize orders list (empty but not null)
        editorProduct.setOrders(new ArrayList<EditorOrderTO>());

        return editorProduct;
    }

    //TODO
    public void assertEditorProductsEqual(EditorProductTO expected, EditorProductTO actual){
        if(expected.getId() != null && actual.getId() != null) {
            assertEquals(expected.getId(), actual.getId());
        }

        assertEquals(expected.getProductNumber(), actual.getProductNumber());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getPackageSize(), actual.getPackageSize());
        assertEquals(expected.getPackageType(), actual.getPackageType());
        assertEquals(expected.getLanguage(), actual.getLanguage());

        assertEquals(expected.getOrders().size(), actual.getOrders().size());
        for (int i = 0; i < actual.getOrders().size(); i++) {
            assertEditorProductsOrdersEqual(expected.getOrders().get(i), actual.getOrders().get(i));
        }

        assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
        assertEquals(expected.getCreatedBy(), actual.getCreatedBy());
        assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());
        assertEquals(expected.getUpdatedBy(), actual.getUpdatedBy());
    }

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
