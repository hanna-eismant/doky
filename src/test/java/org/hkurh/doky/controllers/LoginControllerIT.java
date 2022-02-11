package org.hkurh.doky.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hkurh.doky.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Login endpoint integration test")
class LoginControllerIT
{

    private static final String USERNAME_PROPERTY = "username";
    private static final String PASSWORD_PROPERTY = "password";
    private static final String VALID_USER_UID = "hanna.test";
    private static final String VALID_USER_PASSWORD = "pass123";
    private static final String VALID_USER_NAME = "Hanna";
    private static final String INCORRECT_USER_UID = ".test";
    private static final String INCORRECT_USER_PASSWORD = "pass";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private String endpoint;

    @BeforeEach
    void setup() {
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        endpoint = "http://localhost:" + port + "/login";
    }

    @Test
    @DisplayName("Should receive token for valid user")
    @SqlGroup({
            @Sql(scripts = "classpath:sql/LoginControllerIntegrationTest/setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:sql/LoginControllerIntegrationTest/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void shouldCreateToken_whenExistingUserLogin() {
        MultiValueMap<String, String> loginBody = new LinkedMultiValueMap<>();
        loginBody.put(USERNAME_PROPERTY, List.of(VALID_USER_UID));
        loginBody.put(PASSWORD_PROPERTY, List.of(VALID_USER_PASSWORD));

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(loginBody, httpHeaders);
        ResponseEntity<UserDto> responseEntity = restTemplate.exchange(endpoint, HttpMethod.POST, requestEntity, UserDto.class);

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
    void shouldReceiveUserInfo_whenExistingUserLogin() {
        MultiValueMap<String, String> loginBody = new LinkedMultiValueMap<>();
        loginBody.put(USERNAME_PROPERTY, List.of(VALID_USER_UID));
        loginBody.put(PASSWORD_PROPERTY, List.of(VALID_USER_PASSWORD));

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(loginBody, httpHeaders);
        ResponseEntity<UserDto> responseEntity = restTemplate.exchange(endpoint, HttpMethod.POST, requestEntity, UserDto.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody(), "Response body is empty");

        String actualUserUid = responseEntity.getBody().getUserUid();
        assertEquals(VALID_USER_UID, actualUserUid, "User UID is incorrect");

        String actualName = responseEntity.getBody().getName();
        assertEquals(VALID_USER_NAME, actualName, "User Name is incorrect");
    }

    @Test
    @DisplayName("Should return error when credentials are incorrect")
    void shouldReturnError_whenIncorrectCredentials() {
        MultiValueMap<String, String> loginBody = new LinkedMultiValueMap<>();
        loginBody.put(USERNAME_PROPERTY, List.of(INCORRECT_USER_UID));
        loginBody.put(PASSWORD_PROPERTY, List.of(INCORRECT_USER_PASSWORD));

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(loginBody, httpHeaders);
        ResponseEntity<ErrorResponse> responseEntity = restTemplate.exchange(endpoint, HttpMethod.POST, requestEntity, ErrorResponse.class);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody(), "Response body is empty");

        ErrorResponse.Error error = responseEntity.getBody().getError();
        assertNotNull(error, "Error in response cannot be null");

        String message = error.getMessage();
        assertNotNull(message, "Error message in response cannot be null");
    }
}
