# Criar e Subir aplicação
docker-compose build      
# Executar com stty para configurar terminal
docker-compose run --rm app bash -c "stty sane && java -jar app.jar"