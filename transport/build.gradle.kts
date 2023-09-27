plugins {
    alias(libs.plugins.kotlin.kmm)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvm {}
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.serialization.json)
            }
        }
        val commonTest by getting
    }
}