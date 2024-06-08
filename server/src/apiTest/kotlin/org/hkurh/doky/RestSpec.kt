package org.hkurh.doky

import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import org.hkurh.doky.authorization.AuthenticationRequest
import org.junit.jupiter.api.Tag
import org.junit.platform.suite.api.IncludeTags
import org.junit.platform.suite.api.Suite
import org.junit.platform.suite.api.SuiteDisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlMergeMode

@ActiveProfiles("test")
@Tag("api")
@Suite
@SuiteDisplayName("API Tests")
@IncludeTags("api")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(scripts = ["classpath:sql/create_base_test_data.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = ["classpath:sql/cleanup_base_test_data.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class RestSpec {
    protected val baseHost = "http://localhost"
    protected val restPrefix = "/api"
    protected val loginEndpoint = "$restPrefix/login"
    protected val authorizationHeader = "Authorization"
    protected val locationHeader = "Location"
    protected val contentTypeHeader = "Content-Type"
    protected val contentTypeJson = "application/json"
    protected val validUserUid = "hanna_test_1@example.com"
    protected val validUserPassword = "Qwert!2345"

    @LocalServerPort
    var port: Int = 0

    @Autowired
    protected lateinit var jdbcTemplate: JdbcTemplate

    fun prepareRequestSpec(): RequestSpecBuilder {
        return RequestSpecBuilder()
            .setBaseUri(baseHost)
            .setPort(port)
            .addHeader(contentTypeHeader, contentTypeJson)
    }

    fun prepareRequestSpecWithLogin(): RequestSpecBuilder {
        val requestBody = AuthenticationRequest().apply {
            uid = validUserUid
            password = validUserPassword
        }
        val request = prepareRequestSpec().setBody(requestBody).build()
        val loginResponse = given(request).post(loginEndpoint)
        val token: String = loginResponse.path("token")
        return RequestSpecBuilder()
            .setBaseUri(baseHost)
            .setPort(port)
            .addHeader(contentTypeHeader, contentTypeJson)
            .addHeader(authorizationHeader, "Bearer $token")
    }
}
