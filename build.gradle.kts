allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

group = "org.hkurh.doky"

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

    val libs = rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")
    dependencies {
        implementation(libs.findBundle("logging").get())
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
