plugins {
    alias(libs.plugins.kotlin.kmm) apply false
    alias(libs.plugins.kotlin.allopen) apply false
    alias(libs.plugins.gradle.versions)
}

allprojects {
    repositories {
        mavenCentral()
    }
}
