import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

var deploymentKey = "azure-dev-front"

job("Tests for main branch") {
    startOn {
        gitPush {
            anyRefMatching {
                +"refs/heads/main"
            }
        }
    }

    host("Schedule Azure DEV Deployment") {
        kotlinScript { api ->
            val deployVersion = "Aardvark-v0.1." + api.executionNumber() + "f"
            api.space().projects.automation.deployments.schedule(
                project = api.projectIdentifier(),
                targetIdentifier = TargetIdentifier.Key(deploymentKey),
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

    host("Sync deployment status") {
        kotlinScript { api ->
            val deployVersion = api.space().projects.automation.deployments.get(
                project = api.projectIdentifier(),
                targetIdentifier = TargetIdentifier.Key(deploymentKey),
                deploymentIdentifier = DeploymentIdentifier.Status(DeploymentIdentifierStatus.scheduled)
            ).version
            api.space().projects.automation.deployments.start(
                project = api.projectIdentifier(),
                targetIdentifier = TargetIdentifier.Key(deploymentKey),
                version = deployVersion,
                syncWithAutomationJob = true
            )
        }
    }

    val sharedBuildPath = "to-deploy"
    val zipFile = "dist.zip"
    container(displayName = "Build", image = "node:18-alpine") {
        shellScript {
            content = """
                   npm ci && npm run build
                   mkdir ${'$'}JB_SPACE_FILE_SHARE_PATH/$sharedBuildPath
                   cd dist
                   cp -a . ${'$'}JB_SPACE_FILE_SHARE_PATH/$sharedBuildPath
                   cd ${'$'}JB_SPACE_FILE_SHARE_PATH/$sharedBuildPath
                   ls -la
               """.trimIndent()
        }
    }

    container(displayName = "Zip dist", image = "joshkeegan/zip") {
        shellScript {
            content = """
                   cd ${'$'}JB_SPACE_FILE_SHARE_PATH/$sharedBuildPath
                   zip -r $zipFile *             
                   ls -la
               """.trimIndent()
        }
    }

    container(displayName = "Deploy to azure", "mcr.microsoft.com/azure-cli") {
        env["AZURE_SUBSCRIPTION"] = "{{ project:azure-subscription }}"
        env["AZURE_RESOURCE_GROUP"] = "{{ project:azure-resource-group }}"
        env["AZURE_APP_NAME"] = "{{ project:azure-app-name-front }}"
        env["AZURE_TENANT"] = "{{ project:azure-tenant }}"
        env["AZURE_CLIENT"] = "{{ project:azure-client }}"
        env["AZURE_AUTH_KEY"] = "{{ project:azure-auth-key }}"

        shellScript {
            content = """
                az login --service-principal -t ${'$'}AZURE_TENANT -u ${'$'}AZURE_CLIENT -p ${'$'}AZURE_AUTH_KEY
                az webapp deployment source config-zip -g ${'$'}AZURE_RESOURCE_GROUP -n ${'$'}AZURE_APP_NAME --src ${'$'}JB_SPACE_FILE_SHARE_PATH/$sharedBuildPath/$zipFile
            """
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
