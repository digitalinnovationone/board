package br.com.dio.persistence.config;

import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ConnectionConfig {

    private static final String URL = "jdbc:mysql://localhost/board";
    private static final String USER = "board";
    private static final String PASSWORD = "board";

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        connection.setAutoCommit(false);
        return connection;
    }
}
