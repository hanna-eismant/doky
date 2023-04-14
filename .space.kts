/**
 * JetBrains Space Automation
 * This Kotlin-script file lets you automate build activities
 * For more info, see https://www.jetbrains.com/help/space/automation.html
 */

job("Unit Tests") {
    container(displayName = "Run unit tests", image = "gradle:6.9.0-jdk11") {
        workDir = "server"
        kotlinScript { api ->
            api.gradle("test")
        }
    }
}

job("API Tests") {
    container(displayName = "Run integration tests", image = "gradle:6.9.0-jdk11") {
        env["DB_HOST"] = "db"
        env["DB_PORT"] = "3306"
        service("mysql:8") {
            alias("db")
            args("--log_bin_trust_function_creators=ON",
                    "--max-connections=700")
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
}

job("Azure DEV Deployment") {
    startOn {
        gitPush {
            branchFilter {
                +"refs/heads/develop"
            }
        }
    }
    parameters {
        text("spring-profile", value = "dev")
    }

    container(displayName = "Deploy artifact", image = "gradle:6.9.0-jdk11") {
        workDir = "server"

        env["AZURE_SUBSCRIPTION"] = "{{ project:azure-subscription }}"
        env["AZURE_RESOURCE_GROUP"] = "{{ project:azure-resource-group }}"
        env["AZURE_APP_NAME"] = "{{ project:azure-app-name }}"
        env["AZURE_TENANT"] = "{{ project:azure-tenant }}"
        env["AZURE_CLIENT"] = "{{ project:azure-client }}"
        env["AZURE_AUTH_KEY"] = "{{ project:azure-auth-key }}"

        env["SPRING_PROFILES_ACTIVE"] = "{{ spring-profile }}"
        env["SPRING_DATASOURCE_URL"] = "{{ project:spring-datasource-url }}"
        env["SPRING_DATASOURCE_USERNAME"] = "{{ project:spring-datasource-username }}"
        env["SPRING_DATASOURCE_PASSWORD"] = "{{ project:spring-datasource-password }}"

        kotlinScript { api ->
            api.space().projects.automation.deployments.start(
                    project = api.projectIdentifier(),
                    targetIdentifier = TargetIdentifier.Key("azure-dev"),
                    version = "Aardvark-v0.1." + api.executionNumber(),
                    syncWithAutomationJob = true
            )
            api.gradle("azureWebAppDeploy")
        }
    }
}
