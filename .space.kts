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
            api.gradle("integrationTest")
        }
    }

}
