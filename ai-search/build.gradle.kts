dependencyManagement {
    imports {
        mavenBom(libs.spring.boot.bom.get().toString())
    }
}

dependencies {
    implementation(libs.azure.search)
    implementation(libs.bundles.spring.basic)
}
