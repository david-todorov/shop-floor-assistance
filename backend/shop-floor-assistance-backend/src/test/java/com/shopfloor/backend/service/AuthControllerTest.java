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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    public void when_LoginWithValidEditorCredentials_ThenReturnsToken() throws Exception {
        LoginUserRequestTO request = new LoginUserRequestTO();
        request.setUsername("editor");
        request.setPassword("editor");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.expiresAt").exists());
    }

    @Test
    public void when_LoginWithValidOperatorCredentials_ThenReturnsToken() throws Exception {
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
    public void when_LoginWithValidEditorUsernameButWrongPassword_ThenReturnsUnauthorized() throws Exception {
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
    public void when_LoginWithValidOperatorUsernameButWrongPassword_ThenReturnsUnauthorized() throws Exception {
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
    public void when_LoginWithInvalidUsername_ThenReturnsUnauthorized() throws Exception {
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

    private String loginAndGetToken(String username, String password) throws Exception {
        LoginUserRequestTO request = new LoginUserRequestTO();
        request.setUsername(username);
        request.setPassword(password);

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("token").asText();
    }

    @Test
    public void when_OperatorAccessesOperatorEndpoint_ThenSuccess() throws Exception {
        String token = loginAndGetToken("operator", "operator");

        mockMvc.perform(get("/operator/orders")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());  // Replace with actual success status expected
    }

    @Test
    public void when_OperatorAccessesEditorEndpoint_ThenForbidden() throws Exception {
        String token = loginAndGetToken("operator", "operator");

        mockMvc.perform(get("/editor/orders")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());  // Expecting 403 Forbidden
    }

    @Test
    public void when_EditorAccessesEditorEndpoint_ThenSuccess() throws Exception {
        String token = loginAndGetToken("editor", "editor");

        mockMvc.perform(get("/editor/orders")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void when_EditorAccessesOperatorEndpoint_ThenSuccess() throws Exception {
        String token = loginAndGetToken("editor", "editor");

        mockMvc.perform(get("/operator/orders")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
