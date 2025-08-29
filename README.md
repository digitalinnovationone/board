<!-- 
  Tags: Dev
  Label: ☯ Board
  Description: Board
  path_hook: hookfigma.hook7
-->

# Board Management System - DIO Challenge

Sistema de gerenciamento de boards estilo Kanban desenvolvido em Java, utilizando arquitetura em camadas com padrão DAO e migrations com Liquibase.

## 📋 Visão Geral

Este projeto implementa um sistema de boards onde é possível:
- Criar e gerenciar boards
- Organizar tarefas em colunas (cards)
- Mover cards entre colunas
- Bloquear/desbloquear cards
- Cancelar cards
- Visualizar detalhes completos de boards e cards

## 🏗️ Arquitetura do Projeto

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

#### 1. Camada UI (User Interface)
- **MainMenu**: Menu principal do sistema
- **BoardMenu**: Menu específico para operações dentro de um board

#### 2. Camada Services
- **BoardService**: Operações de criação e exclusão de boards
- **BoardQueryService**: Consultas relacionadas a boards
- **CardService**: Operações CRUD e movimentação de cards
- **CardQueryService**: Consultas relacionadas a cards
- **BoardColumnQueryService**: Consultas de colunas

#### 3. Camada DAO (Data Access Object)
- **BoardDAO**: Persistência de boards
- **BoardColumnDAO**: Persistência de colunas
- **CardDAO**: Persistência de cards
- **BlockDAO**: Persistência de bloqueios

#### 4. Entidades
- **BoardEntity**: Representa um board
- **BoardColumnEntity**: Representa uma coluna do board
- **CardEntity**: Representa um card
- **BlockEntity**: Representa um bloqueio

#### 5. DTOs (Data Transfer Objects)
- **BoardDetailsDTO**: Detalhes completos de um board
- **BoardColumnDTO**: Informações de coluna com contagem de cards
- **CardDetailsDTO**: Detalhes completos de um card
- **BoardColumnInfoDTO**: Informações básicas de coluna

## 🗄️ Modelo de Dados

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

## ⚙️ Funcionalidades

### Gerenciamento de Boards
1. **Criar Board**: Define nome e estrutura de colunas
2. **Selecionar Board**: Acessa um board específico
3. **Excluir Board**: Remove um board do sistema
4. **Visualizar Board**: Mostra estrutura completa com contagem de cards

### Gerenciamento de Cards
1. **Criar Card**: Adiciona novo card na coluna inicial
2. **Mover Card**: Move para próxima coluna seguindo fluxo
3. **Bloquear Card**: Impede movimentação com justificativa
4. **Desbloquear Card**: Libera card bloqueado
5. **Cancelar Card**: Move card para coluna de cancelamento
6. **Visualizar Card**: Mostra detalhes completos

### Regras de Negócio
- Cards bloqueados não podem ser movidos
- Cards finalizados não podem ser alterados
- Cards cancelados não podem ser movidos
- Sistema mantém histórico de bloqueios

## 🔧 Tecnologias Utilizadas

- **Java 17+**: Linguagem principal
- **MySQL**: Banco de dados
- **Liquibase**: Controle de versão do banco
- **Lombok**: Redução de boilerplate
- **JDBC**: Conectividade com banco
- **Docker/Docker Compose**: Orquestração e conteinerização

## 🚀 Como Executar

### Pré-requisitos
```bash
- Docker
- Docker Compose
```

### Construa e inicie os contêineres:
```bash
cd /home/userlnx/docker/script_docker/java/desafios/board-dio
./gradlew clean shadowJar
docker-compose up --build
docker-compose up -d db && docker-compose run --rm --no-deps app
```

### Execução Local (Alternativa)
```bash
- Java 17+
- MySQL Server
- Gradle 7.0+
```

### Configuração do Banco
```sql
CREATE DATABASE board;
CREATE USER 'board'@'localhost' IDENTIFIED BY 'board';
GRANT ALL PRIVILEGES ON board.* TO 'board'@'localhost';
```

### Execução
```bash
# Com Gradle Wrapper (recomendado)
./gradlew run

# Ou diretamente
gradle run

# Ou compilar e executar separadamente
./gradlew build
java -cp build/libs/*:build/classes/java/main br.com.dio.Main
```

### testando database
```bash
docker exec -it board-dio-db mysql -u board -p # senha board
USE board;
SHOW TABLES;
SELECT * FROM BOARDS;
```

## 📈 Melhorias Implementadas

### 1. Conteinerização Completa com Docker
- O projeto pode ser executado em qualquer ambiente com Docker instalado, eliminando problemas de dependências e configurações de ambiente.
- O docker-compose.yml orquestra a aplicação e o banco de dados, garantindo que o MySQL esteja pronto antes que a aplicação tente se conectar.
- A execução do projeto é simplificada para um único comando.

## 🔍 Possíveis Melhorias Futuras

### 1. Sistema de Logs
```java
// Implementar logging estruturado
private static final Logger logger = LoggerFactory.getLogger(CardService.class);

public void moveCard(Long cardId) {
    logger.info("Iniciando movimentação do card: {}", cardId);
    try {
        // lógica existente
        logger.info("Card {} movido com sucesso", cardId);
    } catch (Exception e) {
        logger.error("Erro ao mover card {}: {}", cardId, e.getMessage());
        throw e;
    }
}
```

### 2. Validações de Entrada
```java
public class CardValidator {
    public static void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Título do card não pode ser vazio");
        }
        if (title.length() > 100) {
            throw new IllegalArgumentException("Título deve ter no máximo 100 caracteres");
        }
    }
}
```

### 3. Pool de Conexões
```java
// Substituir ConnectionConfig por pool
@Component
public class DatabaseConfig {
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost/board");
        config.setUsername("board");
        config.setPassword("board");
        config.setMaximumPoolSize(10);
        return new HikariDataSource(config);
    }
}
```

### 4. API REST
```java
@RestController
@RequestMapping("/api/boards")
public class BoardController {
    
    @GetMapping("/{id}")
    public ResponseEntity<BoardDetailsDTO> getBoard(@PathVariable Long id) {
        // implementação
    }
    
    @PostMapping("/{boardId}/cards")
    public ResponseEntity<CardEntity> createCard(@PathVariable Long boardId, 
                                               @RequestBody CreateCardRequest request) {
        // implementação
    }
}
```

### 5. Testes Unitários
```java
@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    
    @Mock
    private Connection connection;
    
    @Mock
    private CardDAO cardDAO;
    
    @InjectMocks
    private CardService cardService;
    
    @Test
    void shouldCreateCardSuccessfully() {
        // implementação do teste
    }
}
```

### 6. Configuração Externalizada
```yaml
# application.yml
database:
  url: jdbc:mysql://localhost/board
  username: ${DB_USER:board}
  password: ${DB_PASSWORD:board}
  pool:
    maximum-size: 10
    minimum-idle: 2
```

## 📊 Métricas e Monitoramento

### Possíveis Implementações:
- Tempo de execução de operações
- Número de cards por status
- Frequência de bloqueios
- Taxa de conclusão de cards

## 🤝 Como Contribuir

1. Faça fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Faça commit das mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

## 📝 Conceitos Demonstrados

- **Arquitetura em Camadas**: Separação clara de responsabilidades
- **Padrão DAO**: Abstraão do acesso a dados
- **DTO Pattern**: Transferência eficiente de dados
- **Migrations**: Controle de versão do schema
- **Transações**: Consistência de dados
- **Enum com Métodos**: Encapsulamento de lógica
- **Optional**: Tratamento seguro de valores nulos
- **Try-with-resources**: Gerenciamento automático de recursos

---

**Autor**: [Fabiano Rocha/Fabiuniz](https://github.com/SeuUsuarioGitHub)
**Desafio**: DIO - Digital Innovation One  
**Data**: 2025