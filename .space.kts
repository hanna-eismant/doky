import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

/**
 * JetBrains Space Automation
 * This Kotlin-script file lets you automate build activities
 * For more info, see https://www.jetbrains.com/help/space/automation.html
 */

val gradleImageVersion = "gradle:8.2-jdk17"

job("Tests for main branch") {
    startOn {
        // every day at 3:59 am UTC
        schedule { cron("59 3 * * *") }
    }

    val sharedCoveragePath = "coverage"
    container(displayName = "Unit tests with coverage", image = gradleImageVersion) {
        shellScript {
            content = """
                cd server
                ./gradlew koverVerify
                mkdir ${'$'}JB_SPACE_FILE_SHARE_PATH/$sharedCoveragePath
                cd build/kover/bin-reports
                cp -a . ${'$'}JB_SPACE_FILE_SHARE_PATH/$sharedCoveragePath
                cd ${'$'}JB_SPACE_FILE_SHARE_PATH/$sharedCoveragePath
                ls -la
                """.trimIndent()
        }
    }

    container(displayName = "Qodana scan", image = "jetbrains/qodana-jvm:latest") {
        env["QODANA_TOKEN"] = "{{ project:qodana-token }}"
        shellScript {
            content = """
                qodana \
                --project-dir  server \
                --baseline     qodana.sarif.json \
                --coverage-dir ${'$'}JB_SPACE_FILE_SHARE_PATH/$sharedCoveragePath
                """.trimIndent()
        }
    }

    container(displayName = "Integration tests", image = gradleImageVersion) {
        env["DB_HOST"] = "db"
        env["DB_PORT"] = "3306"
        service("mysql:8") {
            alias("db")
            args(
                "--log_bin_trust_function_creators=ON",
                "--max-connections=700"
            )
            env["MYSQL_ROOT_PASSWORD"] = "doky-test"
            env["MYSQL_DATABASE"] = "doky-test"
            env["MYSQL_USER"] = "doky-test"
            env["MYSQL_PASSWORD"] = "doky-test"
        }

        workDir = "server"
        kotlinScript { api ->
            api.gradlew("integrationTest", "-PrunIntegrationTests=true")
        }
    }

    container(displayName = "API tests", image = gradleImageVersion) {
        env["DB_HOST"] = "db"
        env["DB_PORT"] = "3306"
        service("mysql:8") {
            alias("db")
            args(
                "--log_bin_trust_function_creators=ON",
                "--max-connections=700"
            )
            env["MYSQL_ROOT_PASSWORD"] = "doky-test"
            env["MYSQL_DATABASE"] = "doky-test"
            env["MYSQL_USER"] = "doky-test"
            env["MYSQL_PASSWORD"] = "doky-test"
        }

        workDir = "server"
        kotlinScript { api ->
            api.gradlew("apiTest", "-PrunApiTests=true")
        }
    }

    host("Schedule Azure DEV Deployment") {
        kotlinScript { api ->
            val deployVersion = "Aardvark-v0.1." + api.executionNumber()
            api.space().projects.automation.deployments.schedule(
                project = api.projectIdentifier(),
                targetIdentifier = TargetIdentifier.Key("azure-dev"),
                version = deployVersion,
                scheduledStart = getNextSundayDate()
            )
        }
    }
}

job("Azure DEV Deployment") {
    startOn {
        // every Sunday at 11:59 pm UTC
        schedule { cron("59 23 * * SUN") }
    }
    parameters {
        text("spring-profile", value = "dev")
    }

    container(displayName = "Deploy artifact", image = gradleImageVersion) {
        workDir = "server"
        var buildNumber = "Aardvark-v0.1"
        var depVersion

        env["AZURE_SUBSCRIPTION"] = "{{ project:azure-subscription }}"
        env["AZURE_RESOURCE_GROUP"] = "{{ project:azure-resource-group }}"
        env["AZURE_APP_NAME"] = "{{ project:azure-app-name }}"
        env["AZURE_TENANT"] = "{{ project:azure-tenant }}"
        env["AZURE_CLIENT"] = "{{ project:azure-client }}"
        env["AZURE_AUTH_KEY"] = "{{ project:azure-auth-key }}"

        env["AZURE_BLOB_CONNECTION"] = "{{ project:azure-blob-connection }}"
        env["AZURE_BLOB_CONTAINER"] = "{{ project:azure-blob-container }}"

        env["SPRING_PROFILES_ACTIVE"] = "{{ spring-profile }}"
        env["SPRING_DATASOURCE_URL"] = "{{ project:spring-datasource-url }}"
        env["SPRING_DATASOURCE_USERNAME"] = "{{ project:spring-datasource-username }}"
        env["SPRING_DATASOURCE_PASSWORD"] = "{{ project:spring-datasource-password }}"

        env["BUILD_COMMIT"] = "{{ run:git-checkout.commit }}"

        kotlinScript { api ->
            depVersion = api.space().projects.automation.deployments.get(
                project = api.projectIdentifier(),
                targetIdentifier = TargetIdentifier.Key("azure-dev"),
                deploymentIdentifier = DeploymentIdentifier.Status(DeploymentIdentifierStatus.scheduled)
            ).version
            println(depVersion)
        }

        env["BUILD_NUMBER"] = depVersion

//        kotlinScript { api ->
//            api.gradle("azureWebAppDeploy")
//        }
    }
}

fun getNextSundayDate(): Instant {
    var date = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC"))
    // set time to 11:59 pm UTC
    date = date.withHour(23).withMinute(59).withSecond(0).withNano(0)
    val sunday = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
    return Instant.parse(sunday.format(DateTimeFormatter.ISO_INSTANT))
}
