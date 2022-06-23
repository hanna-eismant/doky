package org.hkurh.doky.controllers;

import org.apache.commons.lang3.StringUtils;
import org.hkurh.doky.dto.UserDto;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Login/Register endpoint integration test")
public class UserControllerIT {

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
    public void setup() {
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        loginEndpoint = "http://localhost:" + port + "/login";
        registerEndpoint = "http://localhost:" + port + "/register";
    }

    @Test
    @DisplayName("Should receive token for valid user")
    @SqlGroup({
            @Sql(scripts = "classpath:sql/LoginControllerIntegrationTest/setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:sql/LoginControllerIntegrationTest/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void shouldCreateToken_whenExistingUserLogin() {
        final MultiValueMap<String, String> loginBody = new LinkedMultiValueMap<>();
        loginBody.put(USERNAME_PROPERTY, List.of(VALID_USER_UID));
        loginBody.put(PASSWORD_PROPERTY, List.of(VALID_USER_PASSWORD));

        final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(loginBody, httpHeaders);
        final ResponseEntity<UserDto> responseEntity = restTemplate.exchange(loginEndpoint, HttpMethod.POST, requestEntity, UserDto.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody(), "Response body is empty");
        assertNotNull(responseEntity.getBody().getToken(), "Received token is null");
        assertNotEquals(StringUtils.EMPTY, responseEntity.getBody().getToken(), "Received token is empty");
    }

    @Test
    @DisplayName("Should receive user information for valid user")
    @SqlGroup({
            @Sql(scripts = "classpath:sql/LoginControllerIntegrationTest/setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:sql/LoginControllerIntegrationTest/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void shouldReceiveUserInfo_whenExistingUserLogin() {
        final MultiValueMap<String, String> loginBody = new LinkedMultiValueMap<>();
        loginBody.put(USERNAME_PROPERTY, List.of(VALID_USER_UID));
        loginBody.put(PASSWORD_PROPERTY, List.of(VALID_USER_PASSWORD));

        final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(loginBody, httpHeaders);
        final ResponseEntity<UserDto> responseEntity = restTemplate.exchange(loginEndpoint, HttpMethod.POST, requestEntity, UserDto.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody(), "Response body is empty");

        final String actualUserUid = responseEntity.getBody().getUserUid();
        assertEquals(VALID_USER_UID, actualUserUid, "User UID is incorrect");

        final String actualName = responseEntity.getBody().getName();
        assertEquals(VALID_USER_NAME, actualName, "User Name is incorrect");
    }

    @Test
    @DisplayName("Should return error when credentials are incorrect")
    public void shouldReturnError_whenIncorrectCredentials() {
        final MultiValueMap<String, String> loginBody = new LinkedMultiValueMap<>();
        loginBody.put(USERNAME_PROPERTY, List.of(INCORRECT_USER_UID));
        loginBody.put(PASSWORD_PROPERTY, List.of(INCORRECT_USER_PASSWORD));

        final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(loginBody, httpHeaders);
        final ResponseEntity<ErrorResponse> responseEntity = restTemplate.exchange(loginEndpoint, HttpMethod.POST, requestEntity, ErrorResponse.class);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody(), "Response body is empty");

        final ErrorResponse.Error error = responseEntity.getBody().getError();
        assertNotNull(error, "Error in response cannot be null");

        final String message = error.getMessage();
        assertNotNull(message, "Error message in response cannot be null");
    }

    @Test
    @DisplayName("Should register user when it does not exists")
    @SqlGroup({
            @Sql(scripts = "classpath:sql/LoginControllerIntegrationTest/setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:sql/LoginControllerIntegrationTest/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void shouldRegister_whenUserDoesNotExists() {
        final MultiValueMap<String, String> loginBody = new LinkedMultiValueMap<>();
        loginBody.put(USERNAME_PROPERTY, List.of(NEW_USER_UID));
        loginBody.put(PASSWORD_PROPERTY, List.of(NEW_USER_PASSWORD));

        final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(loginBody, httpHeaders);
        final ResponseEntity<UserDto> responseEntity =
                restTemplate.exchange(registerEndpoint, HttpMethod.POST, requestEntity, UserDto.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody(), "Response body is empty");

        final String actualUserUid = responseEntity.getBody().getUserUid();
        assertEquals(NEW_USER_UID, actualUserUid, "User UID is incorrect");
    }

    @Test
    @DisplayName("Should return error when register with existing user")
    @SqlGroup({
            @Sql(scripts = "classpath:sql/LoginControllerIntegrationTest/setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:sql/LoginControllerIntegrationTest/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void test() {
        final MultiValueMap<String, String> loginBody = new LinkedMultiValueMap<>();
        loginBody.put(USERNAME_PROPERTY, List.of(VALID_USER_UID));
        loginBody.put(PASSWORD_PROPERTY, List.of(NEW_USER_PASSWORD));

        final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(loginBody, httpHeaders);
        final ResponseEntity<ErrorResponse> responseEntity = restTemplate.exchange(loginEndpoint, HttpMethod.POST, requestEntity, ErrorResponse.class);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody(), "Response body is empty");

        final ErrorResponse.Error error = responseEntity.getBody().getError();
        assertNotNull(error, "Error in response cannot be null");

        final String message = error.getMessage();
        assertNotNull(message, "Error message in response cannot be null");
    }
}
