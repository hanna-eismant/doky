dependencyManagement {
    imports {
        mavenBom(libs.spring.boot.bom.get().toString())
    }
}

dependencies {
    implementation(libs.azure.search)
    implementation("org.springframework:spring-core")
    implementation("org.springframework:spring-context")
}
