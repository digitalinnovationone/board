-- Script de inicialização do MySQL para permitir acesso externo
-- (Este arquivo não deve conter comandos de criação de usuário ou permissões)

-- Remova estas linhas:
-- CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY 'root_password';
-- GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
-- CREATE USER IF NOT EXISTS 'board'@'%' IDENTIFIED BY 'board';
-- GRANT ALL PRIVILEGES ON board.* TO 'board'@'%';
-- GRANT ALL PRIVILEGES ON board.* TO 'board'@'vmlinuxd';
-- FLUSH PRIVILEGES;

-- Se você precisar de alguma migração inicial, adicione aqui.
-- Por exemplo:
-- CREATE TABLE `migration_history` (
--   `id` INT NOT NULL AUTO_INCREMENT,
--   `script_name` VARCHAR(255) NOT NULL,
--   `executed_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--   PRIMARY KEY (`id`)
-- );