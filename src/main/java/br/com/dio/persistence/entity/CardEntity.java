package br.com.dio.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CardEntity {

    private Long id;
    private String title;
    private String description;
    private BoardColumnEntity boardColumn;

    public CardEntity(Long id, String title, String description, BoardColumnEntity boardColumn) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.boardColumn = boardColumn;
    }

    /**
     * Atualiza o título e descrição do card se os valores fornecidos não forem nulos.
     */
    public void updateDetails(String title, String description) {
        if (title != null) this.title = title;
        if (description != null) this.description = description;
    }

    /**
     * Move o card para a coluna alvo, caso não seja nula.
     */
    public void moveToColumn(BoardColumnEntity targetColumn) {
        if (targetColumn != null) this.boardColumn = targetColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardEntity)) return false;
        CardEntity that = (CardEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
