rootProject.name = "server"

dependencyResolutionManagement {
    repositories {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

        // Declare the Node.js download repository
        ivy {
            name = "Node.js"
            setUrl("https://nodejs.org/dist/")
            patternLayout {
                artifact("v[revision]/[artifact](-v[revision]-[classifier]).[ext]")
            }
            metadataSources {
                artifact()
            }
            content {
                includeModule("org.nodejs", "node")
            }
        }
    }
}
