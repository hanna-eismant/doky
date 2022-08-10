package org.hkurh.doky.controllers;

import org.apache.commons.lang3.StringUtils;
import org.hkurh.doky.dto.UserDto;
import org.hkurh.doky.security.AuthenticationResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Login/Register endpoint integration test")
class UserControllerIT {

    private static final String USERNAME_PROPERTY = "username";
    private static final String PASSWORD_PROPERTY = "password";
    private static final String VALID_USER_UID = "hanna.test";
    private static final String VALID_USER_PASSWORD = "pass123";
    private static final String VALID_USER_NAME = "Hanna";
    private static final String INCORRECT_USER_UID = ".test";
    private static final String INCORRECT_USER_PASSWORD = "pass";
    private static final String NEW_USER_UID = "new.user.test";
    private static final String NEW_USER_PASSWORD = "pass";
    private final HttpHeaders httpHeaders = new HttpHeaders();
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    private String loginEndpoint;
    private String registerEndpoint;

    @BeforeEach
    void setup() {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        loginEndpoint = "http://localhost:" + port + "/login";
        registerEndpoint = "http://localhost:" + port + "/register";
    }

    @Test
    @DisplayName("Should receive token for valid user when login")
    @SqlGroup({
            @Sql(scripts = "classpath:sql/LoginControllerIntegrationTest/setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
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
            @Sql(scripts = "classpath:sql/LoginControllerIntegrationTest/setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:sql/LoginControllerIntegrationTest/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void shouldRegister_whenUserDoesNotExists() throws JSONException {
        var loginBody = new JSONObject();
        loginBody.put(USERNAME_PROPERTY, NEW_USER_UID);
        loginBody.put(PASSWORD_PROPERTY, NEW_USER_PASSWORD);

        var requestEntity = new HttpEntity<>(loginBody.toString(), httpHeaders);
        var responseEntity = restTemplate.exchange(registerEndpoint, HttpMethod.POST, requestEntity, UserDto.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody(), "Response body is empty");

        var actualUserUid = responseEntity.getBody().getUserUid();
        assertEquals(NEW_USER_UID, actualUserUid, "User UID is incorrect");
    }

    @Test
    @DisplayName("Should return error when register with existing user")
    @SqlGroup({
            @Sql(scripts = "classpath:sql/LoginControllerIntegrationTest/setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:sql/LoginControllerIntegrationTest/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void test() throws JSONException {
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
