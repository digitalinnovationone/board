# Projeto Prático: Gerenciador de Boards (Kanban)

Este projeto é uma aplicação de linha de comando em Java para gerenciar boards no estilo Kanban. Ele foi desenvolvido como um desafio prático para consolidar conceitos de **Programação Orientada a Objetos (POO)** e **persistência de dados** usando Java.

## ✨ Destaques do Projeto

* **POO:** Aplicação dos quatro pilares da POO: Abstração, Encapsulamento, Herança e Polimorfismo.
* **Design Patterns:** O projeto segue o padrão **DDD (Domain-Driven Design)**, com camadas de UI, Service e Persistence (DAO).
* **Persistência de Dados:** O projeto utiliza o JDBC para interagir com um banco de dados relacional, permitindo a criação, leitura, atualização e exclusão de boards, colunas e cards.
* **Refatoração:** O código foi estruturado para ser limpo e de fácil manutenção, com métodos e classes coesas.

## 🚀 Funcionalidades

O aplicativo permite ao usuário:

* Criar um novo board com colunas personalizáveis.
* Selecionar e interagir com um board existente.
* Criar, mover, bloquear, desbloquear e cancelar cards.
* Visualizar o status de um board, suas colunas e cards.
* Excluir um board completo.

## 🛠️ Tecnologias Utilizadas

* **Java 17+**
* **JDBC (Java Database Connectivity)**
* **Lombok:** Para reduzir o boilerplate de código.
* **Maven:** Para gerenciar as dependências do projeto.
* **Git:** Para versionamento do código.
* **SQL (MySQL/PostgreSQL):** Para o banco de dados.

## ⚙️ Como Executar o Projeto

1.  **Configurar o Banco de Dados:**
    * Crie um banco de dados com o nome `board`.
    * Execute os seguintes scripts SQL para criar as tabelas necessárias:

    ```sql
    -- Tabela BOARDS
    CREATE TABLE IF NOT EXISTS BOARDS (
      id BIGINT NOT NULL AUTO_INCREMENT,
      name VARCHAR(255) NOT NULL,
      PRIMARY KEY (id)
    );

    -- Tabela BOARDS_COLUMNS
    CREATE TABLE IF NOT EXISTS BOARDS_COLUMNS (
      id BIGINT NOT NULL AUTO_INCREMENT,
      name VARCHAR(255) NOT NULL,
      `order` INT NOT NULL,
      kind VARCHAR(255) NOT NULL,
      board_id BIGINT NOT NULL,
      PRIMARY KEY (id),
      FOREIGN KEY (board_id) REFERENCES BOARDS (id) ON DELETE CASCADE
    );

    -- Tabela CARDS
    CREATE TABLE IF NOT EXISTS CARDS (
      id BIGINT NOT NULL AUTO_INCREMENT,
      title VARCHAR(255) NOT NULL,
      description VARCHAR(255) NOT NULL,
      board_column_id BIGINT NOT NULL,
      created_at TIMESTAMP NOT NULL,
      PRIMARY KEY (id),
      FOREIGN KEY (board_column_id) REFERENCES BOARDS_COLUMNS (id) ON DELETE CASCADE
    );

    -- Tabela BLOCKS
    CREATE TABLE IF NOT EXISTS BLOCKS (
      id BIGINT NOT NULL AUTO_INCREMENT,
      blocked_at TIMESTAMP NOT NULL,
      unblocked_at TIMESTAMP NULL,
      block_reason VARCHAR(255) NOT NULL,
      unblock_reason VARCHAR(255) NULL,
      card_id BIGINT NOT NULL,
      PRIMARY KEY (id),
      FOREIGN KEY (card_id) REFERENCES CARDS (id) ON DELETE CASCADE
    );
    ```

    * Altere as credenciais de conexão no arquivo `ConnectionConfig.java` conforme necessário (`url`, `user`, `password`).

2.  **Configurar o Projeto no IntelliJ:**
    * Abra o projeto no IntelliJ. O Maven deve automaticamente baixar as dependências (`mysql-connector-j`, `lombok`).
    * Selecione o arquivo `MainMenu.java` e execute a classe principal.

3.  **Executar a Aplicação:**
    * A aplicação irá iniciar no console. Siga as instruções no menu para criar seu primeiro board, adicionar cards e interagir com eles.

## 🤝 Contribuições

Sinta-se à vontade para fazer um **fork** deste projeto e aprimorá-lo. As contribuições são bem-vindas! Se você tiver uma ideia de melhoria, crie uma **branch** para a sua funcionalidade e envie um **pull request**.

---

### Próximos Passos (Ideias para Melhorias)

* Implementar testes unitários para as classes de serviço e DAO.
* Adicionar validação de dados nas entradas do usuário.
* Criar uma interface gráfica (GUI) para tornar o aplicativo mais amigável.
* Implementar funcionalidades de usuário e autenticação.