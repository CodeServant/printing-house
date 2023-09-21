import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.spring.depManagement)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.dokka)
}

group = "pl.macia"
version = libs.versions.app.get()

allprojects {
    apply(plugin = rootProject.libs.plugins.kotlin.jvm.get().pluginId)
    apply(plugin = rootProject.libs.plugins.dokka.get().pluginId)

    dependencies {
        implementation(rootProject.libs.spring.boot.web)
        implementation(rootProject.libs.kotlin.reflect)
        testImplementation(rootProject.libs.spring.boot.test)
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    repositories {
        mavenCentral()
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
dependencies {
    implementation(project("repo"))
}

tasks.withType<DokkaMultiModuleTask>().configureEach {
    includes.from("packages.md")
}