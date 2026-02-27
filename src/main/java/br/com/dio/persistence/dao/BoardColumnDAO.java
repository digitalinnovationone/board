package br.com.dio.persistence.dao;

import br.com.dio.dto.BoardColumnDTO;
import br.com.dio.persistence.entity.BoardColumnEntity;
import br.com.dio.persistence.entity.BoardColumnKindEnum;
import br.com.dio.persistence.entity.CardEntity;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.dio.exception.DataAccessException;

import static java.util.Objects.isNull;
import static br.com.dio.persistence.config.JdbcUtils.getGeneratedKey;

@RequiredArgsConstructor
public class BoardColumnDAO {

    private final Connection connection;

    // Insere uma coluna e retorna a entidade com ID preenchido
    public BoardColumnEntity insert(final BoardColumnEntity entity) {
        final String sql = "INSERT INTO BOARDS_COLUMNS (name, `order`, kind, board_id) VALUES (?, ?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            stmt.setString(i++, entity.getName());
            stmt.setInt(i++, entity.getOrder());
            stmt.setString(i++, entity.getKind().name());
            stmt.setLong(i, entity.getBoard().getId());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                entity.setId(getGeneratedKey(rs));
            }
            return entity;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert board column", e);
        }
    }

    // Busca todas as colunas de um board pelo ID
    public List<BoardColumnEntity> findByBoardId(final Long boardId) {
        final String sql = "SELECT id, name, `order`, kind FROM BOARDS_COLUMNS WHERE board_id = ? ORDER BY `order`";
        List<BoardColumnEntity> entities = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, boardId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BoardColumnEntity entity = new BoardColumnEntity();
                    entity.setId(rs.getLong("id"));
                    entity.setName(rs.getString("name"));
                    entity.setOrder(rs.getInt("order"));
                    entity.setKind(BoardColumnKindEnum.findByName(rs.getString("kind")));
                    entities.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find board columns by board id", e);
        }
        return entities;
    }

    // Busca colunas com quantidade de cards (DTO)
    public List<BoardColumnDTO> findByBoardIdWithDetails(final Long boardId) {
        final String sql = """
                SELECT bc.id,
                       bc.name,
                       bc.kind,
                       (SELECT COUNT(c.id) FROM CARDS c WHERE c.board_column_id = bc.id) AS cards_amount
                  FROM BOARDS_COLUMNS bc
                 WHERE board_id = ?
                 ORDER BY `order`;
                """;

        List<BoardColumnDTO> dtos = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, boardId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BoardColumnDTO dto = new BoardColumnDTO(
                            rs.getLong("id"),
                            rs.getString("name"),
                            BoardColumnKindEnum.findByName(rs.getString("kind")),
                            rs.getInt("cards_amount")
                    );
                    dtos.add(dto);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find board columns with details", e);
        }
        return dtos;
    }

    // Busca coluna por ID, incluindo cards associados
    public Optional<BoardColumnEntity> findById(final Long columnId) {
        final String sql = """
                SELECT bc.name,
                       bc.kind,
                       c.id AS card_id,
                       c.title AS card_title,
                       c.description AS card_description
                  FROM BOARDS_COLUMNS bc
                  LEFT JOIN CARDS c ON c.board_column_id = bc.id
                 WHERE bc.id = ?;
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, columnId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                BoardColumnEntity entity = new BoardColumnEntity();
                entity.setName(rs.getString("name"));
                entity.setKind(BoardColumnKindEnum.findByName(rs.getString("kind")));

                List<CardEntity> cards = new ArrayList<>();
                do {
                    String title = rs.getString("card_title");
                    if (isNull(title)) continue;

                    CardEntity card = new CardEntity();
                    card.setId(rs.getLong("card_id"));
                    card.setTitle(title);
                    card.setDescription(rs.getString("card_description"));
                    cards.add(card);

                } while (rs.next());

                entity.setCards(cards);
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find board column by id", e);
        }
    }
}
