import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

allprojects {
    repositories {
        maven {
            url = uri("https://repo.maven.apache.org/maven2")
        }
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://repo.spring.io/snapshot") }
        maven { url = uri("https://repo.spring.io/milestone") }
    }
}

group = "org.hkurh.doky"
val deployVersion: String by extra {
    if (project.hasProperty("deployVersion")) {
        project.property("deployVersion") as String
    } else {
        "Aardvark-v0.1"
    }
}
val buildDate: String by extra {
    ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z"))
}

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.kotlin.plugin.jpa) apply false
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.sonarqube)
}

dependencyManagement {
    imports {
        mavenBom(libs.spring.boot.bom.get().toString())
        mavenBom(libs.azure.spring.bom.get().toString())
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "io.spring.dependency-management")

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

    val libs = rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")
    dependencies {
        implementation(libs.findBundle("logging").get())
        testImplementation(libs.findBundle("testing.unit").get())
    }

    tasks.withType<Test>().configureEach {
        if (name.contains("integrationTest", ignoreCase = true)) {
            useJUnitPlatform()

            // Configure JVM system properties for SSL truststore
            systemProperty(
                "javax.net.ssl.trustStore",
                rootProject.rootDir.resolve("wiremock-cert/wiremock-truststore.jks").absolutePath
            )
            systemProperty("javax.net.ssl.trustStorePassword", "password")

            // Optional Debug Information for TrustStore
            doFirst {
                println("Integration Test Truststore Path: ${rootProject.rootDir.resolve("wiremock-cert/wiremock-truststore.jks").absolutePath}")
            }
        }
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
