import org.jetbrains.dokka.gradle.DokkaMultiModuleTask

plugins {
    alias(libs.plugins.spring.depManagement)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.dokka)
}

allprojects {
    group = "pl.macia.printing-house"
    version = rootProject.libs.versions.app.get()

    apply(plugin = rootProject.libs.plugins.kotlin.jvm.get().pluginId)
    apply(plugin = rootProject.libs.plugins.dokka.get().pluginId)

    dependencies {
        implementation(rootProject.libs.spring.boot.web)
        implementation(rootProject.libs.kotlin.reflect)
        testImplementation(rootProject.libs.spring.boot.test)
        implementation(rootProject.libs.jgrapht)
    }

    kotlin {
        jvmToolchain(rootProject.libs.versions.java.get().toInt())
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

dependencies {
    implementation(project("repo"))
    implementation(project(":transport"))
    implementation(libs.spring.boot.security)
    testImplementation(libs.spring.security.test)
    implementation(libs.springdoc.openapi.common)
}

tasks.withType<DokkaMultiModuleTask>().configureEach {
    includes.from("packages.md")
}

tasks.bootRun {
    dependencies {
        implementation(libs.springdoc.openapi.webmvc.ui)
    }
}

tasks.test {
    systemProperty("spring.profiles.active", "dev")
}