import com.github.gradle.node.npm.task.NpmTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    id("com.github.node-gradle.node") version "7.0.2"
}

group = "org.hkurh.doky"
version = "Aardvark-v0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

node {
    download = true
    version = "20.12.2"
    npmInstallCommand = "ci"
    nodeProjectDir = file("${project.projectDir}/doky-front")
}

tasks.register<NpmTask>("npmBuild") {
    dependsOn("npmInstall")
    args = listOf("run", "build")
}

tasks.register<Copy>("copyFrontDist") {
    dependsOn("npmBuild")
    from("$projectDir/doky-front/dist")
    into("$buildDir/resources/main/static")
}

tasks.named("processResources") {
    dependsOn("copyFrontDist")
}
