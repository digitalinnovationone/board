# Usa uma imagem base oficial do OpenJDK para Java 17
FROM openjdk:17-jdk-slim

# Instala o utilitário 'netcat' necessário para o wait-for-it.sh
RUN apt-get update && apt-get install -y netcat

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia os arquivos do projeto para o contêiner
# Copie o script antes dos arquivos de código para aproveitar o cache do Docker
COPY build.gradle.kts settings.gradle.kts ./
COPY gradlew gradlew.bat ./
COPY gradle ./gradle
COPY wait-for-it.sh ./
RUN apt-get update && apt-get install -y dos2unix
RUN dos2unix wait-for-it.sh
RUN chmod +x ./wait-for-it.sh
COPY src ./src
# Copia o arquivo .zip do Gradle para o contêiner para evitar download lento
COPY gradle-8.8-bin.zip /root/.gradle/wrapper/dists/gradle-8.8-bin/

# Torna o gradlew e o wait-for-it.sh executáveis
# Agora, estas linhas vêm antes do build, para que a próxima etapa possa usar o cache
RUN chmod +x ./gradlew
RUN chmod +x ./wait-for-it.sh

# Executa o build do projeto
# O wrapper vai encontrar o arquivo local e não tentará baixar novamente
RUN ./gradlew clean shadowJar -x test

# Move o JAR gerado para o diretório de trabalho e o renomeia para um nome genérico
RUN mv build/libs/*.jar app.jar

# Expõe a porta 8080 (opcional, mas boa prática)
EXPOSE 8080

# Comando para rodar a aplicação quando o contêiner for iniciado
# Usa o wait-for-it.sh para esperar o banco de dados
# O host (db) e a porta (3306) devem ser configurados no docker-compose.yml
CMD ["./wait-for-it.sh", "board-dio-db", "3306", "--", "java", "-jar", "app.jar"]