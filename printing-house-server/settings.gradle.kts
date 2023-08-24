rootProject.name = "printing-house-server"

pluginManagement {
    val springBootVersion: String by settings
    val kotlinVersion: String by settings
    val springDependencyManagement: String by settings
    plugins {
        id("org.springframework.boot") version springBootVersion
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        kotlin("plugin.jpa") version kotlinVersion
        kotlin("plugin.allopen") version kotlinVersion
        id("io.spring.dependency-management") version springDependencyManagement
    }
}