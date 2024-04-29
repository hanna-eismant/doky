/**
 * JetBrains Space Automation
 * This Kotlin-script file lets you automate build activities
 * For more info, see https://www.jetbrains.com/help/space/automation.html
 */

val gradleImageVersion = "gradle:8.2-jdk17"
val deploymentKey = "azure-dev"

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

        env["BUILD_COMMIT"] = "{{ run:git-checkout.commit }}"

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
            // -P is used by gradle (i.e. name of package), -D is used to pass to azure deployment
            api.gradlew("azureWebAppDeploy", "-DdeployVersion=$deployVersion", "-PdeployVersion=$deployVersion")
        }
    }
}
