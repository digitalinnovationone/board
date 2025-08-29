plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "br.com.dio"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.liquibase:liquibase-core:4.29.1")
    implementation("com.mysql:mysql-connector-j:8.0.33")
    implementation("org.projectlombok:lombok:1.18.34")

    annotationProcessor("org.projectlombok:lombok:1.18.34")
}

tasks.test {
    useJUnitPlatform()
}

// Configure a classe principal do seu aplicativo
tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = "br.com.dio.Main" // Substitua pelo caminho completo da sua classe principal
    }
    archiveClassifier.set("") // Remove o sufixo "-all" do nome do arquivo
}