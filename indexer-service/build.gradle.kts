plugins {
    alias(libs.plugins.spring.boot)
}

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
    implementation(project(":ai-search"))

    implementation(libs.bundles.spring.starter.web)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.bundles.jwt)
    implementation(libs.bundles.azure.keyvault)
    implementation(libs.kotlin.reflect)
    implementation(libs.bundles.kafka)
    implementation(libs.bundles.json)
    implementation(libs.bundles.azure.search)
    implementation(libs.spring.boot.admin)

    annotationProcessor(libs.spring.boot.config.processor)

    testImplementation(libs.bundles.testing.integration)
    testImplementation(libs.bundles.testing.web)
    testImplementation(libs.bundles.testcontainers)
    testImplementation(libs.spring.kafka.test)

    testRuntimeOnly(libs.junit.platform.launcher)
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
            dependencies {
                implementation(project(":persistence"))
                implementation(project(":ai-search"))
                implementation(libs.spring.boot.starter.data.jpa.get().toString())
            }
        }
        register<JvmTestSuite>("integrationTest") {
            testType = TestSuiteType.INTEGRATION_TEST
            dependencies {
                implementation(project())
                implementation(project(":persistence"))
                implementation(project(":ai-search"))

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

                implementation(libs.testcontainers.core.get().toString())
                implementation(libs.testcontainers.junit.get().toString())
                implementation(libs.testcontainers.wiremock.get().toString())
                implementation(libs.wiremock.standalone.get().toString())
                implementation(libs.json.flattener.get().toString())
                implementation(libs.json.schema.validator.get().toString())

            }
        }
//        register<JvmTestSuite>("apiTest") {
//            testType = TestSuiteType.FUNCTIONAL_TEST
//            dependencies {
//                implementation(project())
//                implementation(project(":persistence"))
//
//                implementation(libs.spring.boot.starter.test.get().toString())
//                implementation(libs.junit4.get().toString())
//
//                implementation(libs.httpclient.get().toString())
//                implementation(libs.rest.assured.get().toString())
//
//                implementation(libs.spring.kafka.test.get().toString())
//                implementation(libs.spring.kafka.production.get().toString())
//                implementation(libs.kafka.clients.get().toString())
//
//                implementation(libs.spring.boot.starter.data.jpa.get().toString())
//                implementation(libs.spring.boot.starter.web.get().toString())
//            }
//        }
    }
}

val deployVersion = rootProject.extra["deployVersion"] as String
val buildDate = rootProject.extra["buildDate"] as String

tasks {
    jar {
        manifest {
            attributes(
                "Manifest-Version" to "1.0",
                "Main-Class" to "org.hkurh.doky.IndexerServiceApplication",
                "Implementation-Title" to "Doky Indexer Service",
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
