import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.6.21"
    kotlin("jvm") version kotlinVersion
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("java")
    id("org.jetbrains.dokka") version "1.6.20"
}

group = "io.github.instakiller"
version = "1.0.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.instagram4j:instagram4j:2.0.7")
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta6")

    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.7")
    implementation("org.slf4j:slf4j-simple:2.0.0-alpha7")
}

application {
    mainClass.set("io.github.instakiller.MainKt")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.dokkaJekyll.configure {
    outputDirectory.set(projectDir.resolve("docs"))
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