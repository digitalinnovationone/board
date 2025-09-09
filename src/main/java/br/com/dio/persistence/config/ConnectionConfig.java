package br.com.dio.persistence.config;

import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ConnectionConfig {

    public static Connection getConnection() throws SQLException {
        // Corrigido: usar variável de ambiente em vez de system property
        var dbHost = System.getenv("DB_HOST");
        var url = dbHost != null ? 
            "jdbc:mysql://" + dbHost + "/board" : "jdbc:mysql://vmlinuxd/board";
        
        var user = System.getenv("DB_USER") != null ? 
            System.getenv("DB_USER") : "board";
        var password = System.getenv("DB_PASSWORD") != null ? 
            System.getenv("DB_PASSWORD") : "board";
            
        var connection = DriverManager.getConnection(url, user, password);
        connection.setAutoCommit(false);
        return connection;
    }

}