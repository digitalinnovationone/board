package br.com.dio.dto;

import java.util.List;

public record BoardDetailsDTO(
        Long id,
        String name,
        List<BoardColumnDTO> columns
) {

    public int totalColumns() {
        return columns.size();
    }

    public int totalCards() {
        return columns.stream()
                .mapToInt(BoardColumnDTO::cardsAmount)
                .sum();
    }
}
