package br.com.dio;

import br.com.dio.controller.BoardController;
import br.com.dio.persistence.migration.MigrationStrategy;

import java.sql.SQLException;

import static br.com.dio.persistence.config.ConnectionConfig.getConnection;

public class Main {

    public static void main(String[] args) throws Exception {
        // Aguardar banco estar pronto com retry
        waitForDatabase();
        
        // Executar migrations
        try(var connection = getConnection()){
            new MigrationStrategy(connection).executeMigration();
        }
        
        // Iniciar servidor web
        new BoardController().startServer();
    }
    
    private static void waitForDatabase() {
        int maxRetries = 30;
        int retryCount = 0;
        
        while (retryCount < maxRetries) {
            try {
                System.out.println("Tentando conectar ao banco de dados...");
                try (var connection = getConnection()) {
                    System.out.println("Conexão com banco estabelecida com sucesso!");
                    return;
                }
            } catch (SQLException e) {
                retryCount++;
                System.out.printf("Tentativa %d/%d falhou. Aguardando 2 segundos...\n", 
                    retryCount, maxRetries);
                
                if (retryCount >= maxRetries) {
                    System.err.println("Não foi possível conectar ao banco após " + maxRetries + " tentativas");
                    System.exit(1);
                }
                
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    System.exit(1);
                }
            }
        }
    }
}