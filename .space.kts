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

job("Tests for PR") {
    startOn {
        codeReviewOpened {
            branchToCheckout = CodeReviewBranch.MERGE_REQUEST_SOURCE
        }
    }

    container(displayName = "Unit tests", image = gradleImageVersion) {
        workDir = "server"
        kotlinScript { api ->
            api.gradle("test")
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

job("Deploy front to azure") {
    val sharedBuildPath = "client/dist"
    container(displayName = "Build", image = "node:18-alpine") {
        workDir = "client"
        shellScript {
            content = """
                   npm ci && npm run build
                   cd dist && ls -la
                   cp -a . ${'$'}JB_SPACE_FILE_SHARE_PATH/$sharedBuildPath/
               """.trimIndent()
        }
    }

    container(displayName = "Deploy", image = "") {
        env["FTP_URL"] = "{{ project:azure-front-ftp-url }}"
        env["FTP_USER"] = "{{ project:azure-front-ftp-username }}"
        env["FTP_PASS"] = "{{ project:azure-front-ftp-password }}"
        shellScript {
            content = """
                ls -la ${'$'}JB_SPACE_FILE_SHARE_PATH/$sharedBuildPath
                curl -T index.html ${'$'}FTP_URL -u ${'$'}FTP_USER:${'$'}FTP_PASS 
            """.trimMargin()
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
