package br.com.dio.persistence.dao;

import br.com.dio.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import br.com.dio.exception.DataAccessException;

import static br.com.dio.persistence.config.JdbcUtils.getGeneratedKey;

@AllArgsConstructor
public class BoardDAO {

    private final Connection connection;

    public BoardEntity insert(final BoardEntity entity) {
        final String sql = "INSERT INTO BOARDS (name) VALUES (?);";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getName());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                var id = getGeneratedKey(rs);
                entity.setId(id);
            }
            return entity;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert board", e);
        }
    }

    public void delete(final Long id) {
        final String sql = "DELETE FROM BOARDS WHERE id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete board", e);
        }
    }

    public Optional<BoardEntity> findById(final Long id) {
        final String sql = "SELECT id, name FROM BOARDS WHERE id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BoardEntity entity = new BoardEntity();
                    entity.setId(rs.getLong("id"));
                    entity.setName(rs.getString("name"));
                    return Optional.of(entity);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find board by id", e);
        }
    }

    public boolean exists(final Long id) {
        final String sql = "SELECT 1 FROM BOARDS WHERE id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to check board existence", e);
        }
    }

}
