package org.hkurh.doky;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Security integration test")
public class SecurityIntegrationTest extends AbstractIntegrationTest {

    private String getUserInfoEndpoint;

    @BeforeEach
    void setUp() {
        getUserInfoEndpoint = AbstractIntegrationTest.BASE_HOST + port + "/users/current";
    }

    @Test
    @DisplayName("Should allow get user info if user is authenticated")
    void shouldAllowGetUserInfo_whenUserIsAuthenticated() throws JSONException {
        final String token = login();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(AbstractIntegrationTest.AUTHORIZATION_HEADER, token);

        final var request = new HttpEntity<>(null, httpHeaders);
        final var response = restTemplate.exchange(getUserInfoEndpoint, HttpMethod.GET, request, LinkedHashMap.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return Access Denied when get user info as not authorized user")
    void shouldReturnAccessDenied_whenUserIsAnonymous() {
        final var request = new HttpEntity<>(null, httpHeaders);
        final var response = restTemplate.exchange(getUserInfoEndpoint, HttpMethod.GET, request, LinkedHashMap.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
