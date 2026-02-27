package br.com.dio.ui;

import br.com.dio.persistence.entity.BoardColumnEntity;
import br.com.dio.persistence.entity.BoardColumnKindEnum;
import br.com.dio.persistence.entity.BoardEntity;
import br.com.dio.service.BoardQueryService;
import br.com.dio.service.BoardService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static br.com.dio.persistence.config.ConnectionConfig.getConnection;
import static br.com.dio.persistence.entity.BoardColumnKindEnum.*;

public class MainMenu {

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public void execute() {
        System.out.println("Bem-vindo ao gerenciador de boards!");
        int option;
        while (true) {
            showMenu();
            option = scanner.nextInt();
            switch (option) {
                case 1 -> createBoard();
                case 2 -> selectBoard();
                case 3 -> deleteBoard();
                case 4 -> System.exit(0);
                default -> System.out.println("Opção inválida, informe uma opção válida do menu.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n1 - Criar um novo board");
        System.out.println("2 - Selecionar um board existente");
        System.out.println("3 - Excluir um board");
        System.out.println("4 - Sair");
        System.out.print("Escolha: ");
    }

    private void createBoard() {
        BoardEntity board = new BoardEntity();

        System.out.print("Informe o nome do seu board: ");
        board.setName(scanner.next());

        System.out.print("Seu board terá colunas além das 3 padrões? (Digite 0 se não): ");
        int additionalColumns = scanner.nextInt();

        List<BoardColumnEntity> columns = new ArrayList<>();
        columns.add(createColumn("Inicial", INITIAL, 0, "Informe o nome da coluna inicial: "));

        for (int i = 0; i < additionalColumns; i++) {
            columns.add(createColumn("Pendente", PENDING, i + 1, "Informe o nome da coluna de tarefa pendente: "));
        }

        columns.add(createColumn("Final", FINAL, additionalColumns + 1, "Informe o nome da coluna final: "));
        columns.add(createColumn("Cancelamento", CANCEL, additionalColumns + 2, "Informe o nome da coluna de cancelamento: "));

        board.setBoardColumns(columns);

        try (var connection = getConnection()) {
            var service = new BoardService(connection);
            service.insert(board);
        } catch (SQLException ex) {
            System.out.println("Erro ao conectar ao banco: " + ex.getMessage());
        } catch (RuntimeException ex) {
            System.out.println("Erro ao criar board: " + ex.getMessage());
        }

        System.out.println("Board criado com sucesso!");
    }

    private void selectBoard() {
        System.out.print("Informe o ID do board que deseja selecionar: ");
        long id = scanner.nextLong();

        try (var connection = getConnection()) {
            var queryService = new BoardQueryService(connection);
            queryService.findById(id).ifPresentOrElse(
                    board -> new BoardMenu(board).execute(),
                    () -> System.out.printf("Não foi encontrado um board com ID %d%n", id)
            );
        } catch (SQLException ex) {
            System.out.println("Erro ao conectar ao banco: " + ex.getMessage());
        } catch (RuntimeException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    private void deleteBoard() {
        System.out.print("Informe o ID do board que será excluído: ");
        long id = scanner.nextLong();

        try (var connection = getConnection()) {
            var service = new BoardService(connection);
            if (service.delete(id)) {
                System.out.printf("O board %d foi excluído%n", id);
            } else {
                System.out.printf("Não foi encontrado um board com ID %d%n", id);
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao conectar ao banco: " + ex.getMessage());
        } catch (RuntimeException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    private BoardColumnEntity createColumn(String defaultName, BoardColumnKindEnum kind, int order, String prompt) {
        System.out.print(prompt);
        String name = scanner.next();

        BoardColumnEntity column = new BoardColumnEntity();
        column.setName(name.isBlank() ? defaultName : name);
        column.setKind(kind);
        column.setOrder(order);
        return column;
    }
}
