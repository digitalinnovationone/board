package br.com.dio.persistence.dao;

import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import static br.com.dio.persistence.converter.OffsetDateTimeConverter.toTimestamp;
import br.com.dio.exception.DataAccessException;

@AllArgsConstructor
public class BlockDAO {

    private final Connection connection;

    public void block(final String reason, final Long cardId) {
        final String sql = "INSERT INTO BLOCKS (blocked_at, block_reason, card_id) VALUES (?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int i = 1;
            stmt.setTimestamp(i++, toTimestamp(OffsetDateTime.now()));
            stmt.setString(i++, reason);
            stmt.setLong(i, cardId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to block card", e);
        }
    }

    public void unblock(final String reason, final Long cardId) {
        final String sql = """
                UPDATE BLOCKS
                   SET unblocked_at = ?, unblock_reason = ?
                 WHERE card_id = ? AND unblock_reason IS NULL;
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int i = 1;
            stmt.setTimestamp(i++, toTimestamp(OffsetDateTime.now()));
            stmt.setString(i++, reason);
            stmt.setLong(i, cardId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to unblock card", e);
        }
    }
}
