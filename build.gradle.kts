plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "br.com.dio"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("org.liquibase:liquibase-core:4.23.0")
    implementation("org.projectlombok:lombok:1.18.28")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
    
    annotationProcessor("org.projectlombok:lombok:1.18.28")
}

application {
    mainClass.set("br.com.dio.Main")
}

tasks.shadowJar {
    archiveFileName.set("app.jar")
    manifest {
        attributes("Main-Class" to "br.com.dio.Main")
    }
}