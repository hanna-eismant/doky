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

job("Tests") {
    container(displayName = "Run unit tests", image = "gradle:6.9.0-jdk11") {
        workDir = "server"
        kotlinScript { api ->
            api.gradle("test")
        }
    }

    container(displayName = "Run API tests", image = "gradle:6.9.0-jdk11") {
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
            api.gradle("apiTest")
        }
    }

    host("Schedule Deployment") {
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
        gitPush {
            branchFilter {
                -"refs/heads/*"
            }
        }
    }
    parameters {
        text("spring-profile", value = "dev")
        text("deploy-version")
    }

    container(displayName = "Deploy artifact", image = "gradle:6.9.0-jdk11") {
        workDir = "server"

        env["AZURE_SUBSCRIPTION"] = "{{ project:azure-subscription }}"
        env["AZURE_RESOURCE_GROUP"] = "{{ project:azure-resource-group }}"
        env["AZURE_APP_NAME"] = "{{ project:azure-app-name }}"
        env["AZURE_TENANT"] = "{{ project:azure-tenant }}"
        env["AZURE_CLIENT"] = "{{ project:azure-client }}"
        env["AZURE_AUTH_KEY"] = "{{ project:azure-auth-key }}"

        env["AZURE_BLOB_CONNECTION"] = "{{ project:azure-blob-connection }]"
        env["AZURE_BLOB_CONTAINER"] = "{{ project:azure-blob-container }]"

        env["SPRING_PROFILES_ACTIVE"] = "{{ spring-profile }}"
        env["SPRING_DATASOURCE_URL"] = "{{ project:spring-datasource-url }}"
        env["SPRING_DATASOURCE_USERNAME"] = "{{ project:spring-datasource-username }}"
        env["SPRING_DATASOURCE_PASSWORD"] = "{{ project:spring-datasource-password }}"

        kotlinScript { api ->
            api.space().projects.automation.deployments.start(
                project = api.projectIdentifier(),
                targetIdentifier = TargetIdentifier.Key("azure-dev"),
                version = "{{ deploy-version }}",
                syncWithAutomationJob = true
            )
            api.gradle("azureWebAppDeploy")
        }
    }
}

fun getNextSundayDate(): Instant {
    var date = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC"))
    // set time to 4pm UTC
    date = date.withHour(16).withMinute(0).withSecond(0).withNano(0)
    val sunday = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
    return Instant.parse(sunday.format(DateTimeFormatter.ISO_INSTANT))
}
