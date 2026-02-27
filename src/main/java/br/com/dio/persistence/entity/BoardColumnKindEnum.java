package br.com.dio.persistence.entity;

import java.util.Arrays;

public enum BoardColumnKindEnum {

    INITIAL,
    FINAL,
    CANCEL,
    PENDING;

    /**
     * Converte uma string em BoardColumnKindEnum.
     * @param name nome da coluna
     * @return BoardColumnKindEnum correspondente
     * @throws IllegalArgumentException se name for nulo, vazio ou inválido
     */
    public static BoardColumnKindEnum fromName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Column kind name must not be null or blank");
        }

        return Arrays.stream(values())
                .filter(kind -> kind.name().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid BoardColumnKindEnum: " + name));
    }

    /**
     * Mantido para compatibilidade, delega para fromName.
     */
    public static BoardColumnKindEnum findByName(String name) {
        return fromName(name);
    }
}
