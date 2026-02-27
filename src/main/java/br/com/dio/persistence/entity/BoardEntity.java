package br.com.dio.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static br.com.dio.persistence.entity.BoardColumnKindEnum.CANCEL;
import static br.com.dio.persistence.entity.BoardColumnKindEnum.INITIAL;

@Getter
@ToString
@NoArgsConstructor
public class BoardEntity {

    private Long id;
    private String name;

    @ToString.Exclude
    private final List<BoardColumnEntity> boardColumns = new ArrayList<>();

    public BoardEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
    }

    public void updateName(String name) {
        setName(name);
    }

    public void addColumn(BoardColumnEntity column) {
        if (column != null) {
            boardColumns.add(column);
            column.assignToBoard(this);
        }
    }

    public void setBoardColumns(List<BoardColumnEntity> columns) {
        if (columns == null) return;
        boardColumns.clear();
        columns.forEach(this::addColumn);
    }

    public List<BoardColumnEntity> getBoardColumns() {
        return Collections.unmodifiableList(boardColumns);
    }

    public BoardColumnEntity getInitialColumn() {
        return findColumnByKind(INITIAL);
    }

    public BoardColumnEntity getCancelColumn() {
        return findColumnByKind(CANCEL);
    }

    private BoardColumnEntity findColumnByKind(BoardColumnKindEnum kind) {
        return boardColumns.stream()
                .filter(column -> column.getKind() == kind)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Column with kind " + kind + " not found in board " + id));
    }

    public BoardColumnEntity findColumn(Predicate<BoardColumnEntity> filter) {
        return boardColumns.stream()
                .filter(filter)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No column found matching filter in board " + id));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardEntity)) return false;
        BoardEntity that = (BoardEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
