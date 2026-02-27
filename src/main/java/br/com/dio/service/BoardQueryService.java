package br.com.dio.service;

import br.com.dio.dto.BoardDetailsDTO;
import br.com.dio.persistence.dao.BoardColumnDAO;
import br.com.dio.persistence.dao.BoardDAO;
import br.com.dio.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.util.Optional;

@AllArgsConstructor
public class BoardQueryService {

    private final Connection connection;

    public Optional<BoardEntity> findById(final Long id) {
        BoardDAO boardDAO = new BoardDAO(connection);
        BoardColumnDAO columnDAO = new BoardColumnDAO(connection);

        return boardDAO.findById(id).map(entity -> {
            entity.setBoardColumns(columnDAO.findByBoardId(entity.getId()));
            return entity;
        });
    }

    public Optional<BoardDetailsDTO> showBoardDetails(final Long id) {
        BoardDAO boardDAO = new BoardDAO(connection);
        BoardColumnDAO columnDAO = new BoardColumnDAO(connection);

        return boardDAO.findById(id).map(entity -> {
            var columns = columnDAO.findByBoardIdWithDetails(entity.getId());
            return new BoardDetailsDTO(entity.getId(), entity.getName(), columns);
        });
    }
}
