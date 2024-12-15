dependencyManagement {
    imports {
        mavenBom(libs.spring.boot.bom.get().toString())
    }
}

dependencies {
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.bundles.flyway)
    implementation(libs.bundles.database.connectors)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}
