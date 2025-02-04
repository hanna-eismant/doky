dependencyManagement {
    imports {
        mavenBom(libs.spring.boot.bom.get().toString())
    }
}

dependencies {
    implementation(libs.spring.ai.bom)
    implementation(libs.azure.ai.search)
}
