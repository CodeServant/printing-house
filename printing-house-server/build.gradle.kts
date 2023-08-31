import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
}

group = "pl.macia"
version = project.properties["applicationVersion"].toString()


allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        val springVersion = project.properties["springBootVersion"].toString()
        val kotlinVersion = project.properties["kotlinVersion"].toString()
        implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
        implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
        testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
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