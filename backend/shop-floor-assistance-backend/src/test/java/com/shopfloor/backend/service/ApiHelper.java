package com.shopfloor.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopfloor.backend.api.transferobjects.authentication.LoginUserRequestTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorOrderTO;
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

    public EditorOrderTO getEditorOrderGET(Long orderId, String authorizationHeader, int expectedStatus) throws Exception {
        String responseContent = mockMvc.perform(get("/editor/orders/{id}", orderId) // Include the order ID as a path variable
                        .header("Authorization", authorizationHeader)) // Add the authorization header
                .andExpect(status().is(expectedStatus)) // Expect the provided status
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, EditorOrderTO.class);
    }

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

    public void deleteEditorOrderDELETE(Long orderId, String authorizationHeader, int expectedStatus) throws Exception {
        mockMvc.perform(delete("/editor/orders/{id}", orderId) // Include the order ID as a path variable
                        .header("Authorization", authorizationHeader)) // Add the authorization header
                .andExpect(status().is(expectedStatus)); // Expect the provided status
    }

    public String createAuthorizationHeaderFrom(String username, String password) throws Exception {
        LoginUserRequestTO request = new LoginUserRequestTO();
        request.setUsername(username);
        request.setPassword(password);

        String response = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        return "Bearer " + objectMapper.readTree(response).get("token").asText();
    }

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
