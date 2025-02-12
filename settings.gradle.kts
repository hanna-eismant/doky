plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "doky"
include("app-server")
include("cleanup-functions")
include("email-service")
include("indexer-service")
include("persistence")
include("ai-search")
