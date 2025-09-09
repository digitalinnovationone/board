#!/usr/bin/env bash

# wait-for-it.sh
# Espera o serviço de banco de dados estar pronto
host="$1"
port="$2"
shift 2

# Espera até que o host e a porta estejam acessíveis
until nc -z "$host" "$port"; do
  echo "Aguardando o banco de dados... ($host:$port)"
  sleep 1
done

# Executa o comando principal da aplicação
exec "$@"