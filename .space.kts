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

job("Tests for development branches") {
    startOn {
        gitPush {
            anyRefMatching {
                +"refs/heads/*"
                -"refs/heads/main"
            }
        }
    }

    git {
        // fetch 'release' and tags to local history
        refSpec {
            +"refs/heads/main"
            +"refs/tags/*:refs/tags/*"
        }
    }

    container(displayName = "Unit tests", image = gradleImageVersion) {
        workDir = "server"
        kotlinScript { api ->
            api.gradle("test")
        }
    }

    container(displayName = "Push git tag", image = "bitnami/git") {
        shellScript {
            content = """
                   BRANCH=${'$'}JB_SPACE_GIT_BRANCH
                   echo BRANCH
               """.trimIndent()
        }
    }
}

job("Tests for main branch") {
    startOn {
        gitPush {
            anyRefMatching {
                +"refs/heads/main"
            }
        }
    }

    container(displayName = "Unit tests", image = gradleImageVersion) {
        workDir = "server"
        kotlinScript { api ->
            api.gradle("test")
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
            api.gradle("apiTest")
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

        kotlinScript { api ->
            val deployVersion = api.space().projects.automation.deployments.get(
                project = api.projectIdentifier(),
                targetIdentifier = TargetIdentifier.Key("azure-dev"),
                deploymentIdentifier = DeploymentIdentifier.Status(DeploymentIdentifierStatus.scheduled)
            ).version
            api.space().projects.automation.deployments.start(
                project = api.projectIdentifier(),
                targetIdentifier = TargetIdentifier.Key("azure-dev"),
                version = deployVersion,
                syncWithAutomationJob = true
            )
            api.gradle("azureWebAppDeploy")
        }
    }
}

fun getNextSundayDate(): Instant {
    var date = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC"))
    // set time to 11:59 pm UTC
    date = date.withHour(23).withMinute(59).withSecond(0).withNano(0)
    val sunday = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
    return Instant.parse(sunday.format(DateTimeFormatter.ISO_INSTANT))
}
