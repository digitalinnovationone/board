package br.com.dio.persistence.migration;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

@AllArgsConstructor
public class MigrationStrategy {

    private static final Logger LOGGER = Logger.getLogger(MigrationStrategy.class.getName());

    private final Connection connection;

    public void executeMigration() {
        var handler = createFileHandler();
        if (handler != null) {
            LOGGER.addHandler(handler);
        }
        try (JdbcConnection jdbcConn = new JdbcConnection(connection)) {
            Liquibase liquibase = new Liquibase(
                    "/db/changelog/db.changelog-master.yml",
                    new ClassLoaderResourceAccessor(),
                    jdbcConn
            );
            liquibase.update("");
            LOGGER.info("Database migration completed successfully.");
        } catch (LiquibaseException e) {
            LOGGER.log(Level.SEVERE, "Liquibase migration failed", e);
        } finally {
            if (handler != null) handler.close();
            try {
                if (!connection.isClosed()) connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to close connection after migration", e);
            }
        }
    }

    private FileHandler createFileHandler() {
        try {
            var fh = new FileHandler("liquibase.log", true);
            fh.setFormatter(new SimpleFormatter());
            return fh;
        } catch (Exception e) {
            Logger.getLogger(MigrationStrategy.class.getName()).log(Level.WARNING, "Could not create file handler for migration logs", e);
            return null;
        }
    }

}
