import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.6.10"
    kotlin("jvm") version kotlinVersion
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("java")
    id("org.jetbrains.dokka") version kotlinVersion
}

group = "io.github.yamin8000"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.instagram4j:instagram4j:2.0.7")
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta5")

    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.6")
    implementation("org.slf4j:slf4j-simple:2.0.0-alpha7")
}

application {
    mainClass.set("io.github.yamin8000.MainKt")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.dokkaHtml.configure {
    outputDirectory.set(projectDir.resolve("docs/dokka/html"))
}

tasks.dokkaJavadoc.configure {
    outputDirectory.set(projectDir.resolve("docs/dokka/javadoc"))
}

tasks.dokkaJekyll.configure {
    outputDirectory.set(projectDir.resolve("docs/dokka/jekyll"))
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