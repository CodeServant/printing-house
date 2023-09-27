plugins {
    alias(libs.plugins.kotlin.kmm) apply false
    alias(libs.plugins.kotlin.allopen) apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}
