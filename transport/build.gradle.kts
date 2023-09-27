plugins {
    alias(libs.plugins.kotlin.kmm)
}

kotlin {
    jvm {}
    sourceSets {
        val commonMain by getting
        val commonTest by getting
    }
}