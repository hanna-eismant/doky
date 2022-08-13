package org.hkurh.doky.controllers;

import org.apache.commons.lang3.StringUtils;
import org.hkurh.doky.security.AuthenticationResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Login/Register endpoint integration test")
class UserControllerIntegrationTest extends AbstractIntegrationTest {

    private static final String INCORRECT_USER_UID = ".test";
    private static final String INCORRECT_USER_PASSWORD = "pass";
    private static final String NEW_USER_UID = "new.user.test";
    private static final String NEW_USER_PASSWORD = "pass";
    private String loginEndpoint;
    private String registerEndpoint;

    @BeforeEach
    void setupEach() throws SQLException {
//        createBaseTestData();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        loginEndpoint = BASE_HOST + port + "/login";
        registerEndpoint = BASE_HOST + port + "/register";
    }

    @AfterEach
    void clearEach() throws SQLException {
//        cleanupBaseTestData();
    }

    @Test
    @DisplayName("Should receive token for valid user when login")
    @SqlGroup({
            @Sql(scripts = "classpath:sql/LoginControllerIntegrationTest/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void shouldCreateToken_whenExistingUserLogin() throws JSONException {
        var loginBody = new JSONObject();
        loginBody.put(USERNAME_PROPERTY, VALID_USER_UID);
        loginBody.put(PASSWORD_PROPERTY, VALID_USER_PASSWORD);

        var requestEntity = new HttpEntity<>(loginBody.toString(), httpHeaders);
        var responseEntity = restTemplate.exchange(loginEndpoint, HttpMethod.POST, requestEntity, AuthenticationResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody(), "Response body is empty");
        assertNotNull(responseEntity.getBody().getToken(), "Received token is null");
        assertNotEquals(StringUtils.EMPTY, responseEntity.getBody().getToken(), "Received token is empty");
    }

    @Test
    @DisplayName("Should return error when credentials are incorrect when login")
    void shouldReturnError_whenIncorrectCredentials() throws JSONException {
        var loginBody = new JSONObject();
        loginBody.put(USERNAME_PROPERTY, INCORRECT_USER_UID);
        loginBody.put(PASSWORD_PROPERTY, INCORRECT_USER_PASSWORD);

        var requestEntity = new HttpEntity<>(loginBody.toString(), httpHeaders);
        var responseEntity = restTemplate.exchange(loginEndpoint, HttpMethod.POST, requestEntity, ErrorResponse.class);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody(), "Response body is empty");

        var error = responseEntity.getBody().getError();
        assertNotNull(error, "Error in response cannot be null");

        var message = error.getMessage();
        assertNotNull(message, "Error message in response cannot be null");
    }

    @Test
    @DisplayName("Should register user when it does not exists")
    @SqlGroup({
            @Sql(scripts = "classpath:sql/LoginControllerIntegrationTest/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void shouldRegister_whenUserDoesNotExists() throws JSONException {
        var loginBody = new JSONObject();
        loginBody.put(USERNAME_PROPERTY, NEW_USER_UID);
        loginBody.put(PASSWORD_PROPERTY, NEW_USER_PASSWORD);

        var requestEntity = new HttpEntity<>(loginBody.toString(), httpHeaders);
        var responseEntity = restTemplate.exchange(registerEndpoint, HttpMethod.POST, requestEntity, Object.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertTrue(responseEntity.getHeaders().containsKey(LOCATION_HEADER), "Response should contain Location header");
        assertTrue(StringUtils.isNotBlank(responseEntity.getHeaders().get(LOCATION_HEADER).get(0)), "Header Location should not be empty");
    }

    @Test
    @DisplayName("Should return error when register with existing user")
    @SqlGroup({
            @Sql(scripts = "classpath:sql/LoginControllerIntegrationTest/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void shouldReturnError_whenUserAlreadyExists() throws JSONException {
        var loginBody = new JSONObject();
        loginBody.put(USERNAME_PROPERTY, VALID_USER_UID);
        loginBody.put(PASSWORD_PROPERTY, NEW_USER_PASSWORD);

        var requestEntity = new HttpEntity<>(loginBody.toString(), httpHeaders);
        var responseEntity = restTemplate.exchange(loginEndpoint, HttpMethod.POST, requestEntity, ErrorResponse.class);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody(), "Response body is empty");

        var error = responseEntity.getBody().getError();
        assertNotNull(error, "Error in response cannot be null");

        var message = error.getMessage();
        assertNotNull(message, "Error message in response cannot be null");
    }
}
