plugins {
    alias(libs.plugins.kotlin.kmm)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvm {}
    js(IR) {
        browser()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlin.serialization.json)
                api(libs.kotlin.datetime)
            }
        }
        val commonTest by getting
    }
}