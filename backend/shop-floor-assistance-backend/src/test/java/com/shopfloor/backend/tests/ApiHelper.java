package com.shopfloor.backend.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopfloor.backend.api.transferobjects.authentication.LoginUserRequestTO;
import com.shopfloor.backend.api.transferobjects.editors.*;
import com.shopfloor.backend.api.transferobjects.operators.OperatorExecutionTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorForecastTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorOrderTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ApiHelper is a helper class that provides methods to interact with the API.
 * It is used to perform requests to the API and retrieve the responses.
 *
 * This class uses the MockMvc object to perform the requests and the ObjectMapper to serialize and deserialize the request and response objects.
 *
 * The methods in this class are used in the integration tests to perform requests to the API and retrieve the responses.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Component
public class ApiHelper {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Authorizes a user by sending a login request.
     *
     * @param loginUser the login request transfer object
     * @param expectedStatus the expected HTTP status code
     * @throws Exception if an error occurs during the request
     */
    public void authorizeFor(LoginUserRequestTO loginUser, int expectedStatus) throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUser)))
                .andExpect(status().is(expectedStatus));
    }

    /**
     * Creates an authorization header from the given username and password.
     *
     * @param username the username
     * @param password the password
     * @return the authorization header
     * @throws Exception if an error occurs during the request
     */
    public String createAuthorizationHeaderFrom(String username, String password) throws Exception {
        LoginUserRequestTO request = new LoginUserRequestTO();
        request.setUsername(username);
        request.setPassword(password);

        String response = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        return "Bearer " + objectMapper.readTree(response).get("token").asText();
    }

    /**
     * Retrieves all orders for an editor.
     *
     * @param authorizationHeader the authorization header
     * @param expectedStatus the expected HTTP status code
     * @return a list of editor order transfer objects
     * @throws Exception if an error occurs during the request
     */
    public List<EditorOrderTO> getEditorAllOrdersGET(String authorizationHeader, int expectedStatus) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/editor/orders")
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse();

        if (response.getStatus() == 200) {
            String responseContent = response.getContentAsString();
            return objectMapper.readValue(responseContent, new TypeReference<List<EditorOrderTO>>() {});
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Creates a new order for an editor.
     *
     * @param editorOrderTO the editor order transfer object
     * @param authorizationHeader the authorization header
     * @param expectedStatus the expected HTTP status code
     * @return the created editor order transfer object
     * @throws Exception if an error occurs during the request
     */
    public EditorOrderTO createEditorOrderPOST(EditorOrderTO editorOrderTO, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(post("/editor/orders").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(editorOrderTO)).header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, EditorOrderTO.class);
    }

    /**
     * Updates an existing order for an editor.
     *
     * @param orderId the order ID
     * @param editorOrderTO the editor order transfer object
     * @param authorizationHeader the authorization header
     * @param expectedStatus the expected HTTP status code
     * @return the updated editor order transfer object
     * @throws Exception if an error occurs during the request
     */
    public EditorOrderTO updateEditorOrderPUT(Long orderId, EditorOrderTO editorOrderTO, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(put("/editor/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(editorOrderTO)).header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, EditorOrderTO.class);
    }

    /**
     * Deletes an existing order for an editor.
     *
     * @param orderId the order ID
     * @param authorizationHeader the authorization header
     * @param expectedStatus the expected HTTP status code
     * @throws Exception if an error occurs during the request
     */
    public void deleteEditorOrderDELETE(Long orderId, String authorizationHeader, int expectedStatus) throws Exception {
        mockMvc.perform(delete("/editor/orders/{id}", orderId)
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus));
    }

    /**
     * Retrieves a specific order for an editor.
     *
     * @param orderId the order ID
     * @param authorizationHeader the authorization header
     * @param expectedStatus the expected HTTP status code
     * @return the editor order transfer object
     * @throws Exception if an error occurs during the request
     */
    public EditorOrderTO getEditorOrderGET(Long orderId, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(get("/editor/orders/{id}", orderId)
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, EditorOrderTO.class);
    }

    /**
     * Retrieves all products for an editor.
     *
     * @param authorizationHeader the authorization header
     * @param expectedStatus the expected HTTP status code
     * @return a list of editor product transfer objects
     * @throws Exception if an error occurs during the request
     */
    public List<EditorProductTO> getEditorAllProductsGET(String authorizationHeader, int expectedStatus) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/editor/products")
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse();

        if (response.getStatus() == 200) {
            String responseContent = response.getContentAsString();
            return objectMapper.readValue(responseContent, new TypeReference<List<EditorProductTO>>() {});
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Creates a new product for an editor.
     *
     * @param editorProductTO the editor product transfer object
     * @param authorizationHeader the authorization header
     * @param expectedStatus the expected HTTP status code
     * @return the created editor product transfer object
     * @throws Exception if an error occurs during the request
     */
    public EditorProductTO createEditorProductPOST(EditorProductTO editorProductTO, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(post("/editor/products").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(editorProductTO)).header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, EditorProductTO.class);
    }

    /**
     * Updates an existing product for an editor.
     *
     * @param productId the product ID
     * @param editorOrderTO the editor product transfer object
     * @param authorizationHeader the authorization header
     * @param expectedStatus the expected HTTP status code
     * @return the updated editor product transfer object
     * @throws Exception if an error occurs during the request
     */
    public EditorProductTO updateEditorProductPUT(Long productId, EditorProductTO editorOrderTO, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(put("/editor/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(editorOrderTO)).header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, EditorProductTO.class);
    }

    /**
     * Deletes an existing product for an editor.
     *
     * @param productId the product ID
     * @param authorizationHeader the authorization header
     * @param expectedStatus the expected HTTP status code
     * @throws Exception if an error occurs during the request
     */
    public void deleteEditorProductDELETE(Long productId, String authorizationHeader, int expectedStatus) throws Exception {
        mockMvc.perform(delete("/editor/products/{id}", productId)
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus));
    }

    /**
     * Retrieves a specific product for an editor.
     *
     * @param productId the product ID
     * @param authorizationHeader the authorization header
     * @param expectedStatus the expected HTTP status code
     * @return the editor product transfer object
     * @throws Exception if an error occurs during the request
     */
    public EditorProductTO getEditorProductGET(Long productId, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(get("/editor/products/{id}", productId)
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, EditorProductTO.class);
    }

    /**
     * Retrieves all equipment for an editor.
     *
     * @param authorizationHeader the authorization header
     * @param expectedStatus the expected HTTP status code
     * @return a list of editor equipment transfer objects
     * @throws Exception if an error occurs during the request
     */
    public List<EditorEquipmentTO> getEditorAllEquipmentGET(String authorizationHeader, int expectedStatus) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/editor/equipment")
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse();

        if (response.getStatus() == 200) {
            String responseContent = response.getContentAsString();
            return objectMapper.readValue(responseContent, new TypeReference<List<EditorEquipmentTO>>() {});
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Creates new equipment for an editor.
     *
     * @param editorEquipmentTO the editor equipment transfer object
     * @param authorizationHeader the authorization header
     * @param expectedStatus the expected HTTP status code
     * @return the created editor equipment transfer object
     * @throws Exception if an error occurs during the request
     */
    public EditorEquipmentTO createEditorEquipmentPOST(EditorEquipmentTO editorEquipmentTO, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(post("/editor/equipment").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(editorEquipmentTO)).header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, EditorEquipmentTO.class);
    }

    /**
     * Updates existing equipment for an editor.
     *
     * @param equipmentId the equipment ID
     * @param editorEquipmentTO the editor equipment transfer object
     * @param authorizationHeader the authorization header
     * @param expectedStatus the expected HTTP status code
     * @return the updated editor equipment transfer object
     * @throws Exception if an error occurs during the request
     */
    public EditorEquipmentTO updateEditorEquipmentPUT(Long equipmentId, EditorEquipmentTO editorEquipmentTO, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(put("/editor/equipment/{id}", equipmentId)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(editorEquipmentTO)).header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, EditorEquipmentTO.class);
    }

    /**
     * Deletes existing equipment for an editor.
     *
     * @param equipmentId the equipment ID
     * @param authorizationHeader the authorization header
     * @param expectedStatus the expected HTTP status code
     * @throws Exception if an error occurs during the request
     */
    public void deleteEditorEquipmentDELETE(Long equipmentId, String authorizationHeader, int expectedStatus) throws Exception {
        mockMvc.perform(delete("/editor/equipment/{id}", equipmentId)
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus));
    }

    /**
     * Retrieves specific equipment for an editor.
     *
     * @param equipmentId the equipment ID
     * @param authorizationHeader the authorization header
     * @param expectedStatus the expected HTTP status code
     * @return the editor equipment transfer object
     * @throws Exception if an error occurs during the request
     */
    public EditorEquipmentTO getEditorEquipmentGET(Long equipmentId, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(get("/editor/equipment/{id}", equipmentId)
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, EditorEquipmentTO.class);
    }

    /**
     * Retrieves all orders for an operator.
     *
     * @param authorizationHeader the authorization header
     * @param expectedStatus the expected HTTP status code
     * @return a list of operator order transfer objects
     * @throws Exception if an error occurs during the request
     */
    public List<OperatorOrderTO> getOperatorAllOrdersGET(String authorizationHeader, int expectedStatus) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/operator/orders")
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse();

        if (response.getStatus() == 200) {
            String responseContent = response.getContentAsString();
            return objectMapper.readValue(responseContent, new TypeReference<List<OperatorOrderTO>>() {});
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves a specific order for an operator.
     *
     * @param orderId the order ID
     * @param authorizationHeader the authorization header
     * @param expectedStatus the expected HTTP status code
     * @return the operator order transfer object
     * @throws Exception if an error occurs during the request
     */
    public OperatorOrderTO getOperatorOrderGET(Long orderId, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(get("/operator/orders/{id}", orderId)
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, OperatorOrderTO.class);
    }



    public List<EditorProductTO> getEditorProductSuggestionsGET(String authorizationHeader, int expectedStatus) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/editor/products/suggestions")
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse();

        if (response.getStatus() == 200) {
            String responseContent = response.getContentAsString();
            return objectMapper.readValue(responseContent, new TypeReference<List<EditorProductTO>>() {});
        } else {
            return Collections.emptyList();
        }
    }

    public List<EditorEquipmentTO> getEditorEquipmentSuggestionsGET(String authorizationHeader, int expectedStatus) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/editor/equipment/suggestions")
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse();

        if (response.getStatus() == 200) {
            String responseContent = response.getContentAsString();
            return objectMapper.readValue(responseContent, new TypeReference<List<EditorEquipmentTO>>() {});
        } else {
            return Collections.emptyList();
        }
    }

    public List<EditorWorkflowTO> getEditorWorkflowSuggestionsPOST(EditorProductTO productAfter,String authorizationHeader, int expectedStatus) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post("/editor/workflows/suggestions")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(productAfter)).header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse();

        if (response.getStatus() == 200) {
            String responseContent = response.getContentAsString();
            return objectMapper.readValue(responseContent, new TypeReference<List<EditorWorkflowTO>>() {});
        } else {
            return Collections.emptyList();
        }
    }

    public List<EditorTaskTO> getEditorTasksSuggestionsPOST(EditorProductTO productAfter,String authorizationHeader, int expectedStatus) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post("/editor/tasks/suggestions")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(productAfter)).header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse();

        if (response.getStatus() == 200) {
            String responseContent = response.getContentAsString();
            return objectMapper.readValue(responseContent, new TypeReference<List<EditorTaskTO>>() {});
        } else {
            return Collections.emptyList();
        }
    }


    public List<EditorItemTO> getEditorItemsSuggestionsPOST(EditorProductTO productAfter,String authorizationHeader, int expectedStatus) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post("/editor/items/suggestions")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(productAfter)).header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse();

        if (response.getStatus() == 200) {
            String responseContent = response.getContentAsString();
            return objectMapper.readValue(responseContent, new TypeReference<List<EditorItemTO>>() {});
        } else {
            return Collections.emptyList();
        }
    }

    public OperatorForecastTO getOperatorForecastGET(Long orderId, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(get("/operator/forecast/{orderId}", orderId)
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, OperatorForecastTO.class);
    }

    public OperatorExecutionTO startOperatorOrderPOST(Long orderId, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(post("/operator/start/{orderId}", orderId)
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, OperatorExecutionTO.class);
    }

    public OperatorExecutionTO finishOperatorOrderPUT(Long executionId, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(put("/operator/finish/{executionId}", executionId)
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, OperatorExecutionTO.class);
    }

    public OperatorExecutionTO abortOperatorOrderPUT(Long executionId, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(put("/operator/abort/{executionId}", executionId)
                        .header("Authorization", authorizationHeader))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, OperatorExecutionTO.class);
    }
}
