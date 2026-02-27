package br.com.dio.service;

import br.com.dio.dto.CardDetailsDTO;
import br.com.dio.persistence.dao.CardDAO;

import java.sql.Connection;
import java.util.Optional;

public class CardQueryService {

    private final CardDAO cardDAO;

    public CardQueryService(Connection connection) {
        this.cardDAO = new CardDAO(connection);
    }

    public Optional<CardDetailsDTO> findById(Long id) {
        return cardDAO.findById(id);
    }
}
