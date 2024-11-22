package com.shopfloor.backend.tests;

import com.shopfloor.backend.api.transferobjects.authentication.LoginUserRequestTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration tests for the authentication controller.
 *
 * This class contains tests for various authentication scenarios, including
 * login with valid and invalid credentials, and access control for different
 * user roles.
 * @author David Todorov (https://github.com/david-todorov)
 */
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    /**
     * Helper for API-related operations.
     */
    @Autowired
    private ApiHelper apiHelper;

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void when_LoginWithValidEditorCredentials_ThenReturnsToken() throws Exception {
        LoginUserRequestTO request = new LoginUserRequestTO();
        request.setUsername("editor");
        request.setPassword("editor");

        this.apiHelper.authorizeFor(request, 200);
    }

    @Test
    public void when_LoginWithValidOperatorCredentials_ThenReturnsToken() throws Exception {
        LoginUserRequestTO request = new LoginUserRequestTO();
        request.setUsername("operator");
        request.setPassword("operator");

        this.apiHelper.authorizeFor(request, 200);
    }

    @Test
    public void when_LoginWithValidEditorUsernameButWrongPassword_ThenReturnsUnauthorized() throws Exception {
        LoginUserRequestTO request = new LoginUserRequestTO();
        request.setUsername("editor");
        request.setPassword("wrongPassword"); // Use an incorrect password

        this.apiHelper.authorizeFor(request, 401);
    }

    @Test
    public void when_LoginWithValidOperatorUsernameButWrongPassword_ThenReturnsUnauthorized() throws Exception {
        LoginUserRequestTO request = new LoginUserRequestTO();
        request.setUsername("operator");
        request.setPassword("invalid"); // Use an incorrect password

        this.apiHelper.authorizeFor(request, 401);
    }

    @Test
    public void when_LoginWithInvalidUsername_ThenReturnsUnauthorized() throws Exception {
        LoginUserRequestTO request = new LoginUserRequestTO();
        request.setUsername("invalid");
        request.setPassword("operator");

        this.apiHelper.authorizeFor(request, 401);
    }

    @Test
    public void when_OperatorAccessesOperatorEndpoint_ThenSuccess() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("operator", "operator");
        this.apiHelper.getOperatorAllOrdersGET(authorizationHeader, 200);
    }

    @Test
    public void when_OperatorAccessesEditorEndpoint_ThenForbidden() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("operator", "operator");
        this.apiHelper.getEditorAllOrdersGET(authorizationHeader, 403);
    }

    @Test
    public void when_EditorAccessesEditorEndpoint_ThenSuccess() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        this.apiHelper.getEditorAllOrdersGET(authorizationHeader, 200);
    }

    @Test
    public void when_EditorAccessesOperatorEndpoint_ThenSuccess() throws Exception {
        String authorizationHeader = this.apiHelper.createAuthorizationHeaderFrom("editor", "editor");
        this.apiHelper.getOperatorAllOrdersGET(authorizationHeader, 200);
    }
}
