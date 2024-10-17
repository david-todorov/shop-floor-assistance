package com.shopfloor.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopfloor.backend.api.transferobjects.LoginUserRequestTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class ShopFloorAssistanceBackendApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Test
    void testSuccessfulLogin() throws Exception {
        // Arrange
        LoginUserRequestTO request = new LoginUserRequestTO("operator", "operator");

        // Act and Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists()) // Check if token exists in the response
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.expiresAt").exists());
    }

}
