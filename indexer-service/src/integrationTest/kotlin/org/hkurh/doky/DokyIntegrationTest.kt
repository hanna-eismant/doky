package org.hkurh.doky

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.testcontainers.junit.jupiter.Testcontainers
import java.nio.file.Paths


@ActiveProfiles("test")
@Tag("integration")
@SpringJUnitConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class DokyIntegrationTest {

    private final val wiremockPort = 8443
    private final val keyStorePath = Paths.get("../wiremock-cert/wiremock-keystore.jks").toAbsolutePath().toString()
    private final val wireMockConfig: WireMockConfiguration = WireMockConfiguration()
        .httpsPort(wiremockPort)
        .keystorePath(keyStorePath)
        .keystorePassword("password")
    val wireMock = WireMockServer(wireMockConfig)

    @BeforeEach
    fun setupWireMockServer() {
        wireMock.start()
        WireMock.configureFor("https", "localhost", wiremockPort)
    }

    @AfterEach
    fun stopWireMockServer() {
        wireMock.stop()
    }

}
