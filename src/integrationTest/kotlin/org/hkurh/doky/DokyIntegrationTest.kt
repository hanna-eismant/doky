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

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlMergeMode
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import java.nio.file.Paths

@ActiveProfiles("test")
@Tag("integration")
@SpringJUnitConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = ["emails-test"])
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(scripts = ["classpath:sql/create_base_test_data.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = ["classpath:sql/cleanup_base_test_data.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class DokyIntegrationTest {
    private final val log = KotlinLogging.logger {}
    private final val wiremockPort = 8443
    private final val keyStorePath = Paths.get("wiremock-cert/wiremock-keystore.jks").toAbsolutePath().toString()
    private final val wireMockConfig: WireMockConfiguration = WireMockConfiguration()
        .httpsPort(wiremockPort)
        .keystorePath(keyStorePath)
        .keystorePassword("password")
    val wireMock = WireMockServer(wireMockConfig)

    companion object {
        @JvmStatic
        @BeforeAll
        fun setupSSL() {
            val trustStorePath = Paths.get("wiremock-cert/wiremock-truststore.jks").toAbsolutePath().toString()
            val trustStorePassword = "password"

            // Set system properties for SSL truststore
            System.setProperty("javax.net.ssl.trustStore", trustStorePath)
            System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword)
            System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        }
    }

    @BeforeEach
    fun setupWireMockServer() {
        wireMock.start()
        log.info { "WireMock HTTPS running at: https://localhost:$wiremockPort" }
        log.info { "Using keystore: $keyStorePath" }
        WireMock.configureFor("https", "localhost", wiremockPort)
    }

    @AfterEach
    fun stopWireMockServer() {
        wireMock.stop()
    }
}
