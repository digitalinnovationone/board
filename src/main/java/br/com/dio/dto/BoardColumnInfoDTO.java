package br.com.dio.dto;

import br.com.dio.persistence.entity.BoardColumnKindEnum;

public record BoardColumnInfoDTO(
        Long id,
        int order,
        BoardColumnKindEnum kind
) {

    public boolean isInitial() {
        return kind == BoardColumnKindEnum.INITIAL;
    }

    public boolean isFinal() {
        return kind == BoardColumnKindEnum.FINAL;
    }

    public boolean isCancel() {
        return kind == BoardColumnKindEnum.CANCEL;
    }

    public boolean isPending() {
        return kind == BoardColumnKindEnum.PENDING;
    }
}
