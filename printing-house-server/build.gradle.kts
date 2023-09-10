import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.spring.depManagement)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
}

group = "pl.macia"
version = libs.versions.app.get()

val jvmPlug = libs.plugins.kotlin.jvm.get().pluginId
val reflect = libs.kotlin.reflect
val springWeb = libs.spring.boot.web
val springTest = libs.spring.boot.test
allprojects {
    apply(plugin = jvmPlug)

    dependencies {
        implementation(springWeb)
        implementation(reflect)
        testImplementation(springTest)
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