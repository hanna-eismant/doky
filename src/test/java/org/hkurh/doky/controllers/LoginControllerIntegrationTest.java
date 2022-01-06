package org.hkurh.doky.controllers;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hkurh.doky.dto.UserDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Login endpoint integration test")
class LoginControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final HttpHeaders httpHeaders = new HttpHeaders();

    @BeforeEach
    void setUp() {
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Should receive token for valid user")
    void shouldCreateToken_whenExistingUserLogin() {
        String endpoint = "http://localhost:" + port + "/login";

        MultiValueMap<String, String> loginBody = new LinkedMultiValueMap<>();
        loginBody.put("username", List.of("Hanna"));
        loginBody.put("password", List.of("pass123"));

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(loginBody, httpHeaders);
        ResponseEntity<UserDto> responseEntity = restTemplate.exchange(endpoint, HttpMethod.POST, requestEntity, UserDto.class);
        System.out.println(responseEntity.getBody());

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody(), "Response body is empty");
        Assertions.assertNotNull(responseEntity.getBody().getToken(), "Received token is null");
        Assertions.assertNotEquals(StringUtils.EMPTY, responseEntity.getBody().getToken(), "Received token is empty");
    }
}
