<!-- 
  Tags: Dev
  Label: ☯ Board
  Description: Board
  path_hook: hookfigma.hook7
-->

# Board Management System - DIO Challenge

Sistema de gerenciamento de boards estilo Kanban desenvolvido em Java, utilizando arquitetura em camadas com padrão DAO e migrations com Liquibase.

## Visão Geral

Sistema completo para gerenciamento de boards Kanban que permite:
- Criar e gerenciar boards personalizados
- Organizar tarefas em colunas (cards)
- Mover cards entre colunas seguindo fluxo definido
- Bloquear/desbloquear cards com justificativas
- Cancelar cards movendo para coluna específica
- Visualizar detalhes completos de boards e cards

## Arquitetura

### Estrutura de Camadas
```
┌─────────────────┐
│       UI        │ ← Interface do usuário (console)
├─────────────────┤
│    Services     │ ← Lógica de negócio
├─────────────────┤
│      DAO        │ ← Acesso a dados
├─────────────────┤
│   Persistence   │ ← Entidades e configuração
└─────────────────┘
```

### Componentes Principais

**Camada UI**
- `MainMenu`: Menu principal do sistema
- `BoardMenu`: Menu específico para operações dentro de um board

**Camada Services**
- `BoardService`: Operações de criação e exclusão de boards
- `BoardQueryService`: Consultas relacionadas a boards
- `CardService`: Operações CRUD e movimentação de cards
- `CardQueryService`: Consultas relacionadas a cards
- `BoardColumnQueryService`: Consultas de colunas

**Camada DAO**
- `BoardDAO`: Persistência de boards
- `BoardColumnDAO`: Persistência de colunas
- `CardDAO`: Persistência de cards
- `BlockDAO`: Persistência de bloqueios

**Entidades**
- `BoardEntity`: Representa um board
- `BoardColumnEntity`: Representa uma coluna do board
- `CardEntity`: Representa um card
- `BlockEntity`: Representa um bloqueio

**DTOs**
- `BoardDetailsDTO`: Detalhes completos de um board
- `BoardColumnDTO`: Informações de coluna com contagem de cards
- `CardDetailsDTO`: Detalhes completos de um card
- `BoardColumnInfoDTO`: Informações básicas de coluna

## Modelo de Dados

### Tipos de Colunas
```java
public enum BoardColumnKindEnum {
    INITIAL,  // Coluna inicial
    PENDING,  // Colunas intermediárias
    FINAL,    // Coluna final
    CANCEL    // Coluna de cancelamento
}
```

### Relacionamentos
- Um **Board** possui múltiplas **BoardColumns**
- Uma **BoardColumn** possui múltiplos **Cards**
- Um **Card** pode ter múltiplos **Blocks** (histórico de bloqueios)

## Funcionalidades

### Gerenciamento de Boards
1. **Criar Board**: Define nome e estrutura de colunas personalizadas
2. **Selecionar Board**: Acessa um board específico para operações
3. **Excluir Board**: Remove um board do sistema
4. **Visualizar Board**: Mostra estrutura completa com contagem de cards

### Gerenciamento de Cards
1. **Criar Card**: Adiciona novo card na coluna inicial
2. **Mover Card**: Move para próxima coluna seguindo fluxo sequencial
3. **Bloquear Card**: Impede movimentação com justificativa obrigatória
4. **Desbloquear Card**: Libera card bloqueado com motivo
5. **Cancelar Card**: Move card para coluna de cancelamento
6. **Visualizar Card**: Mostra detalhes completos incluindo histórico

### Regras de Negócio
- Cards bloqueados não podem ser movidos
- Cards finalizados não podem ser alterados
- Cards cancelados não podem ser movidos
- Sistema mantém histórico completo de bloqueios
- Movimentação segue ordem sequencial das colunas

## Tecnologias Utilizadas

- **Java 17+**: Linguagem principal
- **MySQL**: Banco de dados relacional
- **Liquibase**: Controle de versão do banco de dados
- **Lombok**: Redução de boilerplate code
- **JDBC**: Conectividade com banco de dados
- **Docker/Docker Compose**: Orquestração e containerização

## Como Executar

### Pré-requisitos
- Docker
- Docker Compose

### Execução com Docker (Recomendado)
```bash
cd board-dio
./gradlew clean shadowJar
docker-compose build
```

### Modo Interativo
```bash
# Executar aplicação em modo interativo
./runapp.sh
```

### Execução Alternativa
```bash
docker-compose up -d db && docker-compose run --rm --no-deps app
```

### Execução Local (Desenvolvimento)
**Pré-requisitos:**
- Java 17+
- MySQL Server
- Gradle 7.0+

**Configuração do Banco:**
```sql
CREATE DATABASE board;
CREATE USER 'board'@'localhost' IDENTIFIED BY 'board';
GRANT ALL PRIVILEGES ON board.* TO 'board'@'localhost';
```

**Execução:**
```bash
./gradlew run
```

### Testando o Banco de Dados
```bash
docker exec -it board-dio-db mysql -u board -p # senha: board
USE board;
SHOW TABLES;
SELECT * FROM BOARDS;
```

## Melhorias Implementadas

### Containerização Completa
- Execução simplificada em qualquer ambiente com Docker
- Orquestração automática da aplicação e banco de dados
- Eliminação de problemas de dependências e configurações

### Arquitetura Robusta
- Separação clara de responsabilidades em camadas
- Padrão DAO para abstração do acesso a dados
- Uso de DTOs para transferência eficiente de dados
- Sistema de migrations para controle de versão do schema

## Conceitos Demonstrados

- **Arquitetura em Camadas**: Separação clara de responsabilidades
- **Padrão DAO**: Abstração do acesso a dados
- **DTO Pattern**: Transferência eficiente de dados
- **Migrations**: Controle de versão do schema
- **Transações**: Consistência de dados
- **Enum com Métodos**: Encapsulamento de lógica
- **Optional**: Tratamento seguro de valores nulos
- **Try-with-resources**: Gerenciamento automático de recursos

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/br/com/dio/
│   │   ├── Main.java
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── persistence/
│   │   │   ├── config/
│   │   │   ├── converter/
│   │   │   ├── dao/
│   │   │   ├── entity/
│   │   │   └── migration/
│   │   ├── service/
│   │   └── ui/
│   └── resources/
│       └── db/changelog/
├── docker-compose.yml
├── Dockerfile
└── build.gradle
```

---

**Desafio**: DIO - Digital Innovation One  
**Tecnologias**: Java, MySQL, Docker, Liquibase  
**Padrões**: DAO, DTO, Arquitetura em Camadas

---

**Autor**: [Fabiano Rocha/Fabiuniz](https://github.com/SeuUsuarioGitHub)
**Desafio**: DIO - Digital Innovation One  
**Data**: 2025