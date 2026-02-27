# 📋 Gerenciador de Boards e Cards

Projeto de aplicação desktop para gerenciar **boards kanban** com **cards** (tarefas), desenvolvido em **Java 17+** com foco em arquitetura limpa, padrões SOLID e boas práticas de código.

Aplicação refatorada com ênfase em modernização de código, JDBC robusto, tratamento centralizado de exceções e separação clara de responsabilidades entre camadas.

------------------------------------------------------------------------

## 🎯 Objetivo

Demonstrar a implementação de uma aplicação empresarial com:

- **Arquitetura em Camadas** (DAO, Service, DTO, UI)
- **Padrões de Persistência** (DAO Pattern, Repository)
- **Tratamento Robusto de Exceções** (Checked → Unchecked)
- **JDBC Moderno** (try-with-resources, PreparedStatement com chaves geradas)
- **Separação de Responsabilidades** (Business Logic, Data Access, UI)
- **Boas Práticas Java 17+** (records, Optional, var, lambda expressions)

------------------------------------------------------------------------

## 🧩 Componentes Principais

### 📊 Camada de Persistência (`persistence/`)

**DAOs (Data Access Objects)**
- `BoardDAO` - Operações CRUD para boards
- `BoardColumnDAO` - Gerenciamento de colunas do board
- `CardDAO` - Operações com cards/tarefas
- `BlockDAO` - Registro de bloqueios de cards

**Entidades**
- `BoardEntity` - Representa um board kanban
- `BoardColumnEntity` - Colunas do board (INITIAL, PENDING, FINAL, CANCEL)
- `CardEntity` - Tarefas do board
- `BlockEntity` - Histórico de bloqueios

**Configuração**
- `ConnectionConfig` - Gerenciamento de conexões MySQL
- `JdbcUtils` - Utilitários JDBC (geração de chaves, try-with-resources)

### 💼 Camada de Serviço (`service/`)

**Services**
- `BoardService` - Lógica de negócio para boards
- `CardService` - Operações e validações de cards
- `BoardQueryService` - Consultas complexas de boards
- `BoardColumnQueryService` - Busca de colunas com cards
- `CardQueryService` - Detalhes de cards

**Características**
- Transações com rollback automático em caso de erro
- Validação de regras de negócio
- Conversão entre entidades e DTOs
- Tratamento de exceções personalizadas

### 📦 Camada de Transferência de Dados (`dto/`)

- `BoardDetailsDTO` - Informações de board com colunas
- `BoardColumnDTO` - Coluna com contagem de cards
- `BoardColumnInfoDTO` - Metadata de colunas para validações
- `CardDetailsDTO` - Detalhes completos do card (Record)

### 🎨 Camada de Apresentação (`ui/`)

- `MainMenu` - Menu principal para gerenciar boards
- `BoardMenu` - Menu interativo para operações dentro de um board
- Entrada de dados validada
- Tratamento de erros com mensagens amigáveis

### ⚠️ Tratamento de Exceções (`exception/`)

- `DataAccessException` - Erros de acesso a dados (unchecked)
- `ValidationException` - Erros de validação (unchecked)
- `EntityNotFoundException` - Entidade não encontrada
- `CardBlockedException` - Card bloqueado
- `CardFinishedException` - Card finalizado

### 🔧 Utilitários

- `ValidationUtils` - Validações centralizadas
- `OffsetDateTimeConverter` - Conversão de timestamps

------------------------------------------------------------------------

## 🛠️ Melhorias Implementadas

Durante a refatoração foram aplicadas:

### ✅ JDBC Moderno
- Remoção de classes driver-específicas (`StatementImpl`)
- `PreparedStatement.RETURN_GENERATED_KEYS` para obter IDs auto-gerados
- Try-with-resources para ResultSet, Statement e Connection
- Mapeamento centralizado de ResultSet para objetos

### ✅ Tratamento de Exceções
- Remoção de checked `SQLException` das APIs públicas
- Wrapping de SQLExceptions em `DataAccessException` (unchecked)
- Tratamento de transações (commit/rollback)
- Mensagens de erro contextualizadas

### ✅ Código Moderno Java 17+
- Uso de `var` para inferência de tipos
- `Optional<T>` para valores nulos
- Records para DTOs imutáveis
- Lambda expressions e method references
- Text blocks para queries SQL

### ✅ Separação de Responsabilidades
- DAOs encapsulam JDBC
- Services implementam lógica de negócio
- DTOs estruturam dados entre camadas
- UI isolada de persistência

### ✅ Logging
- Remoção de `System.out.println` em produção
- Integração com `java.util.logging` na migração

### ✅ Organização de Código
- Constantes centralizadas
- Imports bem organizados
- Métodos com propósito único
- Documentação via Javadoc

------------------------------------------------------------------------

## 📂 Estrutura do Projeto

```
board/
├── src/main/java/br/com/dio/
│   ├── Main.java                          # Ponto de entrada
│   ├── dto/                               # Data Transfer Objects
│   │   ├── BoardDetailsDTO.java
│   │   ├── BoardColumnDTO.java
│   │   ├── BoardColumnInfoDTO.java
│   │   └── CardDetailsDTO.java
│   ├── exception/                         # Exceções personalizadas
│   │   ├── DataAccessException.java
│   │   ├── ValidationException.java
│   │   ├── EntityNotFoundException.java
│   │   ├── CardBlockedException.java
│   │   └── CardFinishedException.java
│   ├── persistence/
│   │   ├── config/                        # Configuração de BD
│   │   │   ├── ConnectionConfig.java
│   │   │   └── JdbcUtils.java
│   │   ├── converter/                     # Conversores de tipos
│   │   │   └── OffsetDateTimeConverter.java
│   │   ├── dao/                           # Data Access Objects
│   │   │   ├── BoardDAO.java
│   │   │   ├── BoardColumnDAO.java
│   │   │   ├── CardDAO.java
│   │   │   └── BlockDAO.java
│   │   ├── entity/                        # Entidades do domínio
│   │   │   ├── BoardEntity.java
│   │   │   ├── BoardColumnEntity.java
│   │   │   ├── BoardColumnKindEnum.java
│   │   │   ├── CardEntity.java
│   │   │   └── BlockEntity.java
│   │   └── migration/                     # Migrações (Liquibase)
│   │       └── MigrationStrategy.java
│   ├── service/                           # Camada de Negócio
│   │   ├── BoardService.java
│   │   ├── BoardQueryService.java
│   │   ├── CardService.java
│   │   ├── CardQueryService.java
│   │   └── BoardColumnQueryService.java
│   ├── ui/                                # Interface com Usuário
│   │   ├── MainMenu.java
│   │   └── BoardMenu.java
│   └── util/                              # Utilitários
│       └── ValidationUtils.java
├── src/main/resources/
│   ├── liquibase.properties               # Config Liquibase
│   └── db/changelog/                      # Scripts de migração
│       ├── db.changelog-master.yml
│       └── migrations/
├── build.gradle.kts                       # Configuração Gradle
├── gradlew                                # Gradle Wrapper
└── README.md
```

------------------------------------------------------------------------

## ▶️ Como Executar

### Pré-requisitos

- **Java 17+**
- **MySQL 8.0+**
- **Gradle 7.0+** (ou usar o wrapper `./gradlew`)

### Configuração do Banco de Dados

1. Criar banco de dados:
```sql
CREATE DATABASE board CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'board'@'localhost' IDENTIFIED BY 'board';
GRANT ALL PRIVILEGES ON board.* TO 'board'@'localhost';
FLUSH PRIVILEGES;
```

2. As tabelas serão criadas automaticamente via **Liquibase** na primeira execução.

### Executando a Aplicação

**Opção 1: Via Gradle**
```bash
cd /home/henrique/idea/board
./gradlew build
./gradlew run
```

**Opção 2: Via linha de comando**
```bash
cd /home/henrique/idea/board
./gradlew clean build --no-daemon
java -cp build/classes/java/main br.com.dio.Main
```

**Opção 3: Via IDE (IntelliJ IDEA)**
1. Abra o projeto em File → Open
2. Execute `Main.java` diretamente

------------------------------------------------------------------------

## 🎮 Como Usar

### Menu Principal
1. **Criar Board** - Defina o nome e colunas customizadas
2. **Selecionar Board** - Entre em um board existente
3. **Excluir Board** - Remove um board pelo ID
4. **Sair** - Encerra a aplicação

### Menu do Board
1. **Criar Card** - Adiciona uma nova tarefa
2. **Mover Card** - Move card para próxima coluna
3. **Bloquear Card** - Bloqueia com motivo
4. **Desbloquear Card** - Remove bloqueio
5. **Cancelar Card** - Move para coluna de cancelamento
6. **Ver Board** - Exibe resumo do board
7. **Ver Coluna** - Visualiza cards de uma coluna específica
8. **Ver Card** - Detalhes completos do card

------------------------------------------------------------------------

## 📚 Padrões e Conceitos Aplicados

### Design Patterns
- **DAO Pattern** - Abstração de acesso a dados
- **Service Locator** - Serviços centralizados
- **DTOs** - Transferência de dados entre camadas

### Princípios SOLID
- **S**ingle Responsibility - Cada classe tem uma responsabilidade
- **O**pen/Closed - Aberto para extensão, fechado para modificação
- **L**iskov Substitution - Substituição de tipos
- **I**nterface Segregation - Interfaces específicas
- **D**ependency Inversion - Dependências invertidas

### Práticas Java Modernas
- Try-with-resources para gerenciamento de recursos
- Optional para valores nulos
- Records para imutabilidade
- Lambda expressions para callbacks
- Logging estruturado

------------------------------------------------------------------------

## 📊 Tecnologias Utilizadas

| Tecnologia | Versão | Propósito |
|-----------|--------|----------|
| **Java** | 17+ | Linguagem principal |
| **MySQL** | 8.0+ | Banco de dados relacional |
| **Liquibase** | 4.29.1 | Controle de versão do BD |
| **Lombok** | 1.18.34 | Redução de boilerplate |
| **Gradle** | 7.0+ | Build e dependências |

------------------------------------------------------------------------

## 🔍 Fluxo de Dados

```
UI (Scanner) 
    ↓
MainMenu / BoardMenu (Apresentação)
    ↓
Service Layer (Lógica de Negócio)
    ↓
DAO Layer (Acesso a Dados)
    ↓
MySQL Database
```

---------------------------------

## 👨‍💻 Autor

Projeto desenvolvido como parte do desafio de Padrões de Projeto da DIO.