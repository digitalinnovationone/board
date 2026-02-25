package br.com.dio;

import br.com.dio.persistence.migration.MigrationStrategy;
import br.com.dio.service.CardService;
import br.com.dio.ui.MainMenu;

import java.sql.SQLException;

import static br.com.dio.persistence.config.ConnectionConfig.getConnection;

public class Main {

    public static void main(String[] args) throws SQLException {
        try (var connection = getConnection()) {
            // Executa as migrações
            new MigrationStrategy(connection).executeMigration();

            // Instancia o CardService com a conexão
            CardService cardService = new CardService(connection);

            // 🚀 Teste: excluir um card pelo ID
            Long cardIdParaExcluir = 1L; // ajuste para o ID que você quer testar
            try {
                cardService.delete(cardIdParaExcluir);
                System.out.println("Card " + cardIdParaExcluir + " excluído com sucesso!");
            } catch (Exception e) {
                System.out.println("Erro ao excluir card: " + e.getMessage());
            }
        }

        // Continua com o menu principal
        new MainMenu().execute();
    }
}