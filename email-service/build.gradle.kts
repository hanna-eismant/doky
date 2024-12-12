dependencyManagement {
    imports {
        mavenBom(libs.spring.boot.bom.get().toString())
        mavenBom(libs.azure.spring.bom.get().toString())
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation(project(":persistence"))

    implementation(libs.bundles.spring.starter.web)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.bundles.mail)
    implementation(libs.bundles.azure.keyvault)
    implementation(libs.kotlin.reflect)
    implementation(libs.bundles.kafka)
    implementation(libs.bundles.json)

    developmentOnly(libs.spring.boot.devtools)

    annotationProcessor(libs.spring.boot.config.processor)

    testImplementation(libs.bundles.testing.integration)
    testImplementation(libs.spring.kafka.test)
    testImplementation(libs.greenmail)

    testRuntimeOnly(libs.junit.platform.launcher)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
        register<JvmTestSuite>("integrationTest") {
            testType = TestSuiteType.INTEGRATION_TEST
            dependencies {
                implementation(project())
                implementation(project(":persistence"))

                implementation(libs.spring.boot.starter.test.get().toString())
                implementation(libs.awaitility.get().toString())

                implementation(libs.spring.kafka.test.get().toString())
                implementation(libs.spring.kafka.production.get().toString())
                implementation(libs.kafka.clients.get().toString())

                implementation(libs.greenmail.get().toString())

                implementation(libs.spring.boot.starter.jdbc.get().toString())

                implementation(libs.kotlin.logging.get().toString())
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

tasks.named<Test>("integrationTest") {
    testLogging {
        showStandardStreams = true
        events("PASSED", "SKIPPED", "FAILED")
    }
}
