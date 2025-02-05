plugins {
    alias(libs.plugins.spring.boot)
}

dependencyManagement {
    imports {
        mavenBom(libs.spring.boot.bom.get().toString())
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

    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.bundles.azure.keyvault)
    implementation(libs.kotlin.reflect)
    implementation(libs.bundles.kafka)
    implementation(libs.bundles.json)

    annotationProcessor(libs.spring.boot.config.processor)

    testImplementation(libs.bundles.testing.integration)
    testImplementation(libs.spring.kafka.test)

    testRuntimeOnly(libs.junit.platform.launcher)
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
