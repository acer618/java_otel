/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.8/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    id("java")
    id ("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is used by the application.
    implementation(libs.guava)

    implementation(platform("io.opentelemetry:opentelemetry-bom:1.39.0"))
    //implementation("io.opentelemetry:opentelemetry-api")
    //implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-logging:1.39.0")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp:1.39.0")

    //alpha modules
    implementation("io.opentelemetry:opentelemetry-semconv:1.30.1-alpha")
    implementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure:1.39.0")
    implementation("io.opentelemetry:opentelemetry-api-incubator:1.39.0-alpha")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    // Define the main class for the application.
    mainClass = "org.example.App"
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
