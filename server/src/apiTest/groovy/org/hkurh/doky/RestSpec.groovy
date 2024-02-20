package org.hkurh.doky

import io.restassured.builder.RequestSpecBuilder
import org.hkurh.doky.authorization.AuthenticationRequest
import org.junit.jupiter.api.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlMergeMode
import spock.lang.Specification

import static io.restassured.RestAssured.given

@ActiveProfiles("test")
@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(scripts = "classpath:sql/create_base_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/cleanup_base_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class RestSpec extends Specification {
    protected static final BASE_HOST = 'http://localhost'
    protected static final LOGIN_ENDPOINT = '/login'
    protected static final AUTHORIZATION_HEADER = 'Authorization'
    protected static final LOCATION_HEADER = 'Location'
    protected static final CONTENT_TYPE_HEADER = 'Content-Type'
    protected static final CONTENT_TYPE_JSON = 'application/json'
    protected static final VALID_USER_UID = 'hanna_test_1@example.com'
    protected static final VALID_USER_PASSWORD = 'Qwert!2345'

    @LocalServerPort
    def port

    @Autowired
    protected JdbcTemplate jdbcTemplate

    def prepareRequestSpec() {
        new RequestSpecBuilder()
                .setBaseUri(BASE_HOST)
                .setPort(Integer.parseInt(port))
                .addHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
    }

    def prepareRequestSpecWithLogin() {
        def requestBody = new AuthenticationRequest(VALID_USER_UID, VALID_USER_PASSWORD)
        def request = prepareRequestSpec()
                .setBody(requestBody)
                .build()
        def loginResponse = given(request)
                .post(LOGIN_ENDPOINT)
        def token = loginResponse.path('token') as String
        new RequestSpecBuilder()
                .setBaseUri(BASE_HOST)
                .setPort(Integer.parseInt(port))
                .addHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
                .addHeader(AUTHORIZATION_HEADER, 'Bearer ' + token)
    }
}
