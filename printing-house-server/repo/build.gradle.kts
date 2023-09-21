import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    alias(libs.plugins.spring.depManagement)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.allopen)
}

group = "pl.macia"
version = libs.versions.app.get()


dependencies {
    runtimeOnly(libs.db.mysql)
    implementation(libs.poko)
    implementation(libs.spring.boot.jpa)
    implementation(libs.spring.boot.validation)
    testImplementation(libs.db.h2)
}

allOpen {
    annotation("jakarta.persistence.Table")
}

tasks.named<BootJar>("bootJar") {
    enabled = false
}

tasks.withType<DokkaTaskPartial>().configureEach {
    dokkaSourceSets.configureEach {
        includes.from("packages.md")
    }
}
tasks.dokkaHtml {
    dokkaSourceSets {
        configureEach {
            documentedVisibilities.set(
                setOf(
                    DokkaConfiguration.Visibility.INTERNAL
                )
            )
            includes.from("packages.md")
        }
    }
}