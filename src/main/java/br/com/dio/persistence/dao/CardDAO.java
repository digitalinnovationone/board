package br.com.dio.persistence.dao;

import br.com.dio.dto.CardDetailsDTO;
import br.com.dio.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static br.com.dio.persistence.converter.OffsetDateTimeConverter.toOffsetDateTime;
import static java.util.Objects.nonNull;

import br.com.dio.exception.DataAccessException;
import static br.com.dio.persistence.config.JdbcUtils.getGeneratedKey;

@AllArgsConstructor
public class CardDAO {

    private final Connection connection;

    public CardEntity insert(final CardEntity entity) {
        final String sql = "INSERT INTO CARDS (title, description, board_column_id) VALUES (?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            stmt.setString(i++, entity.getTitle());
            stmt.setString(i++, entity.getDescription());
            stmt.setLong(i, entity.getBoardColumn().getId());

            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                entity.setId(getGeneratedKey(rs));
            }
            return entity;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert card", e);
        }
    }

    public void moveToColumn(final Long columnId, final Long cardId) {
        final String sql = "UPDATE CARDS SET board_column_id = ? WHERE id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int i = 1;
            stmt.setLong(i++, columnId);
            stmt.setLong(i, cardId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to move card to column", e);
        }
    }

    public Optional<CardDetailsDTO> findById(final Long id) {
        final String sql = """
                SELECT c.id,
                       c.title,
                       c.description,
                       b.blocked_at,
                       b.block_reason,
                       c.board_column_id,
                       bc.name,
                       (SELECT COUNT(sub_b.id)
                          FROM BLOCKS sub_b
                         WHERE sub_b.card_id = c.id) AS blocks_amount
                  FROM CARDS c
                  LEFT JOIN BLOCKS b
                    ON c.id = b.card_id AND b.unblocked_at IS NULL
                 INNER JOIN BOARDS_COLUMNS bc
                    ON bc.id = c.board_column_id
                 WHERE c.id = ?;
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CardDetailsDTO dto = new CardDetailsDTO(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            nonNull(rs.getString("block_reason")),
                            toOffsetDateTime(rs.getTimestamp("blocked_at")),
                            rs.getString("block_reason"),
                            rs.getInt("blocks_amount"),
                            rs.getLong("board_column_id"),
                            rs.getString("name")
                    );
                    return Optional.of(dto);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find card by id", e);
        }
    }
}
