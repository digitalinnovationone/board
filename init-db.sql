-- Script de inicialização do MySQL para permitir acesso externo

-- Criar usuário root com acesso de qualquer host (se necessário)
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY 'root_password';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;

-- Garantir que o usuário board pode acessar de qualquer host
CREATE USER IF NOT EXISTS 'board'@'%' IDENTIFIED BY 'board';
GRANT ALL PRIVILEGES ON board.* TO 'board'@'%';
GRANT ALL PRIVILEGES ON board.* TO 'board'@'vmlinuxd';

-- Aplicar as alterações
FLUSH PRIVILEGES;