plugins {
    alias(libs.plugins.kotlin.kmm)
    alias(libs.plugins.kotlin.serialization)
}

project.group = "pl.macia.printing-house"
project.version = libs.versions.app.get()

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