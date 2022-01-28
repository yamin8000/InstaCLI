import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.6.10"
    kotlin("jvm") version kotlinVersion
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("java")
}

group = "io.github.yamin8000"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("com.github.instagram4j:instagram4j:2.0.7")
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta4")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("io.github.yamin8000.MainKt")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "io.github.yamin8000.MainKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}