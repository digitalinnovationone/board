# Usa uma imagem base oficial do OpenJDK para Java 17
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia os arquivos de build e o script 'wait-for-it.sh' para o contêiner
# Copie o script antes dos arquivos de código para aproveitar o cache do Docker
COPY build.gradle.kts settings.gradle.kts ./
COPY gradlew gradlew.bat ./
COPY gradle ./gradle
COPY wait-for-it.sh ./

# Copia o código-fonte
COPY src ./src

# Torna o gradlew e o wait-for-it.sh executáveis
RUN chmod +x ./gradlew
RUN chmod +x ./wait-for-it.sh

# Executa o build do projeto para gerar o JAR
RUN ./gradlew clean build -x test

# Move o JAR gerado para o diretório de trabalho e o renomeia para um nome genérico
RUN mv build/libs/*.jar app.jar

# Expõe a porta 8080 (opcional, mas boa prática)
EXPOSE 8080

# Comando para rodar a aplicação quando o contêiner for iniciado
# Usa o wait-for-it.sh para esperar o banco de dados
# O host (db) e a porta (3306) devem ser configurados no docker-compose.yml
CMD ["./wait-for-it.sh", "db:3306", "--", "java", "-jar", "app.jar"]