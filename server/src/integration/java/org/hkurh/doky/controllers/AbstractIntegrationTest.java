package org.hkurh.doky.controllers;

import org.hkurh.doky.security.AuthenticationResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@SqlGroup({
        @Sql(scripts = "classpath:sql/create_base_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/cleanup_base_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public abstract class AbstractIntegrationTest {

    protected static final String BASE_HOST = "http://localhost:";
    protected static final String AUTHORIZATION_HEADER = "Authorization";
    protected static final String USERNAME_PROPERTY = "username";
    protected static final String PASSWORD_PROPERTY = "password";
    protected static final String VALID_USER_UID = "hanna.test";
    protected static final String VALID_USER_PASSWORD = "pass123";
    protected final HttpHeaders httpHeaders = new HttpHeaders();
    @LocalServerPort
    protected int port;
    @Autowired
    protected TestRestTemplate restTemplate;

    protected String login() throws JSONException {
        var loginEndpoint = BASE_HOST + port + "/login";
        var loginBody = generateLoginBody();

        var requestEntity = new HttpEntity<>(loginBody.toString(), httpHeaders);
        var responseEntity = restTemplate.exchange(loginEndpoint, HttpMethod.POST, requestEntity, AuthenticationResponse.class);

        var token = "Bearer " + responseEntity.getBody().getToken();
        return token;
    }

    protected static JSONObject generateLoginBody() throws JSONException {
        var loginBody = new JSONObject();
        loginBody.put(USERNAME_PROPERTY, VALID_USER_UID);
        loginBody.put(PASSWORD_PROPERTY, VALID_USER_PASSWORD);
        return loginBody;
    }
}
