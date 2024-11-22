package com.shopfloor.backend.tests;

import com.shopfloor.backend.api.transferobjects.editors.EditorOrderTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorProductTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Helper class for building and comparing EditorProductTO objects.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Component
public class ProductHelper {

    /**
     * Builds a complete EditorProductTO object with predefined values.
     *
     * @param productNumber the product number
     * @return the complete EditorProductTO object
     */
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
        editorProduct.setOrdersBefore(new ArrayList<EditorOrderTO>());
        editorProduct.setOrdersAfter(new ArrayList<EditorOrderTO>());

        return editorProduct;
    }

    /**
     * Asserts that two EditorProductTO objects are equal.
     *
     * @param expected the expected EditorProductTO object
     * @param actual the actual EditorProductTO object
     */
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

        assertEquals(expected.getOrdersAfter().size(), actual.getOrdersAfter().size());
        for (int i = 0; i < actual.getOrdersAfter().size(); i++) {
            assertEditorProductsOrdersEqual(expected.getOrdersAfter().get(i), actual.getOrdersAfter().get(i));
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
