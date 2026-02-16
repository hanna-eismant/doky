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

import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.internal.classpath.Instrumented.systemProperty
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://repo.maven.apache.org/maven2")
    }
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://repo.spring.io/milestone") }
}


plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.kotlin.plugin.jpa)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.azurewebapp)
    alias(libs.plugins.dokka)
    alias(libs.plugins.buildconfig)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

configurations.matching { it.name.startsWith("dokka") }.configureEach {
    resolutionStrategy.eachDependency {
        if (requested.group == "com.fasterxml.jackson.core" && requested.name == "jackson-databind") {
            // Force jackson-databind to use version 2.12.7.1
            useVersion("2.12.7.1")
        } else if (requested.group.startsWith("com.fasterxml.jackson")) {
            // Force other Jackson dependencies to use version 2.12.7
            useVersion("2.12.7")
        }
    }
}

dependencyManagement {
    imports {
        mavenBom(libs.spring.boot.bom.get().toString())
        mavenBom(libs.azure.spring.bom.get().toString())
    }
}

dependencies {

    implementation(libs.bundles.flyway)
    implementation(libs.bundles.database.connectors)

    implementation(libs.bundles.azure.search)

    implementation(libs.bundles.mail)

    implementation(libs.bundles.spring.starter.web)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.bundles.jwt)
    implementation(libs.kotlin.reflect)
    implementation(libs.bundles.validation)
    implementation(libs.bundles.azure.keyvault)
    implementation(libs.openapi.starter)
    implementation(libs.bundles.json)
    implementation(libs.bundles.logging)
    implementation(libs.azure.blob)
    implementation(libs.bundles.kafka)
    implementation(libs.dd.trace.api)

    annotationProcessor(libs.spring.boot.config.processor)

    testImplementation(libs.bundles.testing.unit)
    testImplementation(libs.bundles.testing.integration)
    testImplementation(libs.bundles.testing.web)
    testImplementation(libs.spring.kafka.test)
    testImplementation(libs.greenmail)
    testImplementation(libs.bundles.testcontainers)

    testRuntimeOnly(libs.junit.platform.launcher)
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
            dependencies {
                implementation(libs.spring.boot.starter.data.jpa.get().toString())
            }
        }
        register<JvmTestSuite>("integrationTest") {
            testType = TestSuiteType.INTEGRATION_TEST
            systemProperty(
                "javax.net.ssl.trustStore",
                rootProject.rootDir.resolve("wiremock-cert/wiremock-truststore.jks").absolutePath
            )
            systemProperty("javax.net.ssl.trustStorePassword", "password")


            dependencies {
                implementation(project())

                implementation(libs.spring.boot.starter.test.get().toString())
                implementation(libs.awaitility.get().toString())

                implementation(libs.httpclient.get().toString())
                implementation(libs.rest.assured.get().toString())

                implementation(libs.spring.kafka.test.get().toString())
                implementation(libs.spring.kafka.production.get().toString())
                implementation(libs.kafka.clients.get().toString())

                implementation(libs.greenmail.get().toString())

                implementation(libs.spring.boot.starter.jdbc.get().toString())
                implementation(libs.spring.boot.starter.security.get().toString())

                implementation(libs.kotlin.logging.get().toString())

                implementation(libs.greenmail.get().toString())

                implementation(libs.wiremock.standalone.get().toString())
                implementation(libs.json.flattener.get().toString())
                implementation(libs.json.schema.validator.get().toString())
            }
        }
        register<JvmTestSuite>("apiTest") {
            testType = TestSuiteType.FUNCTIONAL_TEST
            dependencies {
                implementation(project())

                implementation(libs.spring.boot.starter.test.get().toString())
                implementation(libs.junit4.get().toString())

                implementation(libs.httpclient.get().toString())
                implementation(libs.rest.assured.get().toString())

                implementation(libs.spring.kafka.test.get().toString())
                implementation(libs.spring.kafka.production.get().toString())
                implementation(libs.kafka.clients.get().toString())

                implementation(libs.spring.boot.starter.data.jpa.get().toString())
                implementation(libs.spring.boot.starter.web.get().toString())
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        events("PASSED", "SKIPPED", "FAILED")
    }
}

tasks.named<Test>("apiTest") {
    testLogging {
        showStandardStreams = true
        events("PASSED", "SKIPPED", "FAILED")
    }
}

tasks.named<Test>("integrationTest") {
    testLogging {
        showStandardStreams = true
        events("PASSED", "SKIPPED", "FAILED")
    }
}

val deployVersion: String by extra {
    if (project.hasProperty("deployVersion")) {
        project.property("deployVersion") as String
    } else {
        project.version.toString()
    }
}
val buildDate: String by extra {
    ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z"))
}

buildConfig {
    packageName("org.hkurh.doky")
    documentation.set("Generated by BuildConfig plugin")
    buildConfigField("DEPLOY_VERSION", provider { deployVersion })
}

tasks {
    jar {
        manifest {
            attributes(
                "Manifest-Version" to "1.0",
                "Main-Class" to "org.hkurh.doky.DokyApplication",
                "Implementation-Title" to "Doky App Server",
                "Implementation-Version" to deployVersion,
                "Implementation-Vendor" to "hkurh-pets",
                "Created-By" to "Kotlin Gradle",
                "Built-By" to "Hanna Kurhuzenkava",
                "Build-Jdk" to "17",
                "Build-Date" to buildDate
            )
        }
    }

    assemble {
        dependsOn(jar)
    }
}

val branchName = if (project.hasProperty("branchName")) {
    project.property("branchName") as String
} else {
    "main"
}

sonar {
    properties {
        property("sonar.projectKey", "hanna-eismant_doky")
        property("sonar.organization", "hanna-eismant")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.token", System.getenv("SONAR_TOKEN") ?: "default_token_value")
        property("sonar.branch.name", branchName)
    }
}

tasks.register("printVersion") {
    description = "Print version of the application."
    group = "doky"
    doLast {
        println(project.version)
    }
}
