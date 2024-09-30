val kotlinVersion = "1.9.25"
val springBootVersion = "3.3.4"
val springDependencyVersion = "1.1.6"
val springAzureVersion = "5.16.0"
val kotlinLoggingVersion = "6.0.9"
var greenmailVersion = "2.0.1"


plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("plugin.jpa") version "1.9.25"
}

dependencyManagement {
    imports {
        mavenBom("com.azure.spring:spring-cloud-azure-dependencies:$springAzureVersion")
        mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("com.azure.spring:spring-cloud-azure-appconfiguration-config-web")
    implementation("com.azure.spring:spring-cloud-azure-starter-keyvault")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.kafka:spring-kafka")

    implementation("io.github.oshai:kotlin-logging:$kotlinLoggingVersion")
    implementation("org.apache.kafka:kafka-clients")
    implementation("com.fasterxml.jackson.core:jackson-annotations")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("com.microsoft.sqlserver:mssql-jdbc")
    runtimeOnly("com.mysql:mysql-connector-j")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
//    testImplementation("org.junit.platform:junit-platform-suite-api:1.10.2")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.junit.platform:junit-platform-suite-api:1.10.2")
    testImplementation("org.mockito:mockito-junit-jupiter:4.0.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
    testImplementation("com.icegreen:greenmail:$greenmailVersion")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
