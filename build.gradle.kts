plugins {
    id("java")
    id("org.springframework.boot") version "3.2.2" // Certifique-se de usar a versão mais recente
    id("io.spring.dependency-management") version "1.1.4"
}

group = "br.com.dio"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Dependências do Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web") // Adiciona suporte a controllers REST
    implementation("org.springframework.boot:spring-boot-starter-security") // Adiciona suporte à segurança do Spring Boot

    // Banco de Dados e Liquibase
    implementation("org.liquibase:liquibase-core:4.29.1")
    implementation("mysql:mysql-connector-java:8.0.33")

    // Lombok (para reduzir código boilerplate)
    implementation("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    // Testes
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test") // Para rodar testes do Spring Boot
}

tasks.test {
    useJUnitPlatform()
}
