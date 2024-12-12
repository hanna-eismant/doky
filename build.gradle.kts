allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

group = "org.hkurh"

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25" apply false
    id("org.springframework.boot") version "3.3.4" apply false
    id("io.spring.dependency-management") version "1.1.6"
    id("org.sonarqube") version "5.1.0.4882"
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
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }

    val libs = rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")
    dependencies {
        implementation(libs.findLibrary("kotlin.logging").get())
        testImplementation(libs.findBundle("testing.unit").get())
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
