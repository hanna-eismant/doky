import java.util.*

val deployVersion = if (project.hasProperty("deployVersion")) {
    project.property("deployVersion") as String
} else {
    "Aardvark-v0.1"
}

version = deployVersion

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.kotlin.plugin.jpa)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.azurefunctions)
}

dependencyManagement {
    imports {
        mavenBom(libs.spring.boot.bom.get().toString())
        mavenBom(libs.azure.spring.bom.get().toString())
    }
}

dependencies {
    implementation(project(":persistence"))

    implementation(libs.bundles.azure.functions)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.kotlin.reflect)
    implementation(libs.bundles.database.connectors)
    implementation(libs.bundles.logging)
}

azurefunctions {
    resourceGroup = "doky"
    appName = "cleanup-functions"
}

tasks {
    jar {
        manifest {
            attributes(
                "Manifest-Version" to "1.0",
                "Main-Class" to "org.hkurh.doky.CleanupFunctionsApplication",
                "Implementation-Title" to "Doky Cleanup Functions",
                "Implementation-Version" to 1,
                "Implementation-Vendor" to "hkurh-pets",
                "Created-By" to "Kotlin Gradle",
                "Built-By" to "Hanna Kurhuzenkava"
            )
        }
    }

    assemble {
        dependsOn(jar)
    }
}

tasks.register("generateLocalSettings") {
    group = "build"
    description = "Generate local.settings.json with sensitive data dynamically"

    doLast {
        // Define paths
        val templateFile = file("local.settings.template.json")
        val outputFile = file("local.settings.json")

        // Load sensitive properties from environment variables or a secure file
        val props = Properties()
        val env = System.getenv()
        props["spring-profile"] = env["SPRING_PROFILES_ACTIVE"] ?: "local"
        props["database-url"] = env["SPRING_DATASOURCE_URL"] ?: "jdbc:mysql://localhost:3309/doky"
        props["database-user"] = env["SPRING_DATASOURCE_USERNAME"] ?: "doky"
        props["database-password"] = env["SPRING_DATASOURCE_PASSWORD"] ?: "doky"

        // Replace placeholders in template
        val templateContent = templateFile.readText()
        val resolvedContent = templateContent
            .replace("<SPRING_PROFILE>", props["spring-profile"].toString())
            .replace("<DATABASE_URL>", props["database-url"].toString())
            .replace("<DATABASE_USERNAME>", props["database-user"].toString())
            .replace("<DATABASE_PASSWORD>", props["database-password"].toString())

        // Write to output file
        outputFile.writeText(resolvedContent)
        println("local.settings.json generated successfully!")
    }
}
