--liquibase formatted sql
--changeset Fabiano:202509021200
--comment: inserts for kanban tables with correct enum values

-- Inserir board exemplo
INSERT INTO BOARDS (name) VALUES ('Projeto Kanban Exemplo');

-- Inserir colunas do board (usando os enum corretos do Java)
INSERT INTO BOARDS_COLUMNS (name, `order`, kind, board_id) 
VALUES 
('A Fazer', 0, 'INITIAL', 1),
('Em Andamento', 1, 'PENDING', 1),
('Concluido', 2, 'FINAL', 1),
('Cancelado', 3, 'CANCEL', 1);

-- Inserir cards de exemplo
INSERT INTO CARDS (title, description, board_column_id) 
VALUES 
('Configurar o ambiente', 'Instalar dependencias e configurar o ambiente de desenvolvimento.', 1),
('Criar a API de usuarios', 'Desenvolver os endpoints para gerenciar usuarios.', 1),
('Implementar autenticacao', 'Desenvolver a logica de login e JWT para autenticacao.', 2),
('Modelar o banco de dados', 'Criar as tabelas para o projeto e as relacoes entre elas.', 3),
('Preparar o Dockerfile', 'Escrever o Dockerfile para conteinerizar a aplicacao.', 3);

-- Bloquear um card de exemplo
INSERT INTO BLOCKS (blocked_at, block_reason, card_id) 
VALUES (NOW(), 'Aguardando revisao do codigo.', 3);