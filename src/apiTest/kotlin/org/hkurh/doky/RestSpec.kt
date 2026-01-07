/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2026
 *  - Hanna Kurhuzenkava (hanna.kurhuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky

import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import org.hkurh.doky.authorization.AuthenticationRequest
import org.junit.jupiter.api.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlMergeMode
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@ActiveProfiles("test")
@Tag("api")
@SpringJUnitConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = ["emails-test"])
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
