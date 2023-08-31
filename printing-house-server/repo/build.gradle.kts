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


dependencies {
    val mysqlVersion = project.properties["mysqlVersion"].toString()
    val pokoVersion = project.properties["pokoVersion"].toString()
    val springVersion = project.properties["springBootVersion"].toString()
    runtimeOnly("com.mysql:mysql-connector-j:$mysqlVersion")
    implementation("dev.drewhamilton.poko:poko-annotations:$pokoVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springVersion")
}

allOpen {
    annotation("jakarta.persistence.Table")
}