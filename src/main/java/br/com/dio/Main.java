package br.com.dio;

import br.com.dio.persistence.migration.MigrationStrategy;
import br.com.dio.ui.MainMenu;

import java.sql.Connection;
import java.sql.SQLException;

import static br.com.dio.persistence.config.ConnectionConfig.getConnection;

public class Main {

    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            new MigrationStrategy(connection).executeMigration();
        } catch (SQLException e) {
            System.err.println("Falha na conexão ou migração do banco: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        new MainMenu().execute();
    }
}
