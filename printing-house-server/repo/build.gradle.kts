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

dependencies {
    runtimeOnly(libs.db.mysql)
    implementation(libs.poko)
    api(libs.spring.boot.jpa)
    api(libs.spring.boot.validation)
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