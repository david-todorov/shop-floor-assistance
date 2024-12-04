package com.shopfloor.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopfloor.backend.api.transferobjects.authentication.LoginUserRequestTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorEquipmentTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorOrderTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorProductTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorOrderTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class ApiHelper {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public void authorizeFor(LoginUserRequestTO loginUser, int expectedStatus) throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUser)))
                .andExpect(status().is(expectedStatus));
    }

    public String createAuthorizationHeaderFrom(String username, String password) throws Exception {
        LoginUserRequestTO request = new LoginUserRequestTO();
        request.setUsername(username);
        request.setPassword(password);

        String response = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        return "Bearer " + objectMapper.readTree(response).get("token").asText();
    }

    /**
     * EDITOR
     * ORDERS
     */
    public List<EditorOrderTO> getEditorAllOrdersGET(String authorizationHeader, int expectedStatus) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/editor/orders")
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse();

        // Only attempt to parse the response body for successful status codes
        if (response.getStatus() == 200) {
            String responseContent = response.getContentAsString();
            return objectMapper.readValue(responseContent, new TypeReference<List<EditorOrderTO>>() {});
        } else {
            // Return an empty list or handle the error case
            return Collections.emptyList(); // or throw an exception if you prefer
        }
    }

    public EditorOrderTO createEditorOrderPOST(EditorOrderTO editorOrderTO, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(post("/editor/orders").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(editorOrderTO)).header("Authorization", authorizationHeader)) // Add the authorization header
                .andExpect(status().is(expectedStatus)) // Expect the provided status
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, EditorOrderTO.class);
    }

    public EditorOrderTO updateEditorOrderPUT(Long orderId, EditorOrderTO editorOrderTO, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(put("/editor/orders/{id}", orderId) // Include the order ID as a path variable
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(editorOrderTO)).header("Authorization", authorizationHeader)) // Add the authorization header
                .andExpect(status().is(expectedStatus)) // Expect the provided status
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, EditorOrderTO.class);
    }

    public void deleteEditorOrderDELETE(Long orderId, String authorizationHeader, int expectedStatus) throws Exception {
        mockMvc.perform(delete("/editor/orders/{id}", orderId) // Include the order ID as a path variable
                        .header("Authorization", authorizationHeader)) // Add the authorization header
                .andExpect(status().is(expectedStatus)); // Expect the provided status
    }

    public EditorOrderTO getEditorOrderGET(Long orderId, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(get("/editor/orders/{id}", orderId) // Include the order ID as a path variable
                        .header("Authorization", authorizationHeader)) // Add the authorization header
                .andExpect(status().is(expectedStatus)) // Expect the provided status
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, EditorOrderTO.class);
    }

    /**
     * EDITOR
     * PRODUCTS
     */
    public List<EditorProductTO> getEditorAllProductsGET(String authorizationHeader, int expectedStatus) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/editor/products")
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse();

        // Only attempt to parse the response body for successful status codes
        if (response.getStatus() == 200) {
            String responseContent = response.getContentAsString();
            return objectMapper.readValue(responseContent, new TypeReference<List<EditorProductTO>>() {});
        } else {
            // Return an empty list or handle the error case
            return Collections.emptyList(); // or throw an exception if you prefer
        }
    }

    public EditorProductTO createEditorProductPOST(EditorProductTO editorProductTO, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(post("/editor/products").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(editorProductTO)).header("Authorization", authorizationHeader)) // Add the authorization header
                .andExpect(status().is(expectedStatus)) // Expect the provided status
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, EditorProductTO.class);
    }

    public EditorProductTO updateEditorProductPUT(Long productId, EditorProductTO editorOrderTO, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(put("/editor/products/{id}", productId) // Include the order ID as a path variable
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(editorOrderTO)).header("Authorization", authorizationHeader)) // Add the authorization header
                .andExpect(status().is(expectedStatus)) // Expect the provided status
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, EditorProductTO.class);
    }

    public void deleteEditorProductDELETE(Long productId, String authorizationHeader, int expectedStatus) throws Exception {
        mockMvc.perform(delete("/editor/products/{id}", productId) // Include the order ID as a path variable
                        .header("Authorization", authorizationHeader)) // Add the authorization header
                .andExpect(status().is(expectedStatus)); // Expect the provided status
    }

    public EditorProductTO getEditorProductGET(Long productId, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(get("/editor/products/{id}", productId) // Include the order ID as a path variable
                        .header("Authorization", authorizationHeader)) // Add the authorization header
                .andExpect(status().is(expectedStatus)) // Expect the provided status
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, EditorProductTO.class);
    }

    /**
     * EDITOR
     * EQUIPMENT
     */

    public List<EditorEquipmentTO> getEditorAllEquipmentGET(String authorizationHeader, int expectedStatus) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/editor/equipment")
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse();

        // Only attempt to parse the response body for successful status codes
        if (response.getStatus() == 200) {
            String responseContent = response.getContentAsString();
            return objectMapper.readValue(responseContent, new TypeReference<List<EditorEquipmentTO>>() {});
        } else {
            // Return an empty list or handle the error case
            return Collections.emptyList(); // or throw an exception if you prefer
        }
    }

    public EditorEquipmentTO createEditorEquipmentPOST(EditorEquipmentTO editorEquipmentTO, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(post("/editor/equipment").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(editorEquipmentTO)).header("Authorization", authorizationHeader)) // Add the authorization header
                .andExpect(status().is(expectedStatus)) // Expect the provided status
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, EditorEquipmentTO.class);
    }

    public EditorEquipmentTO updateEditorEquipmentPUT(Long equipmentId, EditorEquipmentTO editorEquipmentTO, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(put("/editor/equipment/{id}", equipmentId) // Include the order ID as a path variable
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(editorEquipmentTO)).header("Authorization", authorizationHeader)) // Add the authorization header
                .andExpect(status().is(expectedStatus)) // Expect the provided status
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, EditorEquipmentTO.class);
    }

    public void deleteEditorEquipmentDELETE(Long equipmentId, String authorizationHeader, int expectedStatus) throws Exception {
        mockMvc.perform(delete("/editor/equipment/{id}", equipmentId) // Include the order ID as a path variable
                        .header("Authorization", authorizationHeader)) // Add the authorization header
                .andExpect(status().is(expectedStatus)); // Expect the provided status
    }

    public EditorEquipmentTO getEditorEquipmentGET(Long equipmentId, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(get("/editor/equipment/{id}", equipmentId) // Include the order ID as a path variable
                        .header("Authorization", authorizationHeader)) // Add the authorization header
                .andExpect(status().is(expectedStatus)) // Expect the provided status
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, EditorEquipmentTO.class);
    }

    /**
     * OPERATOR
     * ORDERS
     */
    public List<OperatorOrderTO> getOperatorAllOrdersGET(String authorizationHeader, int expectedStatus) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/operator/orders")
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse();

        // Only attempt to parse the response body for successful status codes
        if (response.getStatus() == 200) {
            String responseContent = response.getContentAsString();
            return objectMapper.readValue(responseContent, new TypeReference<List<OperatorOrderTO>>() {});
        } else {
            // Return an empty list or handle the error case
            return Collections.emptyList(); // or throw an exception if you prefer
        }
    }

    public OperatorOrderTO getOperatorOrderGET(Long orderId, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(get("/operator/orders/{id}", orderId) // Include the order ID as a path variable
                        .header("Authorization", authorizationHeader)) // Add the authorization header
                .andExpect(status().is(expectedStatus)) // Expect the provided status
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, OperatorOrderTO.class);
    }

}
