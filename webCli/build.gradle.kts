import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlin.kmm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kvision)
}

repositories {
    mavenCentral()
    mavenLocal()
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                outputFileName = "main.bundle.js"
            }
            runTask {
                sourceMaps = false
                devServer = KotlinWebpackConfig.DevServer(
                    open = false,
                    port = 3000,
                    proxy = mutableMapOf(
                        "/kv/*" to "http://localhost:8080",
                        "/kvsse/*" to "http://localhost:8080",
                        "/kvws/*" to mapOf("target" to "ws://localhost:8080", "ws" to true)
                    ),
                    static = mutableListOf("${layout.buildDirectory.asFile.get()}/processedResources/js/main")
                )
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
        binaries.executable()
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(libs.kvision)
                implementation(libs.kvision.bootstrap)
                implementation(libs.kvision.rest)
                implementation(project(":transport"))
                implementation(libs.kvision.state)
                implementation(libs.kvision.tabulator)
                implementation(libs.kvision.fontawesome)
                implementation(libs.kvision.tom.select)
                implementation(libs.kvision.i18n)
                implementation(libs.kvision.datetime)
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
                implementation(libs.kvision.testutils)
            }
        }
    }
}
