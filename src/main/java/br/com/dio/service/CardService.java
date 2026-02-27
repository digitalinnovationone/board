package br.com.dio.service;

import br.com.dio.dto.BoardColumnInfoDTO;
import br.com.dio.dto.CardDetailsDTO;
import br.com.dio.exception.CardBlockedException;
import br.com.dio.exception.CardFinishedException;
import br.com.dio.exception.EntityNotFoundException;
import br.com.dio.persistence.dao.BlockDAO;
import br.com.dio.persistence.dao.CardDAO;
import br.com.dio.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static br.com.dio.persistence.entity.BoardColumnKindEnum.CANCEL;
import static br.com.dio.persistence.entity.BoardColumnKindEnum.FINAL;

@AllArgsConstructor
public class CardService {

    private final Connection connection;

    public CardEntity create(final CardEntity entity) throws SQLException {
        var cardDAO = new CardDAO(connection);
        try {
            cardDAO.insert(entity);
            connection.commit();
            return entity;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void moveToNextColumn(final Long cardId, final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException {
        var cardDAO = new CardDAO(connection);
        try {
            BoardColumnInfoDTO currentColumn = findCurrentColumn(cardId, boardColumnsInfo, cardDAO);

            if (currentColumn.kind().equals(FINAL))
                throw new CardFinishedException("O card já foi finalizado");

            BoardColumnInfoDTO nextColumn = boardColumnsInfo.stream()
                    .filter(bc -> bc.order() == currentColumn.order() + 1)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("O card está cancelado"));

            cardDAO.moveToColumn(nextColumn.id(), cardId);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void cancel(final Long cardId, final Long cancelColumnId, final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException {
        var cardDAO = new CardDAO(connection);
        try {
            BoardColumnInfoDTO currentColumn = findCurrentColumn(cardId, boardColumnsInfo, cardDAO);

            if (currentColumn.kind().equals(FINAL))
                throw new CardFinishedException("O card já foi finalizado");

            // Verifica se há próximo passo antes de cancelar
            boardColumnsInfo.stream()
                    .filter(bc -> bc.order() == currentColumn.order() + 1)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("O card está cancelado"));

            cardDAO.moveToColumn(cancelColumnId, cardId);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void block(final Long cardId, final String reason, final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException {
        var cardDAO = new CardDAO(connection);
        var blockDAO = new BlockDAO(connection);
        try {
            BoardColumnInfoDTO currentColumn = findCurrentColumn(cardId, boardColumnsInfo, cardDAO);

            if (currentColumn.kind().equals(FINAL) || currentColumn.kind().equals(CANCEL))
                throw new IllegalStateException("O card está em uma coluna do tipo " + currentColumn.kind() + " e não pode ser bloqueado");

            blockDAO.block(reason, cardId);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void unblock(final Long cardId, final String reason) throws SQLException {
        var cardDAO = new CardDAO(connection);
        var blockDAO = new BlockDAO(connection);
        try {
            CardDetailsDTO card = cardDAO.findById(cardId)
                    .orElseThrow(() -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(cardId)));

            if (!card.blocked())
                throw new CardBlockedException("O card %s não está bloqueado".formatted(cardId));

            blockDAO.unblock(reason, cardId);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    // Helper para localizar coluna atual e validar bloqueio
    private BoardColumnInfoDTO findCurrentColumn(Long cardId, List<BoardColumnInfoDTO> boardColumnsInfo, CardDAO cardDAO) {
        CardDetailsDTO card = cardDAO.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(cardId)));

        if (card.blocked())
            throw new CardBlockedException("O card %s está bloqueado".formatted(cardId));

        return boardColumnsInfo.stream()
                .filter(bc -> bc.id().equals(card.columnId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board"));
    }
}
