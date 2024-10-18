package com.shopfloor.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopfloor.backend.api.transferobjects.LoginUserRequestTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenLoginWithValidEditorCredentials_thenReturnsToken() throws Exception {
        LoginUserRequestTO request = new LoginUserRequestTO();
        request.setUsername("editor");
        request.setPassword("editor"); // Replace with the actual password

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.expiresAt").exists());
    }

    @Test
    public void whenLoginWithValidOperatorCredentials_thenReturnsToken() throws Exception {
        LoginUserRequestTO request = new LoginUserRequestTO();
        request.setUsername("operator");
        request.setPassword("operator");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.expiresAt").exists());
    }

    @Test
    public void whenLoginWithValidEditorUsernameButWrongPassword_thenReturnsUnauthorized() throws Exception {
        LoginUserRequestTO request = new LoginUserRequestTO();
        request.setUsername("editor");
        request.setPassword("wrongPassword"); // Use an incorrect password

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title").value("Unauthorized"))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.detail").value("Bad credentials"))
                .andExpect(jsonPath("$.description").value("The username or password is incorrect"));
    }

    @Test
    public void whenLoginWithValidOperatorUsernameButWrongPassword_thenReturnsUnauthorized() throws Exception {
        LoginUserRequestTO request = new LoginUserRequestTO();
        request.setUsername("operator");
        request.setPassword("invalid"); // Use an incorrect password

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title").value("Unauthorized"))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.detail").value("Bad credentials"))
                .andExpect(jsonPath("$.description").value("The username or password is incorrect"));
    }
    @Test
    public void whenLoginWithInvalidUsername_thenReturnsUnauthorized() throws Exception {
        LoginUserRequestTO request = new LoginUserRequestTO();
        request.setUsername("invalid");
        request.setPassword("operator");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title").value("Unauthorized"))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.detail").value("Bad credentials"))
                .andExpect(jsonPath("$.description").value("The username or password is incorrect"));
    }
}
