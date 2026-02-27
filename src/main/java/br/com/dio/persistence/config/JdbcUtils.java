package br.com.dio.persistence.config;

import br.com.dio.exception.DataAccessException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class JdbcUtils {

    private JdbcUtils() {}

    public static long getGeneratedKey(ResultSet rs) {
        try {
            if (rs != null && rs.next()) {
                return rs.getLong(1);
            }
            throw new DataAccessException("No generated key returned");
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve generated key", e);
        }
    }

    public static PreparedStatement prepareStatementWithKeys(java.sql.Connection connection, String sql) {
        try {
            return connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to prepare statement with generated keys", e);
        }
    }
}
