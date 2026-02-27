package br.com.dio.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
@NoArgsConstructor
public class BoardColumnEntity {

    private Long id;
    private String name;
    private int order;
    private BoardColumnKindEnum kind;
    private BoardEntity board;

    @ToString.Exclude
    private final List<CardEntity> cards = new ArrayList<>();

    public BoardColumnEntity(Long id, String name, int order, BoardColumnKindEnum kind, BoardEntity board) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.kind = kind;
        this.board = board;
    }

    public void updateDetails(String name, int order, BoardColumnKindEnum kind) {
        setName(name);
        setOrder(order);
        setKind(kind);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        if (name != null && !name.isBlank()) this.name = name;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setKind(BoardColumnKindEnum kind) {
        if (kind != null) this.kind = kind;
    }

    public void setBoard(BoardEntity board) {
        this.board = board;
    }

    public void assignToBoard(BoardEntity board) {
        setBoard(board);
    }

    public void addCard(CardEntity card) {
        if (card != null && !cards.contains(card)) {
            cards.add(card);
            card.moveToColumn(this);
        }
    }

    public void removeCard(CardEntity card) {
        if (card != null) cards.remove(card);
    }

    public List<CardEntity> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public void setCards(List<CardEntity> cardList) {
        if (cardList == null) return;
        cards.clear();
        cardList.forEach(this::addCard);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardColumnEntity)) return false;
        BoardColumnEntity that = (BoardColumnEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
