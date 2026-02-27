package br.com.dio.service;

import br.com.dio.persistence.dao.BoardColumnDAO;
import br.com.dio.persistence.dao.BoardDAO;
import br.com.dio.persistence.entity.BoardColumnEntity;
import br.com.dio.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
public class BoardService {

    private final Connection connection;

    public BoardEntity insert(final BoardEntity entity) throws SQLException {
        BoardDAO boardDAO = new BoardDAO(connection);
        BoardColumnDAO columnDAO = new BoardColumnDAO(connection);

        try {
            boardDAO.insert(entity);

            List<BoardColumnEntity> columns = entity.getBoardColumns();
            columns.forEach(column -> {
                column.setBoard(entity);
                columnDAO.insert(column);
            });

            connection.commit();
            return entity;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public boolean delete(final Long id) throws SQLException {
        BoardDAO boardDAO = new BoardDAO(connection);

        try {
            if (!boardDAO.exists(id)) return false;

            boardDAO.delete(id);
            connection.commit();
            return true;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }
}
