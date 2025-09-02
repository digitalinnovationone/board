--liquibase formatted sql
--changeset junior:202509021124
--comment: inserts for kanban tables

-- Inserir dados na tabela BOARDS
INSERT INTO BOARDS (id, name) VALUES (1, 'Projeto Kanban Exemplo');

-- Inserir dados na tabela BOARDS_COLUMNS
-- To Do (Ordem 0)
INSERT INTO BOARDS_COLUMNS (id, name, `order`, kind, board_id) VALUES (1, 'A Fazer', 0, 'TODO', 1);

-- In Progress (Ordem 1)
INSERT INTO BOARDS_COLUMNS (id, name, `order`, kind, board_id) VALUES (2, 'Em Andamento', 1, 'IN_PROGRESS', 1);

-- Done (Ordem 2)
INSERT INTO BOARDS_COLUMNS (id, name, `order`, kind, board_id) VALUES (3, 'Concluído', 2, 'DONE', 1);

-- Inserir dados na tabela CARDS
-- Cards para a coluna 'A Fazer'
INSERT INTO CARDS (id, title, description, board_column_id) VALUES (1, 'Configurar o ambiente', 'Instalar dependências e configurar o ambiente de desenvolvimento.', 1);
INSERT INTO CARDS (id, title, description, board_column_id) VALUES (2, 'Criar a API de usuários', 'Desenvolver os endpoints para gerenciar usuários.', 1);

-- Cards para a coluna 'Em Andamento'
INSERT INTO CARDS (id, title, description, board_column_id) VALUES (3, 'Implementar autenticação', 'Desenvolver a lógica de login e JWT para autenticação.', 2);

-- Cards para a coluna 'Concluído'
INSERT INTO CARDS (id, title, description, board_column_id) VALUES (4, 'Modelar o banco de dados', 'Criar as tabelas para o projeto e as relações entre elas.', 3);
INSERT INTO CARDS (id, title, description, board_column_id) VALUES (5, 'Preparar o Dockerfile', 'Escrever o Dockerfile para conteinerizar a aplicação.', 3);

-- Inserir dados na tabela BLOCKS
-- Bloquear o card de "Implementar autenticação"
INSERT INTO BLOCKS (id, blocked_at, block_reason, card_id) VALUES (1, NOW(), 'Aguardando revisão do código.', 3);
