package br.com.dio.ui;

import br.com.dio.dto.BoardColumnInfoDTO;
import br.com.dio.persistence.entity.BoardColumnEntity;
import br.com.dio.persistence.entity.BoardEntity;
import br.com.dio.persistence.entity.CardEntity;
import br.com.dio.service.BoardColumnQueryService;
import br.com.dio.service.BoardQueryService;
import br.com.dio.service.CardQueryService;
import br.com.dio.service.CardService;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static br.com.dio.persistence.config.ConnectionConfig.getConnection;

public class BoardMenu {

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");
    private final BoardEntity entity;

    public BoardMenu(BoardEntity entity) {
        this.entity = entity;
    }

    public void execute() {
        System.out.printf("Bem-vindo ao board %s (%s)%n", entity.getId(), entity.getName());
        int option = -1;
        while (option != 9) {
            showMenu();
            option = scanner.nextInt();
            try {
                switch (option) {
                    case 1 -> createCard();
                    case 2 -> moveCardToNextColumn();
                    case 3 -> blockCard();
                    case 4 -> unblockCard();
                    case 5 -> cancelCard();
                    case 6 -> showBoard();
                    case 7 -> showColumn();
                    case 8 -> showCard();
                    case 9 -> System.out.println("Voltando para o menu anterior...");
                    case 10 -> System.exit(0);
                    default -> System.out.println("Opção inválida, informe uma opção válida.");
                }
            } catch (RuntimeException ex) {
                System.out.println("Erro: " + ex.getMessage());
            }
        }
    }

    private void showMenu() {
        System.out.println("\n1 - Criar um card");
        System.out.println("2 - Mover um card");
        System.out.println("3 - Bloquear um card");
        System.out.println("4 - Desbloquear um card");
        System.out.println("5 - Cancelar um card");
        System.out.println("6 - Ver board");
        System.out.println("7 - Ver coluna com cards");
        System.out.println("8 - Ver card");
        System.out.println("9 - Voltar para o menu anterior");
        System.out.println("10 - Sair");
        System.out.print("Escolha: ");
    }

    private void createCard() {
        CardEntity card = new CardEntity();

        System.out.print("Informe o título do card: ");
        card.setTitle(scanner.next());

        System.out.print("Informe a descrição do card: ");
        card.setDescription(scanner.next());

        card.setBoardColumn(entity.getInitialColumn());

        try (var connection = getConnection()) {
            new CardService(connection).create(card);
        } catch (SQLException ex) {
            System.out.println("Erro ao conectar ao banco: " + ex.getMessage());
        } catch (RuntimeException ex) {
            System.out.println("Erro ao criar card: " + ex.getMessage());
        }

        System.out.println("Card criado com sucesso!");
    }

    private void moveCardToNextColumn() {
        long cardId = promptLong("Informe o ID do card que deseja mover para a próxima coluna: ");
        List<BoardColumnInfoDTO> columnsInfo = mapColumnsToDTO();

        try (var connection = getConnection()) {
            new CardService(connection).moveToNextColumn(cardId, columnsInfo);
            System.out.println("Card movido com sucesso!");
        } catch (SQLException ex) {
            System.out.println("Erro ao conectar ao banco: " + ex.getMessage());
        } catch (RuntimeException ex) {
            System.out.println("Erro ao mover card: " + ex.getMessage());
        }
    }

    private void blockCard() {
        long cardId = promptLong("Informe o ID do card que será bloqueado: ");
        String reason = promptString("Informe o motivo do bloqueio: ");
        List<BoardColumnInfoDTO> columnsInfo = mapColumnsToDTO();

        try (var connection = getConnection()) {
            new CardService(connection).block(cardId, reason, columnsInfo);
            System.out.println("Card bloqueado com sucesso!");
        } catch (SQLException ex) {
            System.out.println("Erro ao conectar ao banco: " + ex.getMessage());
        } catch (RuntimeException ex) {
            System.out.println("Erro ao bloquear card: " + ex.getMessage());
        }
    }

    private void unblockCard() {
        long cardId = promptLong("Informe o ID do card que será desbloqueado: ");
        String reason = promptString("Informe o motivo do desbloqueio: ");

        try (var connection = getConnection()) {
            new CardService(connection).unblock(cardId, reason);
            System.out.println("Card desbloqueado com sucesso!");
        } catch (SQLException ex) {
            System.out.println("Erro ao conectar ao banco: " + ex.getMessage());
        } catch (RuntimeException ex) {
            System.out.println("Erro ao desbloquear card: " + ex.getMessage());
        }
    }

    private void cancelCard() {
        long cardId = promptLong("Informe o ID do card que deseja mover para a coluna de cancelamento: ");
        long cancelColumnId = entity.getCancelColumn().getId();
        List<BoardColumnInfoDTO> columnsInfo = mapColumnsToDTO();

        try (var connection = getConnection()) {
            new CardService(connection).cancel(cardId, cancelColumnId, columnsInfo);
            System.out.println("Card cancelado com sucesso!");
        } catch (SQLException ex) {
            System.out.println("Erro ao conectar ao banco: " + ex.getMessage());
        } catch (RuntimeException ex) {
            System.out.println("Erro ao cancelar card: " + ex.getMessage());
        }
    }

    private void showBoard() {
        try (var connection = getConnection()) {
            new BoardQueryService(connection).showBoardDetails(entity.getId())
                    .ifPresent(b -> {
                        System.out.printf("Board [%s, %s]%n", b.id(), b.name());
                        b.columns().forEach(c ->
                                System.out.printf("Coluna [%s] tipo [%s] tem %d cards%n", c.name(), c.kind(), c.cardsAmount())
                        );
                    });
        } catch (SQLException ex) {
            System.out.println("Erro ao conectar ao banco: " + ex.getMessage());
        } catch (RuntimeException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    private void showColumn() {
        long selectedId;
        List<Long> columnIds = entity.getBoardColumns().stream().map(BoardColumnEntity::getId).toList();
        do {
            System.out.printf("Escolha uma coluna do board %s pelo ID:%n", entity.getName());
            entity.getBoardColumns().forEach(c ->
                    System.out.printf("%s - %s [%s]%n", c.getId(), c.getName(), c.getKind())
            );
            selectedId = scanner.nextLong();
        } while (!columnIds.contains(selectedId));

        try (var connection = getConnection()) {
            new BoardColumnQueryService(connection).findById(selectedId)
                    .ifPresent(co -> {
                        System.out.printf("Coluna %s tipo %s%n", co.getName(), co.getKind());
                        co.getCards().forEach(ca ->
                                System.out.printf("Card %s - %s%nDescrição: %s%n", ca.getId(), ca.getTitle(), ca.getDescription())
                        );
                    });
        } catch (SQLException ex) {
            System.out.println("Erro ao conectar ao banco: " + ex.getMessage());
        } catch (RuntimeException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    private void showCard() {
        long cardId = promptLong("Informe o ID do card que deseja visualizar: ");
        try (var connection = getConnection()) {
            new CardQueryService(connection).findById(cardId)
                    .ifPresentOrElse(c -> {
                        System.out.printf("Card %s - %s%n", c.id(), c.title());
                        System.out.printf("Descrição: %s%n", c.description());
                        System.out.println(c.blocked() ? "Está bloqueado. Motivo: " + c.blockReason() : "Não está bloqueado");
                        System.out.printf("Já foi bloqueado %d vezes%n", c.blocksAmount());
                        System.out.printf("Está na coluna %s - %s%n", c.columnId(), c.columnName());
                    }, () -> System.out.printf("Não existe um card com o ID %d%n", cardId));
        } catch (SQLException ex) {
            System.out.println("Erro ao conectar ao banco: " + ex.getMessage());
        } catch (RuntimeException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    private List<BoardColumnInfoDTO> mapColumnsToDTO() {
        return entity.getBoardColumns().stream()
                .map(c -> new BoardColumnInfoDTO(c.getId(), c.getOrder(), c.getKind()))
                .collect(Collectors.toList());
    }

    private long promptLong(String message) {
        System.out.print(message);
        return scanner.nextLong();
    }

    private String promptString(String message) {
        System.out.print(message);
        return scanner.next();
    }
}
