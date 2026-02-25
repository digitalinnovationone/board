#**Board Challenge** – README

##**Atenção:** no arquivo ConnectionConfig.java a senha do banco foi deixada vazia (var password = "").
##Para rodar o projeto, insira sua própria senha do MySQL local antes da execução.

Este projeto foi desenvolvido como parte de um desafio de programação em Java com persistência em MySQL.

✅##Durante o desafio, foram realizadas as seguintes implementações e ajustes:

Correção de entradas do usuário

Substituído o uso de scanner.nextInt(), scanner.nextLong() e scanner.next() por scanner.nextLine() com conversão segura (Integer.parseInt / Long.parseLong).

Essa mudança eliminou erros de InputMismatchException e tornou a interação com menus mais estável.

Atualização do MainMenu

Adicionada a funcionalidade de excluir boards, permitindo remover registros diretamente pelo menu.

Atualização do BoardMenu

Corrigidas todas as entradas para criação, movimentação, bloqueio, desbloqueio e cancelamento de cards.

Agora é possível interagir com cards sem que o programa quebre.

Validação com MySQL Workbench e MySQL Shell

Foram testadas todas as operações diretamente no banco de dados (boards e cards) para confirmar que os registros são persistidos corretamente.

Criação de um roteiro de testes para validar cada funcionalidade.

✅ ##Resultado

Com essas alterações, o sistema agora permite:

Criar, selecionar e excluir boards.

Criar, mover, bloquear, desbloquear e cancelar cards.

Visualizar boards, colunas e cards sem erros de execução.

Confirmar todas as operações diretamente no banco de dados.

📝 ##Roteiro de Testes

🔹 **Boards**

Criar um board

Menu principal → opção 1.

Nome: BoardTeste.

Colunas extras: 0.

Colunas: Inicial, Final, Cancelamento.

Esperado: board criado.

**Validação:**

sql
SELECT * FROM boards;
Selecionar um board

Menu principal → opção 2.

Digitar o id do board.

Esperado: abre o menu do board.

Excluir um board

Menu principal → opção 3.

Digitar o id do board.

Esperado: mensagem confirmando exclusão.

Validação:

sql
SELECT * FROM boards WHERE id = <id>;
→ Não retorna nada.

🔹 **Cards**

Criar um card

Menu do board → opção 1.

Título: CardTeste.

Descrição: Primeiro card.

Esperado: card criado na coluna inicial.

**Validação:**

sql
SELECT * FROM cards;
Ver board

Menu do board → opção 6.

Esperado: lista colunas e mostra card em Inicial.

Mover card

Menu do board → opção 2.

Digitar o id do card.

Esperado: card movido para Final.

Validação:

sql
SELECT * FROM cards WHERE id = <id>;
Bloquear card

Menu do board → opção 3.

Digitar o id e motivo.

Esperado: card marcado como bloqueado.

Desbloquear card

Menu do board → opção 4.

Digitar o id e motivo.

Esperado: card liberado.

Cancelar card

Menu do board → opção 5.

Digitar o id.

Esperado: card movido para Cancelamento.

Ver coluna específica

Menu do board → opção 7.

Digitar o id da coluna.

Esperado: lista os cards dessa coluna.

Ver card específico

Menu do board → opção 8.

Digitar o id do card.

Esperado: mostra título, descrição, status e coluna atual.