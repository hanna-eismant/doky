
/**
 * JetBrains Space Automation
 * This Kotlin-script file lets you automate build activities
 * For more info, see https://www.jetbrains.com/help/space/automation.html
 */

job("Server build and unit tests") {
    container(displayName = "Run gradlew from another dir", image = "gradle:jdk11") {
        workDir = "server"
        kotlinScript { api ->
            api.gradle("test")
        }
    }
}
