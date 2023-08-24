import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    kotlin("plugin.allopen")
}

group = "pl.macia"
version = project.properties["applicationVersion"].toString()

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    val springVersion = project.properties["springBootVersion"].toString()
    val kotlinVersion = project.properties["kotlinVersion"].toString()
    val mysqlVersion = project.properties["mysqlVersion"].toString()
    val pokoVersion = project.properties["pokoVersion"].toString()
    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    runtimeOnly("com.mysql:mysql-connector-j:$mysqlVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springVersion")
    implementation("dev.drewhamilton.poko:poko-annotations:$pokoVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

allOpen {
    annotation("jakarta.persistence.Table")
}