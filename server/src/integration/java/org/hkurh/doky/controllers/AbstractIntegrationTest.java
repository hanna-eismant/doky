package org.hkurh.doky.controllers;

import org.hkurh.doky.security.AuthenticationResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.jdbc.SqlMergeMode;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@SqlGroup({
        @Sql(scripts = "classpath:sql/create_base_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/cleanup_base_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public abstract class AbstractIntegrationTest {

    static final String BASE_HOST = "http://localhost:";
    static final String AUTHORIZATION_HEADER = "Authorization";
    static final String LOCATION_HEADER = "Location";
    static final String USERNAME_PROPERTY = "username";
    static final String PASSWORD_PROPERTY = "password";
    static final String VALID_USER_UID = "hanna.test";
    static final String VALID_USER_PASSWORD = "pass123";
    final HttpHeaders httpHeaders = new HttpHeaders();
    @LocalServerPort
    protected int port;
    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    String login() throws JSONException {
        final var loginEndpoint = BASE_HOST + port + "/login";
        final var loginBody = generateLoginBody();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        final var requestEntity = new HttpEntity<>(loginBody.toString(), httpHeaders);
        final var responseEntity = restTemplate.exchange(loginEndpoint, HttpMethod.POST, requestEntity, AuthenticationResponse.class);

        final var token = "Bearer " + responseEntity.getBody().getToken();
        return token;
    }

    private static JSONObject generateLoginBody() throws JSONException {
        final var loginBody = new JSONObject();
        loginBody.put(USERNAME_PROPERTY, VALID_USER_UID);
        loginBody.put(PASSWORD_PROPERTY, VALID_USER_PASSWORD);
        return loginBody;
    }

    protected void createBaseTestData() throws SQLException {
        var connection = dataSource.getConnection();
        var setupScript = new ClassPathResource("classpath:sql/create_base_test_data.sql");
        ScriptUtils.executeSqlScript(connection, setupScript);
    }

    protected void cleanupBaseTestData() throws SQLException {
        var connection = dataSource.getConnection();
        var cleanupScript = new ClassPathResource("classpath:sql/cleanup_base_test_data.sql");
        ScriptUtils.executeSqlScript(connection, cleanupScript);
    }
}
