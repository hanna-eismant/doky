import com.github.gradle.node.npm.task.NpmTask
import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.MetricType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("jvm-test-suite")
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
    id("org.jetbrains.kotlin.plugin.spring") version "1.8.22"
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    id("com.microsoft.azure.azurewebapp") version "1.7.1"
    id("org.jetbrains.kotlinx.kover") version "0.7.6"
    id("org.jetbrains.dokka") version "1.9.20"
    id("com.github.gmazzo.buildconfig") version "5.3.5"
    id("com.github.node-gradle.node") version "7.0.2"
}

dependencyManagement {
    imports {
        mavenBom("com.azure.spring:spring-cloud-azure-dependencies:5.3.0")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

group = "org.hkurh.doky"

version = if (project.hasProperty("deployVersion")) {
    project.property("deployVersion") as String
} else {
    "Aardvark-v0.1"
}

buildConfig {
    packageName(group.toString())
    documentation.set("Generated by BuildConfig plugin")
    buildConfigField("buildVersion", provider { "${project.version}" })
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("jakarta.validation:jakarta.validation-api:3.0.2")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("com.azure.spring:spring-cloud-azure-appconfiguration-config-web")
    implementation("com.azure.spring:spring-cloud-azure-starter-keyvault")

    implementation("org.flywaydb:flyway-core:9.21.0")
    implementation("org.flywaydb:flyway-mysql:9.21.0")
    implementation("org.flywaydb:flyway-sqlserver:9.21.0")
    implementation("com.mysql:mysql-connector-j:8.3.0")
    implementation("com.microsoft.sqlserver:mssql-jdbc:12.6.1.jre11")


    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.2")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.2")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-joda:2.13.1")
    implementation("commons-io:commons-io:2.11.0")
    implementation("io.github.oshai:kotlin-logging:6.0.9")

    implementation("com.azure:azure-storage-blob:12.25.3")

    implementation("net.logstash.logback:logstash-logback-encoder:7.4")

    runtimeOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.apache.httpcomponents:httpclient:4.5.13")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.junit.platform:junit-platform-suite-api:1.10.2")
    testImplementation("org.mockito:mockito-junit-jupiter:4.0.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
    testImplementation("com.icegreen:greenmail:2.0.1")
    testImplementation("org.awaitility:awaitility:4.2.1")

    testImplementation("io.rest-assured:rest-assured:5.3.0")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.test {
    useJUnitPlatform()
    maxHeapSize = "1G"
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
        register<JvmTestSuite>("integrationTest") {
            dependencies {
                implementation(project())

                implementation("org.springframework.boot:spring-boot-starter-data-jpa")
                implementation("org.springframework.boot:spring-boot-starter-web")
                implementation("org.springframework.boot:spring-boot-starter-test")

                implementation("org.springframework.boot:spring-boot-starter-security")
                implementation("io.github.oshai:kotlin-logging:6.0.9")

                implementation("org.junit.platform:junit-platform-suite-api:1.10.2")

                implementation("com.icegreen:greenmail:2.0.1")
                implementation("org.awaitility:awaitility:4.2.1")
            }
        }
        register<JvmTestSuite>("apiTest") {
            dependencies {
                implementation(project())

                implementation("org.springframework.boot:spring-boot-starter-data-jpa")
                implementation("org.springframework.boot:spring-boot-starter-web")
                implementation("org.springframework.boot:spring-boot-starter-test")

                implementation("org.springframework:spring-mock:2.0.8")
                implementation("org.apache.httpcomponents:httpclient:4.5.13")
                implementation("org.junit.jupiter:junit-jupiter:5.8.2")
                implementation("org.junit.platform:junit-platform-suite-api:1.10.2")
                implementation("org.mockito:mockito-junit-jupiter:4.0.0")
                implementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
                implementation("io.rest-assured:rest-assured:5.3.0")
            }
        }
    }
}

tasks.named<Test>("apiTest") {
    onlyIf { project.hasProperty("runApiTests") && project.property("runApiTests").toString().toBoolean() }
}

tasks.named<Test>("integrationTest") {
    onlyIf {
        project.hasProperty("runIntegrationTests") && project.property("runIntegrationTests").toString().toBoolean()
    }
}

tasks.test {
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

kover {
    excludeSourceSets {
        names("integrationTest")
        names("apiTest")
    }
}

koverReport {
    defaults {
        filters {
            includes {
                classes("org.hkurh.*")
            }
        }
        verify {
            rule {
                bound {
                    minValue = 33
                    metric = MetricType.LINE
                    aggregation = AggregationType.COVERED_PERCENTAGE
                }
            }
        }
    }
}

node {
    download = true
    distBaseUrl = null
    version = "20.12.2"
    npmInstallCommand = "ci"
    nodeProjectDir = file("${project.projectDir}/doky-front")
}

tasks.register<NpmTask>("npmBuild") {
    dependsOn("npmInstall")
//    args = listOf("run", "build")   // prod
    args = listOf("run", "build", "--", "--env", "be-env=local") // local
}

tasks.register<Copy>("copyFrontDist") {
    dependsOn("npmBuild")
    from("$projectDir/doky-front/dist")
    into("$buildDir/resources/main/static")
}

tasks.register<Copy>("copyFrontDistSrc") {
    dependsOn("npmBuild")
    from("$projectDir/doky-front/dist")
    into("$projectDir/src/main/resources/static")
}

tasks.named("processResources") {
    dependsOn("copyFrontDist")
    dependsOn("copyFrontDistSrc")
}
