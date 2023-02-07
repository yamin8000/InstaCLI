import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.7.10"
    kotlin("jvm") version kotlinVersion
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("java")
}

group = "io.github.instakiller"
version = "1.0.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.instagram4j:instagram4j:2.0.7")
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta11")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.14.2")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.10")
    implementation("org.slf4j:slf4j-simple:2.0.4")
}

application {
    mainClass.set("io.github.instakiller.MainKt")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "io.github.instakiller.MainKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}